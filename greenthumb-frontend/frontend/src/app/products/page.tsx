// src/app/products/page.tsx
"use client";

import { useEffect, useState, useMemo } from "react";
import { getProducts } from "@/services/api";
import { ProductoListado, Page, ApiResponse } from "@/libs/types"; // Importar Page y ApiResponse
import styles from "./ProductsPage.module.css";
import { ProductCard } from "@/components/ProductCard";

export default function ProductsPage() {
  const [products, setProducts] = useState<ProductoListado[]>([]); // Inicialización segura
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [searchTerm, setSearchTerm] = useState("");
  const [sortOrder, setSortOrder] = useState("name-asc");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        // La función getProducts devuelve un Promise<ApiResponse<Page<ProductoListado>>>
        const response: ApiResponse<Page<ProductoListado>> = await getProducts({ size: "100" });

        // --- CORRECCIÓN CLAVE AQUÍ ---
        // Verificamos que response.data exista y que response.data.content sea un array.
        if (response.data && Array.isArray(response.data.content)) {
          setProducts(response.data.content); // ✅ Acceso correcto: response.data.content
          setError(null);
        } else {
          // Si no hay datos o el formato es inesperado, aseguramos que 'products' sea un array vacío.
          setProducts([]);
          // Usamos el error de la respuesta de la API si existe, o un mensaje genérico.
          setError(response.error || 'No se recibieron datos de productos o el formato fue inesperado.');
          console.error("Respuesta inesperada de la API:", response);
        }
      } catch (err: any) {
        // Capturamos cualquier error de la llamada a la API o de red
        setError(
          err.message || "No se pudieron cargar los productos. Por favor, asegúrate de que el backend esté funcionando."
        );
        console.error("Error al cargar productos:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const filteredAndSortedProducts = useMemo(() => {
    // --- CORRECCIÓN CLAVE AQUÍ (dentro de useMemo) ---
    // Aseguramos que 'products' sea siempre un array antes de intentar filtrar o ordenar.
    if (!Array.isArray(products)) {
        console.warn("Products state is not an array, returning empty array.");
        return []; // Retornar un array vacío si products no es un array válido.
    }

    let result = [...products]; // Crear una copia para evitar mutaciones directas al ordenar

    // Filtrado por término de búsqueda
    if (searchTerm) {
      result = result.filter((p) =>
        p.nombreProducto.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Ordenamiento
    result.sort((a, b) => {
      switch (sortOrder) {
        case "price-asc":
          return a.precioVentaActual - b.precioVentaActual;
        case "price-desc":
          return b.precioVentaActual - a.precioVentaActual;
        case "name-asc":
        default:
          return a.nombreProducto.localeCompare(b.nombreProducto);
      }
    });

    return result;
  }, [products, searchTerm, sortOrder]); // Dependencias: products, searchTerm, sortOrder

  if (loading) return <p>Cargando productos...</p>;
  // Usar la clase de estilo para el error si está definida, o un estilo en línea.
  if (error) return <p className={styles.error || ''} style={!styles.error ? { color: "red" } : {}}>{error}</p>;

  return (
    <div className={styles.container}>
      <h1 className={styles.pageTitle}>Nuestros Productos</h1>

      <div className={styles.filtersContainer}>
        <input
          type="text"
          placeholder="Buscar por nombre..."
          className={styles.searchInput}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <select
          className={styles.sortSelect}
          value={sortOrder}
          onChange={(e) => setSortOrder(e.target.value)}
        >
          <option value="name-asc">Ordenar por Nombre (A-Z)</option>
          <option value="price-asc">Ordenar por Precio (Menor a Mayor)</option>
          <option value="price-desc">Ordenar por Precio (Mayor a Menor)</option>
        </select>
      </div>

      <div className={styles.grid}>
        {filteredAndSortedProducts.length > 0 ? (
          filteredAndSortedProducts.map((product) => (
            <ProductCard key={product.productoId} product={product} />
          ))
        ) : (
          // Mensaje si no hay productos o si la búsqueda no encontró nada.
          <p>No se encontraron productos que coincidan con la búsqueda o no hay productos disponibles.</p>
        )}
      </div>
    </div>
  );
}