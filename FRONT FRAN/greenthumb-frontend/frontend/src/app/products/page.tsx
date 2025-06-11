"use client";

import { useEffect, useState } from 'react';
import { getProducts } from '@/services/api';
import { ProductoListado } from '@/libs/types';
import styles from './ProductsPage.module.css';
import { ProductCard } from '@/components/ProductCard'; // <-- Cambio Clave: Importar el componente

export default function ProductsPage() {
    const [products, setProducts] = useState<ProductoListado[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                setLoading(true);
                // Usamos el Page<T> que definimos en types.ts
                const response = await getProducts({ size: '20' });
                setProducts(response.content);
                setError(null);
            } catch (err) {
                setError("No se pudieron cargar los productos. Por favor, asegúrate de que el backend esté funcionando.");
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    if (loading) return <p>Cargando productos...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div>
            <h1 className={styles.pageTitle}>Nuestros Productos</h1>
            <div className={styles.grid}>
                {products.map(product => (
                    // -- Cambio Clave: Usar el componente importado --
                    <ProductCard key={product.productoId} product={product} />
                ))}
            </div>
        </div>
    );
}