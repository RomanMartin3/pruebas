// Ruta: src/app/providers.tsx

"use client";

import React from "react";
import { CartProvider } from "@/contexts/CartContext";
import { AuthProvider } from "@/contexts/AuthContext"; // <-- Importa el AuthProvider

export function Providers({ children }: { children: React.ReactNode }) {
  return (
    // Ahora envolvemos la aplicación con AuthProvider y CartProvider
    <AuthProvider> {/* <-- Envuelve la aplicación con AuthProvider */}
      <CartProvider>
        {children}
      </CartProvider>
    </AuthProvider>
  );
}