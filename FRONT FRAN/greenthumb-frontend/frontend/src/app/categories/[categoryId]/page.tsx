"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { getProducts, getCategoryById } from '@/services/api';
import { ProductoListado, Categoria } from '@/libs/types'; // <-- Cambio Clave: Importar tipos necesarios
// Reutilizamos los estilos de la página de productos
import styles from '../../products/ProductsPage.module.css';
import { ProductCard } from '@/components/ProductCard'; // <-- Cambio Clave: Importar el componente

export default function CategoryProductsPage() {
    const params = useParams();
    const categoryId = params.categoryId as string;

    const [products, setProducts] = useState<ProductoListado[]>([]);
    const [category, setCategory] = useState<Categoria | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!categoryId) return;

        const fetchCategoryData = async () => {
            try {
                setLoading(true);
                // Obtener la información de la categoría y los productos al mismo tiempo
                const [categoryData, productsResponse] = await Promise.all([
                    getCategoryById(Number(categoryId)),
                    getProducts({ categoriaId: Number(categoryId), size: '20' })
                ]);

                setCategory(categoryData);
                setProducts(productsResponse.content);
                setError(null);
            } catch (err) {
                setError("No se pudieron cargar los productos para esta categoría.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchCategoryData();
    }, [categoryId]);

    if (loading) return <div>Cargando productos...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;

    return (
        <div>
            <h1 className={styles.pageTitle}>
                {category ? `Productos de: ${category.nombreCategoria}` : 'Productos'}
            </h1>
            {products.length > 0 ? (
                <div className={styles.grid}>
                    {products.map(product => (
                        // -- Cambio Clave: Usar el componente importado --
                        <ProductCard key={product.productoId} product={product} />
                    ))}
                </div>
            ) : (
                <p>No hay productos disponibles en esta categoría.</p>
            )}
        </div>
    );
}