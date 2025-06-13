// EN: src/app/cart/page.tsx
"use client";

import { useCart } from "@/contexts/CartContext";
import styles from './CartPage.module.css';
import Image from "next/image";
import Link from "next/link";
import { useState, useEffect } from "react";
import * as api from '@/services/api';

export default function CartPage() {
    const {
        items,
        total,
        itemCount,
        loading,
        updateItemQuantity,
        removeItem,
        clearAllItems
    } = useCart();

    const [preferenceId, setPreferenceId] = useState<string | null>(null);

    // Reemplaza esta función en src/app/cart/page.tsx

    const handleCheckout = async () => {
        if (total <= 0) {
            alert("No puedes proceder al pago con un carrito vacío o un total de cero.");
            return;
        }
        try {
            const response = await api.createOrderAndGetPreference();
            console.log("✅ PASO 1: Objeto COMPLETO recibido del backend:", response);

            // --- CAMBIO CLAVE ---
            // Verificamos que 'data' y 'data.id' existan antes de usarlos
            if (response.data && response.data.id) {
                setPreferenceId(response.data.id);
            } else {
                // Si no vienen los datos, mostramos el error que devuelve la API
                console.error("Error: la respuesta no contiene un ID de preferencia.", response.error);
                alert(`No se pudo iniciar el proceso de pago: ${response.error}`);
            }

        } catch (error) {
            console.error("Error en handleCheckout:", error);
            alert("No se pudo iniciar el proceso de pago. Inténtalo de nuevo.");
        }
    };
    
    const renderCheckoutButton = (prefId: string) => {
        // Limpiamos el contenedor por si acaso
        const container = document.getElementById("wallet_container");
        if (container) {
            container.innerHTML = "";
        }

        // --- PUNTO CRÍTICO: USA TU PUBLIC KEY REAL AQUÍ ---
        // La obtienes de las credenciales de tu aplicación en Mercado Pago
        const mp = new window.MercadoPago('APP_USR-8b36b358-8042-4dbe-8a83-2574091932ba', {
            locale: 'es-AR'
        });

        mp.bricks().create("wallet", "wallet_container", {
            initialization: {
                preferenceId: prefId,
            },
            customization: {
                texts: {
                    valueProp: 'smart_option',
                },
            },
        });
    };

    useEffect(() => {
        if (preferenceId) {
            console.log("✅ PASO 2: useEffect detectó el ID, renderizando el botón de MP...");
            renderCheckoutButton(preferenceId);
        }
    }, [preferenceId]);

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
                                src={item.imagenProductoUrl ? `http://localhost:8080${item.imagenProductoUrl}` : '/placeholder.jpg'}
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
                                        onChange={(e) => updateItemQuantity(item.productoId, parseInt(e.target.value))}
                                        className={styles.quantityInput}
                                    />
                                </div>
                            </div>
                            <div className={styles.itemActions}>
                                <p className={styles.itemSubtotal}>Subtotal: ${(item.precioUnitario * item.cantidad).toFixed(2)}</p>
                                <button onClick={() => removeItem(item.productoId)} className={styles.removeButton}>
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

                    {!preferenceId && (
                        <button onClick={handleCheckout} className={styles.buttonPrimary}>
                            Proceder al Pago
                        </button>
                    )}

                    <div id="wallet_container"></div>

                    <button onClick={clearAllItems} className={styles.buttonSecondary}>
                        Vaciar Carrito
                    </button>
                </div>
            </div>
        </div>
    );
}