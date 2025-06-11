"use client";

import React, { useState, useEffect } from 'react';
import { ProductoListado, ProductoDetalle } from '@/libs/types';
import * as api from '@/services/api';
import { useAuth } from '@/contexts/AuthContext';
import Modal from '@/components/Modal';
import ProductForm from '@/components/ProductForm';
// Usaremos nuestro nuevo módulo de estilos
import styles from './AdminProducts.module.css';

const AdminProductsPage: React.FC = () => {
    // --- ESTADOS ---
    const [products, setProducts] = useState<ProductoListado[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Estado para el modal y el formulario
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<ProductoDetalle | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    
    const { getAccessToken } = useAuth();

    // --- LÓGICA DE DATOS ---
    const loadProducts = async () => {
        try {
            setIsLoading(true);
            const data = await api.getProducts({ size: '100' }); // Cargar hasta 100 productos
            setProducts(data.content);
            setError(null);
        } catch (err) {
            setError('Error al cargar los productos. Por favor, intente de nuevo.');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadProducts();
    }, []);

    // --- MANEJADORES DE EVENTOS ---
    const handleOpenModalForCreate = () => {
        setSelectedProduct(null);
        setIsModalOpen(true);
    };

    const handleOpenModalForEdit = async (productId: number) => {
        try {
            setIsLoading(true); // Reutilizamos el loader general
            const productDetails = await api.getProductById(productId);
            setSelectedProduct(productDetails);
            setIsModalOpen(true);
        } catch (err) {
            setError('No se pudieron cargar los detalles del producto.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedProduct(null);
    };

    const handleSubmitForm = async (formData: FormData) => {
        setIsSubmitting(true);
        setError(null);
        try {
            const token = await getAccessToken();
            if (!token) throw new Error("No se pudo obtener el token de autenticación.");

            if (selectedProduct) {
                // Actualizando un producto existente
                await api.updateProduct(selectedProduct.productoId, formData, token);
            } else {
                // Creando un nuevo producto
                await api.createProduct(formData, token);
            }
            handleCloseModal();
            loadProducts(); // Recargar la lista de productos
        } catch (err: any) {
            setError(err.message || 'Ocurrió un error al guardar el producto.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('¿Está seguro de que desea eliminar este producto?')) {
            setError(null);
            try {
                const token = await getAccessToken();
                if (!token) throw new Error("No se pudo obtener el token de autenticación.");

                await api.deleteProduct(id, token);
                loadProducts(); // Recargar la lista
            } catch (err: any) {
                setError(err.message || 'Error al eliminar el producto.');
                console.error(err);
            }
        }
    };

    // --- RENDERIZADO ---
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1>Administrar Productos</h1>
                <button onClick={handleOpenModalForCreate} className={styles.addButton}>
                    Agregar Nuevo Producto
                </button>
            </div>

            {isLoading && <p>Cargando...</p>}
            {error && <p className={styles.error}>{error}</p>}
            
            {!isLoading && !error && (
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Precio</th>
                            <th>Stock</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(p => (
                            <tr key={p.productoId}>
                                <td>{p.productoId}</td>
                                <td>{p.nombreProducto}</td>
                                <td>{p.categoriaNombre}</td>
                                <td>${p.precioVentaActual.toFixed(2)}</td>
                                <td>{p.stockActual}</td>
                                <td>
                                    <div className={styles.actionButtons}>
                                        <button onClick={() => handleOpenModalForEdit(p.productoId)} className={styles.editButton}>
                                            Editar
                                        </button>
                                        <button onClick={() => handleDelete(p.productoId)} className={styles.deleteButton}>
                                            Eliminar
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

            {isModalOpen && (
                 <Modal 
                    isOpen={isModalOpen} 
                    onClose={handleCloseModal}
                    title={selectedProduct ? 'Editar Producto' : 'Crear Nuevo Producto'}
                >
                    <ProductForm
                        initialData={selectedProduct}
                        onSubmit={handleSubmitForm}
                        onCancel={handleCloseModal}
                        isSubmitting={isSubmitting}
                    />
                </Modal>
            )}
        </div>
    );
};

export default AdminProductsPage;