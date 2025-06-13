// src/app/products/[productId]/page.tsx
'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import { getProductById } from '@/services/api';
// 👇 CAMBIO: Importamos ProductoListado además de los otros tipos
import { ProductoDetalle, ImagenProducto, ProductoListado } from '@/libs/types';
import { useCart } from '@/contexts/CartContext';
import styles from './ProductDetailPage.module.css';

export default function ProductDetailPage() {
  const params = useParams();
  // 👇 CAMBIO: Obtenemos todo el contexto para poder verificarlo
  const cartContext = useCart();

  const [product, setProduct] = useState<ProductoDetalle | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedImage, setSelectedImage] = useState<ImagenProducto | null>(null);

  useEffect(() => {
    const productId = Number(params.productId);
    if (isNaN(productId)) {
      setError('ID de producto inválido.');
      setLoading(false);
      return;
    }
    const fetchProduct = async () => {
      try {
        setLoading(true);
        const data = await getProductById(productId);
        setProduct(data);
        const mainImage = data.imagenes.find(img => img.principal) || data.imagenes[0];
        setSelectedImage(mainImage);
      } catch (err) {
        setError('No se pudo encontrar el producto.');
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [params.productId]);

  // 👇 CAMBIO: Este es el código de seguridad. Si el contexto no existe, muestra un error claro.
  if (!cartContext) {
    return (
      <div className={styles.container}>
        <p className={styles.messageError}>
          <b>Error Crítico:</b> El contexto del carrito no está disponible.
          <br />
          Esto usualmente significa que el `CartProvider` no está envolviendo esta página.
          Por favor, verifica que `src/app/layout.tsx` utilice el componente `Providers` y que `src/app/providers.tsx` importe `CartProvider` desde ` '@/contexts/CartContext'`.
        </p>
      </div>
    );
  }

  // Si el contexto existe, ahora sí podemos desestructurar la función.
  const { addToCart } = cartContext;

  if (loading) return <p className={styles.message}>Cargando producto...</p>;
  if (error) return <p className={styles.messageError}>{error}</p>;
  if (!product) return <p className={styles.message}>Producto no encontrado.</p>;

  const handleAddToCart = () => {
    // 👇 CAMBIO: Ajustamos la llamada para que coincida con la firma de tu `CartContextType`.
    // Creamos un objeto que se asemeja a `ProductoListado` a partir de nuestro `ProductoDetalle`.
    const productForCart: ProductoListado = {
      productoId: product.productoId,
      nombreProducto: product.nombre,
      precioVentaActual: product.precio,
      stockActual: product.stockActual,
      imagenUrlPrincipal: selectedImage?.url || null,
      // Rellenamos los campos que no tenemos en la página de detalle.
      categoriaNombre: 'N/A',
      tipoProductoNombre: 'N/A',
    };

    // Llamamos a `addToCart` con los dos argumentos que espera.
    addToCart(productForCart, 1)
      .then(() => {
        alert(`${product.nombre} ha sido añadido al carrito!`);
      })
      .catch((err) => {
        console.error("Error al añadir al carrito:", err);
        alert("Hubo un problema al añadir el producto al carrito.");
      });
  };

  return (
    <div className={styles.container}>
      {/* ...el resto del JSX sigue igual... */}
      <div className={styles.imageGallery}>
        <div className={styles.mainImageContainer}>
          <img
            src={selectedImage?.url || '/placeholder.png'}
            alt={product.nombre}
            className={styles.mainImage}
          />
        </div>
        <div className={styles.thumbnailContainer}>
          {product.imagenes.map((img) => (
            <img
              key={img.id}
              src={img.url}
              alt={`${product.nombre} - vista ${img.id}`}
              className={`${styles.thumbnail} ${selectedImage?.id === img.id ? styles.selectedThumbnail : ''}`}
              onClick={() => setSelectedImage(img)}
            />
          ))}
        </div>
      </div>

      <div className={styles.productDetails}>
        <h1 className={styles.productName}>{product.nombre}</h1>
        <p className={styles.productDescription}>{product.descripcion}</p>
        <p className={styles.productPrice}>${product.precio.toFixed(2)}</p>

        {product.detallesPlanta && (
          <div className={styles.specificDetails}>
            <h4>Cuidados de la Planta</h4>
            <p><strong>Luz:</strong> {product.detallesPlanta.nivelLuz}</p>
            <p><strong>Riego:</strong> {product.detallesPlanta.frecuenciaRiego}</p>
          </div>
        )}

        <div className={styles.stockInfo}>
          {product.stockActual > 0
            ? <p>Disponibles: {product.stockActual}</p>
            : <p className={styles.outOfStock}>Agotado</p>
          }
        </div>

        <button
          className={styles.addToCartButton}
          onClick={handleAddToCart}
          disabled={product.stockActual <= 0}
        >
          Agregar al Carrito
        </button>
      </div>
    </div>
  );
}