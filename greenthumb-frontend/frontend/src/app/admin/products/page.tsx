"use client";
import Link from 'next/link';
// We'll reuse the same styles and fetch logic from the public products page for now
import styles from '../../products/ProductsPage.module.css';
import { useEffect, useState } from 'react';
import { getProducts } from '@/services/api';
import { ProductoListado } from '@/libs/types';



export default function AdminProductsPage() {
    const [products, setProducts] = useState<ProductoListado[]>([]);

    useEffect(() => {
        getProducts({ size: '100' }).then(res => setProducts(res.content));
    }, []);

    return (
        <div>
            <div className={styles.adminHeader}>
                <h1>Manage Products</h1>
                <Link href="/admin/products/new" className={styles.addButton}>Add New Product</Link>
            </div>

            {/* A simple table to display products */}
            <table className={styles.adminTable}>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(p => (
                        <tr key={p.productoId}>
                            <td>{p.productoId}</td>
                            <td>{p.nombreProducto}</td>
                            <td>{p.categoriaNombre}</td>
                            <td>${p.precioVentaActual.toFixed(2)}</td>
                            <td>{p.stockActual}</td>
                            <td>
                                <Link href={`/admin/products/edit/${p.productoId}`}>Edit</Link>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}