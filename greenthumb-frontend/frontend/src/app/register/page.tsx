// src/app/register/page.tsx
"use client";

import { useState, useEffect } from 'react'; // <-- Importa useEffect
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import styles from './Register.module.css';
import Link from 'next/link';

export default function RegisterPage() {
    const [formData, setFormData] = useState({
        nombre: '',
        apellido: '',
        email: '',
        contrasena: '',
        confirmContrasena: '',
        telefono: '',
        calle: '',
        numero: '',
        codigoPostal: '',
        ciudad: '',
        provincia: '',
    });
    const [error, setError] = useState<string | null>(null);
    const { register, loading, isAuthenticated } = useAuth();
    const router = useRouter();

    // <-- SOLUCIÓN: Usar useEffect para la redirección
    useEffect(() => {
        if (isAuthenticated && !loading) { // Asegurarse de que esté autenticado y la carga haya terminado
            router.push('/');
        }
    }, [isAuthenticated, loading, router]); // Dependencias del efecto

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (formData.contrasena !== formData.confirmContrasena) {
            setError("Las contraseñas no coinciden.");
            return;
        }

        const { confirmContrasena, ...dataToRegister } = formData; 

        const result = await register(dataToRegister);

        if (!result.success) { // Si el registro falla, mostramos el error
            setError(result.error || "Error al registrar el usuario.");
        }
        // Si el registro es exitoso, useEffect se encargará de la redirección
    };

    // No necesitamos el `if (isAuthenticated)` directo aquí porque useEffect se encarga
    // Si isAuthenticated es true, useEffect ya habrá redirigido o lo hará pronto.
    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h1 className={styles.title}>Registro de Cliente</h1>
                <form onSubmit={handleSubmit} className={styles.form}>
                    <div className={styles.formGroup}>
                        <label htmlFor="nombre">Nombre</label>
                        <input type="text" id="nombre" name="nombre" value={formData.nombre} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="apellido">Apellido</label>
                        <input type="text" id="apellido" name="apellido" value={formData.apellido} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="email">Email</label>
                        <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="contrasena">Contraseña</label>
                        <input type="password" id="contrasena" name="contrasena" value={formData.contrasena} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="confirmContrasena">Confirmar Contraseña</label>
                        <input type="password" id="confirmContrasena" name="confirmContrasena" value={formData.confirmContrasena} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="telefono">Teléfono</label>
                        <input type="tel" id="telefono" name="telefono" value={formData.telefono} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="calle">Calle</label>
                        <input type="text" id="calle" name="calle" value={formData.calle} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="numero">Número</label>
                        <input type="text" id="numero" name="numero" value={formData.numero} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="ciudad">Ciudad</label>
                        <input type="text" id="ciudad" name="ciudad" value={formData.ciudad} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="provincia">Provincia</label>
                        <input type="text" id="provincia" name="provincia" value={formData.provincia} onChange={handleChange} required className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="codigoPostal">Código Postal</label>
                        <input type="text" id="codigoPostal" name="codigoPostal" value={formData.codigoPostal} onChange={handleChange} required className={styles.input} />
                    </div>

                    {error && <p className={styles.error}>{error}</p>}
                    <button type="submit" disabled={loading} className={styles.button}>
                        {loading ? 'Registrando...' : 'Registrarse'}
                    </button>
                </form>
                <p className={styles.loginPrompt}>
                    ¿Ya tienes una cuenta? <Link href="/login">Inicia sesión aquí</Link>
                </p>
            </div>
        </div>
    );
}