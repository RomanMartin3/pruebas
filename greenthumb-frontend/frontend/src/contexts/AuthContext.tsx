// src/contexts/AuthContext.tsx
"use client";

import { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import { Usuario, AuthRequest, ClienteRegistro } from '@/libs/types'; // Importar tipos necesarios
import { loginUser, registerClient } from '@/services/api'; // Importar las funciones de API

interface AuthContextType {
    user: Usuario | null;
    isAuthenticated: boolean;
    loading: boolean;
    login: (credentials: AuthRequest) => Promise<{ success: boolean; error?: string }>;
    logout: () => void;
    register: (registrationData: ClienteRegistro) => Promise<{ success: boolean; error?: string }>;
    hasRole: (role: string) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<Usuario | null>(null);
    const [loading, setLoading] = useState(true);

    // Cargar usuario desde localStorage al inicio
    useEffect(() => {
        const storedUser = localStorage.getItem('greenthumb_user');
        if (storedUser) {
            try {
                setUser(JSON.parse(storedUser));
            } catch (e) {
                console.error("Failed to parse user from localStorage", e);
                localStorage.removeItem('greenthumb_user'); // Limpiar si hay error al parsear
            }
        }
        setLoading(false);
    }, []);

    const login = useCallback(async (credentials: AuthRequest) => {
        setLoading(true);
        try {
            const response = await loginUser(credentials);
            if (response.data) {
                setUser(response.data);
                localStorage.setItem('greenthumb_user', JSON.stringify(response.data));
                return { success: true };
            } else {
                return { success: false, error: response.error || "Login fallido." };
            }
        } catch (err: any) {
            console.error("Login error:", err);
            return { success: false, error: err.message || "Error de conexión." };
        } finally {
            setLoading(false);
        }
    }, []);

    const logout = useCallback(() => {
        setUser(null);
        localStorage.removeItem('greenthumb_user');
        // Podrías redirigir al usuario aquí si lo deseas
        // router.push('/login');
    }, []);

    const register = useCallback(async (registrationData: ClienteRegistro) => {
        setLoading(true);
        try {
            const response = await registerClient(registrationData);
            if (response.data) {
                // Opcional: logear al usuario automáticamente después del registro
                setUser(response.data);
                localStorage.setItem('greenthumb_user', JSON.stringify(response.data));
                return { success: true };
            } else {
                return { success: false, error: response.error || "Registro fallido." };
            }
        } catch (err: any) {
            console.error("Registration error:", err);
            return { success: false, error: err.message || "Error de conexión durante el registro." };
        } finally {
            setLoading(false);
        }
    }, []);

    const hasRole = useCallback((role: string) => {
        return user?.roles.includes(role) || false;
    }, [user]);

    const value = {
        user,
        isAuthenticated: !!user,
        loading,
        login,
        logout,
        register,
        hasRole,
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};