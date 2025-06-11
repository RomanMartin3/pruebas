"use client";

import React, { useEffect, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import * as api from '@/services/api';
import { DashboardDTO } from '@/libs/types';
import styles from './AdminDashboard.module.css';
import DashboardCard from '@/components/DashboardCard';

const AdminDashboardPage: React.FC = () => {
    const { user, getAccessToken } = useAuth();
    const [data, setData] = useState<DashboardDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = await getAccessToken();
                if (!token) {
                    throw new Error("Token de autenticación no disponible.");
                }
                const dashboardData = await api.getDashboardData(token);
                setData(dashboardData);
            } catch (err: any) {
                setError(err.message || 'Error al cargar los datos del dashboard.');
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [getAccessToken]);

    const formatCurrency = (value: number) => {
        return new Intl.NumberFormat('es-AR', {
            style: 'currency',
            currency: 'ARS'
        }).format(value);
    };

    if (isLoading) {
        return <div className={styles.centered}>Cargando panel de control...</div>;
    }

    if (error) {
        return <div className={`${styles.centered} ${styles.error}`}>{error}</div>;
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Bienvenido, {user?.nombre || 'Admin'}!</h1>
            <p className={styles.subtitle}>Aquí tienes un resumen de la actividad de tu tienda.</p>

            {data && (
                <>
                    <div className={styles.cardsGrid}>
                        <DashboardCard 
                            title="Total de Ventas" 
                            value={formatCurrency(data.totalVentas)} 
                            icon="💰" 
                        />
                        <DashboardCard 
                            title="Cantidad de Pedidos" 
                            value={data.cantidadPedidos} 
                            icon="📦" 
                        />
                        <DashboardCard 
                            title="Ticket Promedio" 
                            value={formatCurrency(data.ticketPromedio)} 
                            icon="💸" 
                        />
                    </div>

                    <div className={styles.topProducts}>
                        <h2>🏆 Productos Más Vendidos</h2>
                        <ul>
                            {data.productosMasVendidos.map((producto, index) => (
                                <li key={index}>
                                    <span className={styles.productName}>{producto.nombreProducto}</span>
                                    <span className={styles.productSales}>{producto.cantidadTotalVendida} unidades</span>
                                </li>
                            ))}
                        </ul>
                    </div>
                </>
            )}
        </div>
    );
};

export default AdminDashboardPage;