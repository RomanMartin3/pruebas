"use client";

import { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { UsuarioDTO } from '@/libs/types';
import { sincronizarUsuario } from '@/services/api';
import { useRouter } from 'next/navigation';

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
    // Este estado nos ayuda a saber si estamos en nuestro proceso de sync
    const [isSyncing, setIsSyncing] = useState(true); 
    
    const sincronizar = useCallback(async () => {
        // Solo intentamos sincronizar si Auth0 terminó de cargar y el usuario está autenticado
        if (!auth0IsLoading && auth0IsAuthenticated && auth0User) {
            setIsSyncing(true); // Empezamos nuestro proceso de sync
            try {
                console.log("LOG: Usuario autenticado en Auth0. Sincronizando con el backend...");
                const token = await getAccessTokenSilently();
                const syncedUser = await sincronizarUsuario(token);

                setLocalUser(syncedUser);
                console.log("LOG: Usuario sincronizado exitosamente:", syncedUser);
                
                // La redirección se comprueba después de establecer el usuario
                if (!syncedUser.registroCompleto) {
                    console.log("LOG: Usuario nuevo. Redirigiendo a /registro...");
                    router.push('/registro'); // CORREGIDO: la ruta correcta es /registro
                }
            } catch (error) {
                console.error("Error al sincronizar el usuario con el backend:", error);
                // Si falla la sincronización, deslogueamos para evitar un estado inconsistente
                auth0Logout({ logoutParams: { returnTo: window.location.origin } });
            } finally {
                setIsSyncing(false); // Terminamos nuestro proceso de sync
            }
        } else if (!auth0IsLoading) {
            // Si Auth0 no está cargando y no hay usuario, terminamos el proceso.
            setIsSyncing(false);
            setLocalUser(null);
        }
    }, [auth0IsAuthenticated, auth0User, auth0IsLoading, getAccessTokenSilently, router, auth0Logout]);

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
    
    // El loading final es verdadero si Auth0 está cargando O si nosotros estamos sincronizando.
    const finalIsLoading = auth0IsLoading || isSyncing;

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