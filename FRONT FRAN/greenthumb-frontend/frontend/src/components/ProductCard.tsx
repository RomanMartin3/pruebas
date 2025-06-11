"use client";

import Image from 'next/image'; // Asegúrate de que importe desde 'next/image'
import styles from './ProductCard.module.css';
import { useCart } from '@/contexts/CartContext';
import { useState } from 'react';
import { ProductoListado } from '@/libs/types';

export const ProductCard = ({ product }: { product: ProductoListado }) => {
    const { handleAddToCart } = useCart();
    const [isAdding, setIsAdding] = useState(false);

    const onAddToCartClick = async () => {
        setIsAdding(true);
        await handleAddToCart(product.productoId, 1);
        setIsAdding(false);
    };

    return (
        <div className={styles.card}>
            <Image
                src={product.imagenUrlPrincipal ? `http://localhost:8080${product.imagenUrlPrincipal}` : '/placeholder.jpg'}
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
    );
};