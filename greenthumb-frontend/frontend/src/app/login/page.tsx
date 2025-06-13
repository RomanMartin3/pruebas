// src/app/login/page.tsx
"use client";

import { useState, useEffect } from 'react'; // <-- Importa useEffect
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import styles from './Login.module.css';
import Link from 'next/link';

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const { login, loading, isAuthenticated } = useAuth();
    const router = useRouter();

    // <-- SOLUCIÓN: Usar useEffect para la redirección
    useEffect(() => {
        if (isAuthenticated && !loading) { // Asegurarse de que esté autenticado y la carga haya terminado
            router.push('/');
        }
    }, [isAuthenticated, loading, router]); // Dependencias del efecto

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        const result = await login({ email, contrasena: password });

        if (!result.success) { // Si el login falla, mostramos el error
            setError(result.error || "Email o contraseña incorrectos.");
        }
        // Si el login es exitoso, useEffect se encargará de la redirección
    };

    // No necesitamos el `if (isAuthenticated)` directo aquí porque useEffect se encarga
    // Si isAuthenticated es true, useEffect ya habrá redirigido o lo hará pronto.
    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h1 className={styles.title}>Iniciar Sesión</h1>
                <form onSubmit={handleSubmit} className={styles.form}>
                    <div className={styles.formGroup}>
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className={styles.input}
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Contraseña</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className={styles.input}
                        />
                    </div>
                    {error && <p className={styles.error}>{error}</p>}
                    <button type="submit" disabled={loading} className={styles.button}>
                        {loading ? 'Cargando...' : 'Entrar'}
                    </button>
                </form>
                <p className={styles.registerPrompt}>
                    ¿No tienes una cuenta? <Link href="/register">Regístrate aquí</Link>
                </p>
            </div>
        </div>
    );
}