// greenthumb/FrontEnd/src/components/CategoryForm.tsx

"use client";

import React, { useState, useEffect } from 'react';
import { Categoria } from '@/libs/types';
import styles from './CategoryForm.module.css';

interface CategoryFormProps {
    initialData?: Categoria | null;
    onSubmit: (data: Omit<Categoria, 'categoriaId'>) => void;
    onCancel: () => void;
    isSubmitting: boolean;
}

const CategoryForm: React.FC<CategoryFormProps> = ({ initialData, onSubmit, onCancel, isSubmitting }) => {
    const [nombre, setNombre] = useState('');
    const [descripcion, setDescripcion] = useState('');

    useEffect(() => {
        if (initialData) {
            setNombre(initialData.nombreCategoria || '');
            setDescripcion(initialData.descripcionCategoria || '');
        }
    }, [initialData]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit({
            nombreCategoria: nombre,
            descripcionCategoria: descripcion,
        });
    };

    const isEditing = !!initialData;

    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            <div className={styles.formGroup}>
                <label htmlFor="nombre">Nombre de la Categoría</label>
                <input
                    id="nombre"
                    type="text"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    required
                    placeholder="Ej: Plantas de Interior"
                />
            </div>
            <div className={styles.formGroup}>
                <label htmlFor="descripcion">Descripción</label>
                <textarea
                    id="descripcion"
                    rows={4}
                    value={descripcion}
                    onChange={(e) => setDescripcion(e.target.value)}
                    required
                    placeholder="Una breve descripción de la categoría..."
                />
            </div>
            <div className={styles.buttonContainer}>
                <button type="button" onClick={onCancel} className={styles.cancelButton} disabled={isSubmitting}>
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