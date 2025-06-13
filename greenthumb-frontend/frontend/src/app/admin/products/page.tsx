"use client";

import React, { useState, useEffect } from 'react';
import { ProductoListado, ProductoDetalle } from '@/libs/types';
import * as api from '@/services/api';
import Modal from '@/components/Modal';
import ProductForm from '@/components/ProductForm';
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
    
    // --- LÓGICA DE DATOS ---
    const loadProducts = async () => {
        try {
            setIsLoading(true);
            // CORRECCIÓN CLAVE 1: Acceder a data.data.content
            const response = await api.getProducts({ size: '100' }); 
            if (response.data && response.data.content) {
                setProducts(response.data.content);
            } else {
                setProducts([]); // Asegurarse de que sea un array vacío si no hay datos
            }
            setError(null);
        } catch (err: any) {
            setError(err.message || 'Error al cargar los productos. Por favor, intente de nuevo.');
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
            setIsLoading(true);
            // CORRECCIÓN CLAVE 2: Acceder a productDetails.data
            const response = await api.getProductById(productId);
            if (response.data) {
                setSelectedProduct(response.data);
            } else {
                setSelectedProduct(null); // Si no se encuentran los detalles
            }
            setIsModalOpen(true);
        } catch (err: any) {
            setError(err.message || 'No se pudieron cargar los detalles del producto.');
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
            if (selectedProduct) {
                await api.updateProduct(selectedProduct.productoId, formData);
            } else {
                await api.createProduct(formData);
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
                await api.deleteProduct(id);
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
            
            {!isLoading && !error && (products && products.length > 0 ? ( // Asegurarse de que products sea un array y no esté vacío
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
            ) : (
                <p>No hay productos para mostrar.</p> // Mensaje si no hay productos
            ))}

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