// src/app/categories/page.tsx
"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { getCategories } from '@/services/api';
import { Categoria, ApiResponse } from '@/libs/types'; // Importar ApiResponse
import styles from './CategoriesPage.module.css';

export default function CategoriesPage() {
    const [categories, setCategories] = useState<Categoria[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                setLoading(true);
                // La función getCategories devuelve un Promise<ApiResponse<Categoria[]>>
                const response: ApiResponse<Categoria[]> = await getCategories(); //

                // --- CORRECCIÓN CLAVE AQUÍ ---
                // Verificamos que response.data exista y que sea un array.
                if (response.data && Array.isArray(response.data)) { //
                    setCategories(response.data); // ✅ Acceso correcto: response.data
                    setError(null);
                } else {
                    // Si no hay datos o el formato es inesperado, aseguramos que 'categories' sea un array vacío.
                    setCategories([]);
                    // Usamos el error de la respuesta de la API si existe, o un mensaje genérico.
                    setError(response.error || 'No se recibieron datos de categorías o el formato fue inesperado.'); //
                    console.error("Respuesta inesperada de la API:", response);
                }
            } catch (err: any) {
                // Capturamos cualquier error de la llamada a la API o de red
                setError("No se pudieron cargar las categorías. Asegúrate de que el backend esté funcionando correctamente.");
                console.error("Error al cargar categorías:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchCategories();
    }, []);

    if (loading) return <div className={styles.centered}>Cargando categorías...</div>;
    // Usar la clase de estilo para el error si está definida, o un estilo en línea.
    if (error) return <div className={`${styles.centered} ${styles.error || ''}`} style={!styles.error ? { color: "red" } : {}}>{error}</div>;

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Explora Nuestras Categorías</h1>
            <div className={styles.grid}>
                {categories.length > 0 ? ( // Solo renderizamos si hay categorías
                    categories.map((category) => (
                        <Link
                            href={`/categories/${category.categoriaId}`}
                            key={category.categoriaId}
                            className={styles.card}
                        >
                            <h2 className={styles.cardTitle}>{category.nombreCategoria}</h2>
                            <p className={styles.cardDescription}>{category.descripcionCategoria}</p>
                            <span className={styles.cardLink}>Ver productos &rarr;</span>
                        </Link>
                    ))
                ) : (
                    // Mensaje si no hay categorías disponibles después de la carga.
                    !loading && !error && <p className={styles.centered}>No hay categorías disponibles.</p>
                )}
            </div>
        </div>
    );
}