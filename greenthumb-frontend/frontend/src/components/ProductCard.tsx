"use client";

import Image from 'next/image';
import Link from 'next/link';
import styles from './ProductCard.module.css';
import { useCart } from '@/contexts/CartContext';
import { useState } from 'react';
import { ProductoListado } from '@/libs/types'; // ¡Perfecto que uses un tipo específico!

export const ProductCard = ({ product }: { product: ProductoListado }) => {
    const { addItem } = useCart();
    const [isAdding, setIsAdding] = useState(false);

    // --- Lógica de imagen corregida y a prueba de errores ---
    const backendUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
    const placeholderImage = '/placeholder.png'; // La imagen que pusiste en /public

    // La sintaxis del operador ternario ahora es correcta (?:)
    const imageUrl = product.imagenUrlPrincipal
        ? `${backendUrl}${product.imagenUrlPrincipal}`
        : placeholderImage;

    const onAddToCartClick = async (e: React.MouseEvent) => {
        e.preventDefault();
        setIsAdding(true);
        await addItem(product.productoId, 1);
        setIsAdding(false);
    };

    return (
        <Link href={`/products/${product.productoId}`} className={styles.cardLink}>
            <div className={styles.card}>
                <Image
                    src={imageUrl} // Ahora imageUrl siempre será un string válido
                    alt={product.nombreProducto}
                    width={300}
                    height={200}
                    className={styles.cardImage}
                />
                <div className={styles.cardContent}>
                    <h3 className={styles.cardTitle}>{product.nombreProducto}</h3>
                    <p className={styles.cardCategory}>{product.categoriaNombre}</p>
                    <div className={styles.cardFooter}>
                        <p className={styles.cardPrice}>${product.precioVentaActual.toFixed(2)}</p>
                        <button
                            className={styles.cardButton}
                            onClick={onAddToCartClick}
                            disabled={isAdding || product.stockActual === 0}
                        >
                            {product.stockActual === 0 ? 'Sin Stock' : (isAdding ? 'Añadiendo...' : 'Añadir al Carrito')}
                        </button>
                    </div>
                </div>
            </div>
        </Link>
    );
};