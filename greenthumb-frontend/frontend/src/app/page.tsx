// src/app/page.tsx
'use client';

import { useState, useEffect } from 'react';
import { getProducts } from '@/services/api';
import { ProductoListado } from '@/libs/types';
import styles from './page.module.css';
import { ProductCard } from '@/components/ProductCard'; // Asegúrate de que ProductCard se exporta correctamente

export default function HomePage() {
  const [products, setProducts] = useState<ProductoListado[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        // La función getProducts devuelve un Promise<ApiResponse<Page<ProductoListado>>>
        const response = await getProducts({ size: "8" }); 
        
        // Verificamos si response.data existe y si response.data.content existe
        if (response.data && response.data.content) { //
          setProducts(response.data.content); // Accedemos a content a través de data
          setError(null);
        } else {
          // Si no hay datos o contenido, inicializamos products como un array vacío
          // y establecemos un mensaje de error si hay uno en la respuesta de la API.
          setProducts([]);
          setError(response.error || 'No se recibieron datos de productos o hubo un problema.'); //
        }
      } catch (err) {
        // Capturamos cualquier error de la llamada a la API o de red
        setError('No se pudieron cargar los productos. Inténtalo de nuevo más tarde.');
        console.error("Error al cargar productos:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  return (
    <main className={styles.main}>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1 className={styles.title}>Bienvenido a GreenThumb</h1>
          <p className={styles.subtitle}>Tu tienda de plantas online</p>
        </div>
      </section>

      <section className={styles.featuredProducts}>
        <h2 className={styles.sectionTitle}>Productos Destacados</h2>
        {loading && <p>Cargando productos...</p>}
        {error && <p className={styles.error}>{error}</p>}
        {!loading && !error && products.length > 0 && ( // Solo renderizamos si hay productos
          <div className={styles.productsGrid}>
            {products.map((product) => (
              <ProductCard key={product.productoId} product={product} />
            ))}
          </div>
        )}
        {!loading && !error && products.length === 0 && ( // Mensaje si no hay productos
          <p>No hay productos destacados disponibles.</p>
        )}
      </section>
    </main>
  );
}