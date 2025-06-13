"use client";

import { createContext, useState, useContext, ReactNode, useEffect, useCallback } from 'react';
import { CarritoItem, ApiResponse } from '@/libs/types'; // Importar ApiResponse
import { getCart, addToCart, updateCartItemQuantity, removeFromCart, clearCart } from '@/services/api';

interface CartContextType {
    items: CarritoItem[];
    itemCount: number;
    total: number;
    loading: boolean;
    error: string | null;
    addItem: (productoId: number, cantidad: number) => Promise<void>;
    updateItemQuantity: (productoId: number, nuevaCantidad: number) => Promise<void>;
    removeItem: (productoId: number) => Promise<void>;
    clearAllItems: () => Promise<void>;
    reloadCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
    const [items, setItems] = useState<CarritoItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchCart = useCallback(async () => {
        try {
            setLoading(true);
            // CORRECCIÓN CLAVE AQUÍ: Acceder a la propiedad 'data' de la respuesta
            const response: ApiResponse<CarritoItem[]> = await getCart();
            setItems(response.data || []); // Usar response.data
            setError(null);
        } catch (err: any) { // Capturar el error como 'any' para acceder a 'message'
            console.error("Failed to fetch cart:", err);
            setError(err.message || "No se pudo cargar el carrito.");
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchCart();
    }, [fetchCart]);

    const addItem = useCallback(async (productoId: number, cantidad: number) => {
        try {
            await addToCart(productoId, cantidad);
            await fetchCart(); // Recargar el carrito para asegurar consistencia
        } catch (err: any) {
            console.error("Failed to add item to cart:", err);
            setError(err.message || "No se pudo añadir el producto al carrito.");
        }
    }, [fetchCart]);

    const updateItemQuantity = useCallback(async (productoId: number, nuevaCantidad: number) => {
        try {
            if (nuevaCantidad < 1) {
                await removeFromCart(productoId);
            } else {
                await updateCartItemQuantity(productoId, nuevaCantidad);
            }
            await fetchCart(); // Recargar
        } catch (err: any) {
            console.error("Failed to update item quantity:", err);
            setError(err.message || "No se pudo actualizar la cantidad del producto.");
        }
    }, [fetchCart]);

    const removeItem = useCallback(async (productoId: number) => {
        try {
            await removeFromCart(productoId);
            await fetchCart(); // Recargar
        } catch (err: any) {
            console.error("Failed to remove item from cart:", err);
            setError(err.message || "No se pudo eliminar el producto del carrito.");
        }
    }, [fetchCart]);

    const clearAllItems = useCallback(async () => {
        try {
            await clearCart();
            await fetchCart(); // Recargar
        } catch (err: any) {
            console.error("Failed to clear cart:", err);
            setError(err.message || "No se pudo vaciar el carrito.");
        }
    }, [fetchCart]);

    const itemCount = items.reduce((sum, item) => sum + item.cantidad, 0);
    const total = items.reduce((sum, item) => {
        const precio = Number(item.precioUnitario) || 0;
        const cantidad = Number(item.cantidad) || 0;
        return sum + (precio * cantidad);
    }, 0);

    return (
        <CartContext.Provider value={{ items, itemCount, total, loading, error, addItem, updateItemQuantity, removeItem, clearAllItems, reloadCart: fetchCart }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (context === undefined) {
        throw new Error('useCart must be used within a CartProvider');
    }
    return context;
};