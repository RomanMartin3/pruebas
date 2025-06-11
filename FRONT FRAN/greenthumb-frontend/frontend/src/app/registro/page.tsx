"use client";

import React, { useState, useEffect, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { registrarNuevoCliente } from '@/services/api';
import { ClienteRegistroDTO } from '@/libs/types';
import styles from './RegistroPage.module.css'; // Crearemos este archivo de estilos

const CompletarRegistroPage = () => {
  const { user, getAccessToken } = useAuth();
  const router = useRouter();

  const [formData, setFormData] = useState<Omit<ClienteRegistroDTO, 'auth0Id'>>({
    email: '',
    nombre: '',
    apellido: '',
    telefono: '',
    calle: '',
    numero: '',
    ciudad: '',
    provincia: '',
    codigoPostal: '',
  });

  // Pre-rellenamos el formulario con los datos que ya tenemos de Auth0
  useEffect(() => {
    if (user) {
      setFormData(prev => ({
        ...prev,
        email: user.email,
        nombre: user.nombre,
        apellido: user.apellido,
      }));
    }
  }, [user]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const token = await getAccessToken();
    if (!token) {
      alert("Error de autenticación. Por favor, intenta iniciar sesión de nuevo.");
      return;
    }

    try {
      console.log("LOG: Enviando datos de registro al backend:", formData);
      await registrarNuevoCliente(formData, token);
      alert("¡Registro completado con éxito! Serás redirigido a la página principal.");
      // Forzamos un refresh para que el AuthContext se re-sincronice
      window.location.href = '/'; 
    } catch (error) {
      console.error("Error al completar el registro:", error);
      alert("Hubo un error al guardar tus datos. Por favor, inténtalo de nuevo.");
    }
  };

  if (!user) {
    return <div>Cargando datos de usuario...</div>;
  }

  return (
    <div className={styles.container}>
      <h1>Completar Registro</h1>
      <p>
        ¡Bienvenido a GreenThumb! Como es tu primera vez, necesitamos algunos
        datos adicionales para crear tu perfil.
      </p>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formGroup}>
          <label htmlFor="nombre">Nombre</label>
          <input id="nombre" name="nombre" value={formData.nombre} onChange={handleChange} required disabled />
        </div>
        <div className={styles.formGroup}>
            <label htmlFor="apellido">Apellido</label>
            <input id="apellido" name="apellido" value={formData.apellido} onChange={handleChange} required disabled />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="email">Email</label>
          <input id="email" type="email" name="email" value={formData.email} onChange={handleChange} required disabled />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="telefono">Teléfono</label>
          <input id="telefono" type="tel" name="telefono" value={formData.telefono} onChange={handleChange} placeholder="Ej: 2611234567" required />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="calle">Calle</label>
          <input id="calle" name="calle" value={formData.calle} onChange={handleChange} required />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="numero">Número / Dpto</label>
          <input id="numero" name="numero" value={formData.numero} onChange={handleChange} required />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="codigoPostal">Código Postal</label>
          <input id="codigoPostal" name="codigoPostal" value={formData.codigoPostal} onChange={handleChange} required />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="ciudad">Ciudad</label>
          <input id="ciudad" name="ciudad" value={formData.ciudad} onChange={handleChange} required />
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="provincia">Provincia</label>
          <input id="provincia" name="provincia" value={formData.provincia} onChange={handleChange} required />
        </div>

        <button type="submit" className={styles.submitButton}>Completar Registro</button>
      </form>
    </div>
  );
};

export default CompletarRegistroPage;