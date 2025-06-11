"use client";

import React, { createContext, useContext, useState, useEffect, ReactNode, useMemo, useCallback } from 'react';
import { useAuth } from './AuthContext';
import { CarritoItem, ProductoListado, Pedido } from '@/libs/types';
import * as api from '@/services/api'; // Usamos un alias para mayor claridad

// CORRECCIÓN: La interfaz debe usar los tipos exactos de tu archivo de tipos
interface CartContextType {
    cartItems: CarritoItem[];
    cartItemCount: number;
    isLoading: boolean;
    error: string | null;
    addToCart: (producto: ProductoListado, cantidad: number) => Promise<void>;
    updateCartItemQuantity: (productId: number, cantidad: number) => Promise<void>;
    removeFromCart: (productId: number) => Promise<void>;
    clearCart: () => Promise<void>;
    checkout: (metodoPago: string, notasCliente?: string) => Promise<Pedido | null>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
    const { isAuthenticated, getAccessToken } = useAuth();
    // CORRECCIÓN: El estado debe usar el tipo 'CarritoItem[]'
    const [cartItems, setCartItems] = useState<CarritoItem[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchCart = useCallback(async () => {
        if (isAuthenticated) {
            setIsLoading(true);
            setError(null);
            try {
                const token = await getAccessToken();
                if (!token) throw new Error("No autenticado");

                // Asumiendo que crearás una función `getCarrito` en api.ts
                // const items = await api.getCarrito(token);
                // setCartItems(items);
                
                // NOTA: Como aún no tenemos el endpoint `getCarrito` en api.ts,
                // por ahora lo dejaremos vacío para que no falle.
                // Cuando lo implementes, descomenta las líneas de arriba.
                setCartItems([]); // Placeholder

            } catch (err) {
                setError(err instanceof Error ? err.message : "Error al cargar el carrito");
            } finally {
                setIsLoading(false);
            }
        } else {
            // Si el usuario no está autenticado, el carrito debe estar vacío
            setCartItems([]);
            setIsLoading(false);
        }
    }, [isAuthenticated, getAccessToken]);

    useEffect(() => {
        fetchCart();
    }, [fetchCart]);

    const addToCart = async (producto: ProductoListado, cantidad: number) => {
        // Lógica para agregar al carrito... (requiere endpoint en api.ts)
        console.log("Añadiendo al carrito (simulado):", producto, cantidad);
    };

    const updateCartItemQuantity = async (productId: number, cantidad: number) => {
        // Lógica para actualizar cantidad... (requiere endpoint en api.ts)
        console.log("Actualizando cantidad (simulado):", productId, cantidad);
    };

    const removeFromCart = async (productId: number) => {
        // Lógica para remover item... (requiere endpoint en api.ts)
        console.log("Removiendo del carrito (simulado):", productId);
    };

    const clearCart = async () => {
        // Lógica para limpiar el carrito... (requiere endpoint en api.ts)
        console.log("Limpiando carrito (simulado)");
    };


    const checkout = async (metodoPago: string, notasCliente?: string): Promise<Pedido | null> => {
        // Lógica para finalizar compra... (requiere endpoint en api.ts)
        console.log("Checkout (simulado)", metodoPago, notasCliente);
        return null;
    };

    const cartItemCount = useMemo(() => {
        return cartItems.reduce((acc, item) => acc + item.cantidad, 0);
    }, [cartItems]);

    return (
        <CartContext.Provider value={{
            cartItems,
            cartItemCount,
            isLoading,
            error,
            addToCart,
            updateCartItemQuantity,
            removeFromCart,
            clearCart,
            checkout
        }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (context === undefined) {
        throw new Error('useCart debe ser usado dentro de un CartProvider');
    }
    return context;
};