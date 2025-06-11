"use client";

import React, { useState, useEffect } from 'react';
import { Categoria } from '@/libs/types';
import styles from './CategoryForm.module.css';

// Usamos Omit para no requerir el 'categoriaId' al crear/actualizar
type CategoryFormData = Omit<Categoria, 'categoriaId'>;

interface CategoryFormProps {
    initialData?: Categoria | null; // Datos para editar, null para crear
    onSubmit: (data: CategoryFormData) => void;
    onCancel: () => void;
    isSubmitting: boolean;
}

const CategoryForm: React.FC<CategoryFormProps> = ({ initialData, onSubmit, onCancel, isSubmitting }) => {
    const [formData, setFormData] = useState<CategoryFormData>({
        nombreCategoria: '',
        descripcionCategoria: '',
    });

    // useEffect para llenar el formulario cuando 'initialData' cambie (al editar)
    useEffect(() => {
        if (initialData) {
            setFormData({
                nombreCategoria: initialData.nombreCategoria,
                descripcionCategoria: initialData.descripcionCategoria,
            });
        } else {
            // Resetear el formulario si no hay datos iniciales (para creación)
            setFormData({
                nombreCategoria: '',
                descripcionCategoria: '',
            });
        }
    }, [initialData]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData);
    };

    const isEditing = !!initialData;

    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            <div className={styles.formGroup}>
                <label htmlFor="nombreCategoria" className={styles.label}>
                    Nombre de la Categoría
                </label>
                <input
                    type="text"
                    id="nombreCategoria"
                    name="nombreCategoria"
                    value={formData.nombreCategoria}
                    onChange={handleChange}
                    className={styles.input}
                    required
                />
            </div>
            <div className={styles.formGroup}>
                <label htmlFor="descripcionCategoria" className={styles.label}>
                    Descripción
                </label>
                <textarea
                    id="descripcionCategoria"
                    name="descripcionCategoria"
                    value={formData.descripcionCategoria}
                    onChange={handleChange}
                    className={styles.textarea}
                    rows={4}
                    required
                />
            </div>
            <div className={styles.buttonContainer}>
                <button type="button" onClick={onCancel} className={styles.cancelButton}>
                    Cancelar
                </button>
                <button type="submit" className={styles.submitButton} disabled={isSubmitting}>
                    {isSubmitting ? 'Guardando...' : (isEditing ? 'Guardar Cambios' : 'Crear Categoría')}
                </button>
            </div>
        </form>
    );
};

export default CategoryForm;