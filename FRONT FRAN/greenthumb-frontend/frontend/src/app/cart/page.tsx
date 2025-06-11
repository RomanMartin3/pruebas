"use client";

import { useCart } from "@/contexts/CartContext";
import styles from './CartPage.module.css';
import Image from "next/image";
import Link from "next/link";

export default function CartPage() {
    const { 
        items, 
        total, 
        itemCount, 
        loading, 
        handleUpdateQuantity, 
        handleRemoveItem,
        handleClearCart 
    } = useCart();

    const handleCheckout = () => {
        alert('Procediendo al pago (funcionalidad pendiente)');
    };

    if (loading) {
        return <div className={styles.centered}>Cargando carrito...</div>;
    }

    if (!items || items.length === 0) {
        return (
            <div className={`${styles.centered} ${styles.emptyCart}`}>
                <h2>Tu carrito está vacío.</h2>
                <Link href="/products" className={styles.buttonPrimary}>
                    Ver productos
                </Link>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Tu Carrito de Compras</h1>
            <div className={styles.cartLayout}>
                <div className={styles.cartItems}>
                    {items.map(item => (
                        <div key={item.productoId} className={styles.item}>
                            <Image 
                                src={item.imagenUrl ? `http://localhost:8080${item.imagenUrl}` : '/placeholder.jpg'}
                                alt={item.nombreProducto}
                                width={100}
                                height={100}
                                className={styles.itemImage}
                            />
                            <div className={styles.itemDetails}>
                                <h3 className={styles.itemName}>{item.nombreProducto}</h3>
                                <p className={styles.itemPrice}>${item.precioUnitario.toFixed(2)}</p>
                                <div className={styles.itemQuantity}>
                                    <label>Cantidad:</label>
                                    <input 
                                        type="number"
                                        min="1"
                                        value={item.cantidad}
                                        onChange={(e) => handleUpdateQuantity(item.productoId, parseInt(e.target.value))}
                                        className={styles.quantityInput}
                                    />
                                </div>
                            </div>
                            <div className={styles.itemActions}>
                                {/* --- CAMBIO CLAVE: Calcular subtotal aquí --- */}
                                <p className={styles.itemSubtotal}>Subtotal: ${(item.precioUnitario * item.cantidad).toFixed(2)}</p>
                                <button onClick={() => handleRemoveItem(item.productoId)} className={styles.removeButton}>
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
                <div className={styles.cartSummary}>
                    <h2>Resumen del Pedido</h2>
                    <div className={styles.summaryLine}>
                        <span>Subtotal ({itemCount} productos)</span>
                        {/* El 'total' ya viene calculado desde el context */}
                        <span>${total.toFixed(2)}</span>
                    </div>
                    <div className={styles.summaryLine}>
                        <span>Envío</span>
                        <span>Gratis</span>
                    </div>
                    <div className={`${styles.summaryLine} ${styles.summaryTotal}`}>
                        <span>Total</span>
                        <span>${total.toFixed(2)}</span>
                    </div>
                    <button onClick={handleCheckout} className={styles.buttonPrimary}>
                        Proceder al Pago
                    </button>
                    <button onClick={handleClearCart} className={styles.buttonSecondary}>
                        Vaciar Carrito
                    </button>
                </div>
            </div>
        </div>
    );
}