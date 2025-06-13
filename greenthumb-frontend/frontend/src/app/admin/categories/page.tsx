// greenthumb/FrontEnd/src/app/admin/categories/page.tsx

"use client";

import React, { useState, useEffect } from 'react';
import { Categoria } from '@/libs/types';
import * as api from '@/services/api';
import Modal from '@/components/Modal';
import CategoryForm from '@/components/CategoryForm';
import styles from './AdminCategories.module.css'; // Y también su CSS

const AdminCategoriesPage: React.FC = () => {
    // --- ESTADOS ---
    const [categories, setCategories] = useState<Categoria[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Estado para el modal y el formulario
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState<Categoria | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // --- LÓGICA DE DATOS ---
    const loadCategories = async () => {
    try {
        setIsLoading(true);
        const response = await api.getCategories();

        // --- CAMBIO CLAVE ---
        // Verificamos si no hubo error y si la propiedad 'data' existe y es un array.
        if (response.data && !response.error) {
            setCategories(response.data); // Guardamos el array, no el objeto completo.
        } else {
            // Si hay un error en la respuesta de la API, lo mostramos.
            setError(response.error || 'No se recibieron datos de categorías.');
        }
    } catch (err: any) {
        // Este catch es para errores de red o si la promesa es rechazada.
        setError('Error al cargar las categorías. Por favor, intente de nuevo.');
        console.error(err);
    } finally {
        setIsLoading(false);
    }
};
    useEffect(() => {
        loadCategories();
    }, []);

    // --- MANEJADORES DE EVENTOS DEL MODAL ---
    const handleOpenModalForCreate = () => {
        setSelectedCategory(null);
        setIsModalOpen(true);
    };

    const handleOpenModalForEdit = (category: Categoria) => {
        setSelectedCategory(category);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedCategory(null);
    };

    // --- MANEJADORES DE ACCIONES CRUD ---
    const handleSubmitForm = async (categoryData: Omit<Categoria, 'categoriaId'>) => {
        setIsSubmitting(true);
        setError(null);
        try {
            if (selectedCategory) {
                await api.updateCategory(selectedCategory.categoriaId, categoryData);
            } else {
                await api.createCategory(categoryData);
            }
            handleCloseModal();
            await loadCategories(); // Recargar la lista de categorías
        } catch (err: any) {
            setError(err.message || 'Ocurrió un error al guardar la categoría.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('¿Está seguro de que desea eliminar esta categoría?')) {
            setError(null);
            try {
                await api.deleteCategory(id);
                await loadCategories(); // Recargar la lista
            } catch (err: any) {
                setError(err.message || 'Error al eliminar la categoría.');
                console.error(err);
            }
        }
    };

    // --- RENDERIZADO ---
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1>Administrar Categorías</h1>
                <button onClick={handleOpenModalForCreate} className={styles.addButton}>
                    Agregar Nueva Categoría
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
                            <th>Descripción</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map(cat => (
                            <tr key={cat.categoriaId}>
                                <td>{cat.categoriaId}</td>
                                <td>{cat.nombreCategoria}</td>
                                <td>{cat.descripcionCategoria}</td>
                                <td>
                                    <div className={styles.actionButtons}>
                                        <button onClick={() => handleOpenModalForEdit(cat)} className={styles.editButton}>
                                            Editar
                                        </button>
                                        <button onClick={() => handleDelete(cat.categoriaId)} className={styles.deleteButton}>
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
                    title={selectedCategory ? 'Editar Categoría' : 'Crear Nueva Categoría'}
                >
                    <CategoryForm
                        initialData={selectedCategory}
                        onSubmit={handleSubmitForm}
                        onCancel={handleCloseModal}
                        isSubmitting={isSubmitting}
                    />
                </Modal>
            )}
        </div>
    );
};

export default AdminCategoriesPage;