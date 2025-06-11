"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, ReactNode } from "react";

interface AdminLayoutProps {
    children: ReactNode;
}

export default function AdminLayout({ children }: AdminLayoutProps) {
    const { user, isAuthenticated, isLoading } = useAuth();
    const router = useRouter();

    useEffect(() => {
        // Si no está cargando y el usuario no está autenticado o no es ADMIN, redirigir.
        if (!isLoading) {
            if (!isAuthenticated || user?.rol !== 'ADMIN') {
                // Redirige al inicio o a una página de "acceso denegado"
                router.push('/'); 
            }
        }
    }, [isLoading, isAuthenticated, user, router]);

    // Muestra un mensaje de carga mientras se verifica la autenticación
    if (isLoading) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
                <h2>Verificando permisos de administrador...</h2>
            </div>
        );
    }

    // Si está autenticado y es admin, muestra el contenido de la página
    if (isAuthenticated && user?.rol === 'ADMIN') {
        return <>{children}</>;
    }

    // Muestra null o un loader mientras se redirige para evitar parpadeos
    return null; 
}