"use client";

import React, { useState, useEffect } from 'react';
import { Categoria } from '@/libs/types';
import * as api from '@/services/api';
import { useAuth } from '@/contexts/AuthContext';
import Modal from '@/components/Modal';
import CategoryForm from '@/components/CategoryForm';
import styles from './AdminCategories.module.css';

const AdminCategoriesPage: React.FC = () => {
    const [categories, setCategories] = useState<Categoria[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Estado para el modal
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState<Categoria | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const { getAccessToken } = useAuth();

    const loadCategories = async () => {
        try {
            setIsLoading(true);
            const data = await api.getCategories();
            setCategories(data);
            setError(null);
        } catch (err) {
            setError('Error al cargar las categorías. Por favor, intente de nuevo.');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadCategories();
    }, []);

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

    const handleSubmitForm = async (formData: Omit<Categoria, 'categoriaId'>) => {
        setIsSubmitting(true);
        setError(null);
        try {
            const token = await getAccessToken();
            if (!token) throw new Error("No se pudo obtener el token de autenticación.");

            if (selectedCategory) {
                // Actualizando una categoría existente
                await api.updateCategory(selectedCategory.categoriaId, formData, token);
            } else {
                // Creando una nueva categoría
                await api.createCategory(formData, token);
            }
            handleCloseModal();
            loadCategories(); // Recargar la lista de categorías
        } catch (err: any) {
            setError(err.message || 'Ocurrió un error al guardar la categoría.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('¿Está seguro de que desea eliminar esta categoría? Esta acción no se puede deshacer.')) {
            setError(null);
            try {
                const token = await getAccessToken();
                if (!token) throw new Error("No se pudo obtener el token de autenticación.");

                await api.deleteCategory(id, token);
                loadCategories(); // Recargar la lista
            } catch (err: any) {
                setError(err.message || 'Error al eliminar la categoría.');
                console.error(err);
            }
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1>Administrar Categorías</h1>
                <button onClick={handleOpenModalForCreate} className={styles.addButton}>
                    Agregar Nueva
                </button>
            </div>

            {isLoading && <p>Cargando categorías...</p>}
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
                        {categories.map((cat) => (
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
        </div>
    );
};

export default AdminCategoriesPage;