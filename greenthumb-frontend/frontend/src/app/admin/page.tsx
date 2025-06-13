// src/app/admin/page.tsx
"use client";

import React, { useEffect, useState } from 'react';
import * as api from '@/services/api';
import { DashboardDTO, ApiResponse } from '@/libs/types'; // Importar ApiResponse
import styles from './AdminDashboard.module.css';
import DashboardCard from '@/components/DashboardCard';
import AdminNavCard from '@/components/AdminNavCard';

const AdminDashboardPage: React.FC = () => {
    const [data, setData] = useState<DashboardDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setIsLoading(true);
                // La función getDashboardData devuelve un Promise<ApiResponse<DashboardDTO>>
                const response: ApiResponse<DashboardDTO> = await api.getDashboardData(); //

                // --- CORRECCIÓN CLAVE AQUÍ ---
                // Verificamos que response.data exista.
                if (response.data) { //
                    setData(response.data); // ✅ Acceso correcto: response.data
                    setError(null);
                } else {
                    // Si no hay datos, establecemos el estado a null y un mensaje de error.
                    setData(null);
                    setError(response.error || 'No se recibieron datos del dashboard o el formato fue inesperado.'); //
                    console.error("Respuesta inesperada de la API:", response);
                }
            } catch (err: any) {
                setError(err.message || 'Error al cargar los datos del dashboard.');
                console.error("Error al cargar datos del dashboard:", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchData();
    }, []);

    if (isLoading) return <div className={styles.centered}>Cargando panel de control...</div>;
    if (error) return <div className={`${styles.centered} ${styles.error || ''}`} style={!styles.error ? { color: "red" } : {}}>{error}</div>;

    // --- CORRECCIÓN CLAVE AQUÍ (Comprobación defensiva antes de renderizar) ---
    // Si 'data' es null en este punto (por ejemplo, si la carga falló y no hay error),
    // podemos decidir qué mostrar. En este caso, ya tenemos un 'if (error)' arriba,
    // por lo que si llegamos aquí y 'data' es null, podría ser un caso no manejado
    // o simplemente que no hay datos disponibles (aunque un dashboard siempre debería tenerlos).
    // Si la API puede devolver un dashboard sin top5ProductosMasVendidos, considera
    // usar el operador de encadenamiento opcional `?.` o un valor por defecto.
    if (!data) {
        return <div className={styles.centered}>No hay datos disponibles para el panel de control.</div>;
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Panel de Administración</h1>
            <p className={styles.subtitle}>Selecciona una sección para gestionar o revisa las métricas de la tienda.</p>
            
            <div className={styles.navigationSection}>
                <h2 className={styles.sectionTitle}>Acciones Principales</h2>
                <div className={styles.cardsGrid}>
                    <AdminNavCard
                        title="Gestionar Productos"
                        description="Añadir, editar y eliminar productos del inventario."
                        href="/admin/products"
                    />
                    <AdminNavCard
                        title="Gestionar Categorías"
                        description="Crear, editar y eliminar categorías de productos."
                        href="/admin/categories"
                    />
                </div>
            </div>

            {/* Este bloque solo se renderizará si 'data' no es null */}
            <>
                <div className={styles.topProducts}>
                    <h2 className={styles.sectionTitle}>Métricas de la Tienda</h2>
                    <div className={styles.cardsGrid}>
                        <DashboardCard title="Total de Productos" value={data.totalProductos} icon="🌱" />
                        <DashboardCard title="Usuarios Activos" value={data.totalUsuariosActivos} icon="👤" />
                        <DashboardCard title="Total de Pedidos" value={data.totalPedidos} icon="📦" />
                    </div>
                </div>

                <div className={styles.topProducts}>
                    <h2>🏆 Productos Más Vendidos</h2>
                    <ul>
                        {/* ✅ Aquí usamos el operador de encadenamiento opcional '?'
                             y el operador de coalescencia nula '?? []' para asegurar
                             que .map siempre se llame en un array, incluso si
                             top5ProductosMasVendidos es undefined o null. */}
                        {data.top5ProductosMasVendidos?.map((producto) => ( //
                            <li key={producto.productoId} className={styles.listItem}>
                                <span className={styles.productName}>{producto.nombreProducto}</span>
                                <span className={styles.productSales}>{producto.cantidadVendida} unidades</span>
                            </li>
                        )) ?? <p>No hay productos más vendidos para mostrar.</p>} {/* Mensaje si está vacío */}
                    </ul>
                </div>
            </>
        </div>
    );
};

export default AdminDashboardPage;