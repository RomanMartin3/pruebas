"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { getCategories } from '@/services/api';
import { Categoria } from '@/libs/types';
import styles from './CategoriesPage.module.css';

export default function CategoriesPage() {
    const [categories, setCategories] = useState<Categoria[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                setLoading(true);
                const fetchedCategories = await getCategories();
                setCategories(fetchedCategories);
                setError(null);
            } catch (err) {
                setError("No se pudieron cargar las categorías. Asegúrate de que el backend esté funcionando correctamente.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchCategories();
    }, []);

    if (loading) return <div className={styles.centered}>Cargando categorías...</div>;
    if (error) return <div className={`${styles.centered} ${styles.error}`}>{error}</div>;

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Explora Nuestras Categorías</h1>
            <div className={styles.grid}>
                {categories.map((category) => (
                    <Link
                        href={`/categories/${category.categoriaId}`}
                        key={category.categoriaId}
                        className={styles.card}
                    >
                        <h2 className={styles.cardTitle}>{category.nombreCategoria}</h2>
                        <p className={styles.cardDescription}>{category.descripcionCategoria}</p>
                        <span className={styles.cardLink}>Ver productos &rarr;</span>
                    </Link>
                ))}
            </div>
        </div>
    );
}