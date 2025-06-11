"use client";

import { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { UsuarioDTO } from '@/libs/types';
import { sincronizarUsuario } from '@/services/api';
import { useRouter } from 'next/navigation'; // Importante: usar useRouter de next/navigation

interface AuthContextType {
    isAuthenticated: boolean;
    isLoading: boolean;
    user: UsuarioDTO | null;
    login: () => void;
    logout: () => void;
    getAccessToken: () => Promise<string | undefined>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const router = useRouter();
    const { 
        isAuthenticated: auth0IsAuthenticated, 
        isLoading: auth0IsLoading, 
        user: auth0User, 
        loginWithRedirect, 
        logout: auth0Logout,
        getAccessTokenSilently 
    } = useAuth0();

    const [localUser, setLocalUser] = useState<UsuarioDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    
    const sincronizar = useCallback(async () => {
        if (auth0IsAuthenticated && auth0User) {
            try {
                console.log("LOG: Usuario autenticado en Auth0. Sincronizando con el backend...");
                const token = await getAccessTokenSilently();
                const syncedUser = await sincronizarUsuario(token);

                if (!syncedUser.registroCompleto) {
                    console.log("LOG: Usuario nuevo. Redirigiendo a /completar-registro...");
                    router.push('/completar-registro');
                } else {
                    setLocalUser(syncedUser);
                    console.log("LOG: Usuario sincronizado exitosamente:", syncedUser);
                }
            } catch (error) {
                console.error("Error al sincronizar el usuario con el backend:", error);
                // Opcional: desloguear al usuario si la sincronización falla
                auth0Logout({ logoutParams: { returnTo: window.location.origin } });
            } finally {
                setIsLoading(false);
            }
        } else if (!auth0IsLoading) {
            setIsLoading(false);
        }
    }, [auth0IsAuthenticated, auth0User, getAccessTokenSilently, router, auth0Logout]);

    useEffect(() => {
        sincronizar();
    }, [sincronizar]);

    const login = () => loginWithRedirect();

    const logout = () => {
        setLocalUser(null);
        auth0Logout({ logoutParams: { returnTo: window.location.origin } });
    };

    const getAccessToken = async (): Promise<string | undefined> => {
        try {
            return await getAccessTokenSilently();
        } catch (error) {
            console.error("No se pudo obtener el token de acceso:", error);
            return undefined;
        }
    };
    
    // El loading final es una combinación del loading de Auth0 y nuestro loading de sincronización
    const finalIsLoading = auth0IsLoading || isLoading;

    return (
        <AuthContext.Provider value={{ isAuthenticated: !!localUser, isLoading: finalIsLoading, user: localUser, login, logout, getAccessToken }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth debe ser usado dentro de un AuthProvider");
    }
    return context;
};