"use client";

import React, { useState, useEffect } from 'react';
import { ProductoDetalle, Categoria } from '@/libs/types';
import * as api from '@/services/api';
import styles from './ProductForm.module.css';

interface ProductFormProps {
    initialData?: ProductoDetalle | null;
    onSubmit: (formData: FormData) => void;
    onCancel: () => void;
    isSubmitting: boolean;
}

const ProductForm: React.FC<ProductFormProps> = ({ initialData, onSubmit, onCancel, isSubmitting }) => {
    // --- ESTADOS ---
    const [nombre, setNombre] = useState('');
    const [descripcion, setDescripcion] = useState('');
    const [stockActual, setStockActual] = useState(0);
    const [precio, setPrecio] = useState(0);
    const [costo, setCosto] = useState(0);
    const [categoriaId, setCategoriaId] = useState('');
    const [tipoProductoId, setTipoProductoId] = useState('');
    
    // Estados para detalles específicos
    const [detallesPlanta, setDetallesPlanta] = useState({ nombreCientifico: '', nivelLuz: '', frecuenciaRiego: '' });
    // CORREGIDO: 'dimensiones' a 'dimensione' para coincidir con el tipo.
    const [detallesHerramienta, setDetallesHerramienta] = useState({ material: '', dimensione: '', peso: '' });
    const [detallesSemilla, setDetallesSemilla] = useState({ tiempoDeGerminacion: '', profundidadDeSiembra: '', espaciado: '' });
    
    // Estado para imágenes
    const [imagenes, setImagenes] = useState<FileList | null>(null);

    // Estado para cargar datos auxiliares (categorías)
    const [availableCategories, setAvailableCategories] = useState<Categoria[]>([]);

    // --- EFECTOS ---
    // Cargar categorías para el dropdown
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const cats = await api.getCategories();
                setAvailableCategories(cats);
            } catch (error) {
                console.error("Error al cargar categorías", error);
            }
        };
        fetchCategories();
    }, []);

    // Llenar el formulario si se está editando
    useEffect(() => {
        if (initialData) {
            setNombre(initialData.nombre);
            setDescripcion(initialData.descripcion);
            setStockActual(initialData.stockActual);
            setPrecio(initialData.precio);
            setCosto(initialData.costo);
            setCategoriaId(String(initialData.categoriaId));
            setTipoProductoId(String(initialData.tipoProductoId));

            if (initialData.detallesPlanta) setDetallesPlanta(initialData.detallesPlanta);
            if (initialData.detallesHerramienta) setDetallesHerramienta(initialData.detallesHerramienta);
            if (initialData.detallesSemilla) setDetallesSemilla(initialData.detallesSemilla);
        } else {
            // Resetear el formulario para creación
            setNombre('');
            setDescripcion('');
            setStockActual(0);
            setPrecio(0);
            setCosto(0);
            setCategoriaId('');
            setTipoProductoId('');
        }
    }, [initialData]);

    // --- MANEJADORES ---
    const handleDetailsChange = (e: React.ChangeEvent<HTMLInputElement>, type: 'planta' | 'herramienta' | 'semilla') => {
        const { name, value } = e.target;
        switch (type) {
            case 'planta':
                setDetallesPlanta(prev => ({ ...prev, [name]: value }));
                break;
            case 'herramienta':
                setDetallesHerramienta(prev => ({ ...prev, [name]: value }));
                break;
            case 'semilla':
                setDetallesSemilla(prev => ({ ...prev, [name]: value }));
                break;
        }
    };
    
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const formData = new FormData();

        const productoData = { nombre, descripcion, stockActual, precio, costo, categoriaId: Number(categoriaId), tipoProductoId: Number(tipoProductoId) };
        formData.append('productoJson', new Blob([JSON.stringify(productoData)], { type: 'application/json' }));

        if (tipoProductoId === '1') {
            formData.append('detallesJson', new Blob([JSON.stringify(detallesPlanta)], { type: 'application/json' }));
        } else if (tipoProductoId === '2') {
            formData.append('detallesJson', new Blob([JSON.stringify(detallesHerramienta)], { type: 'application/json' }));
        } else if (tipoProductoId === '3') {
            formData.append('detallesJson', new Blob([JSON.stringify(detallesSemilla)], { type: 'application/json' }));
        }
        
        if (imagenes) {
            for (let i = 0; i < imagenes.length; i++) {
                formData.append('imagenes', imagenes[i]);
            }
        }

        onSubmit(formData);
    };
    
    const isEditing = !!initialData;

    // --- RENDERIZADO ---
    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            {/* Campos Comunes */}
            <div className={styles.formGrid}>
                <div className={styles.formGroup}>
                    <label>Nombre</label>
                    <input type="text" value={nombre} onChange={e => setNombre(e.target.value)} required />
                </div>
                <div className={styles.formGroup}>
                    <label>Categoría</label>
                    <select value={categoriaId} onChange={e => setCategoriaId(e.target.value)} required>
                        <option value="">Seleccione una categoría</option>
                        {availableCategories.map(cat => (
                            <option key={cat.categoriaId} value={cat.categoriaId}>{cat.nombreCategoria}</option>
                        ))}
                    </select>
                </div>
                <div className={styles.formGroup}>
                    <label>Precio</label>
                    <input type="number" step="0.01" value={precio} onChange={e => setPrecio(Number(e.target.value))} required />
                </div>
                <div className={styles.formGroup}>
                    <label>Costo</label>
                    <input type="number" step="0.01" value={costo} onChange={e => setCosto(Number(e.target.value))} required />
                </div>
                <div className={styles.formGroup}>
                    <label>Stock</label>
                    <input type="number" value={stockActual} onChange={e => setStockActual(Number(e.target.value))} required />
                </div>
                 <div className={styles.formGroup}>
                    <label>Tipo de Producto</label>
                    <select value={tipoProductoId} onChange={e => setTipoProductoId(e.target.value)} required disabled={isEditing}>
                        <option value="">Seleccione un tipo</option>
                        <option value="1">Planta</option>
                        <option value="2">Herramienta</option>
                        <option value="3">Semilla</option>
                    </select>
                </div>
            </div>
             <div className={styles.formGroup}>
                <label>Descripción</label>
                <textarea value={descripcion} onChange={e => setDescripcion(e.target.value)} rows={4} required />
            </div>

            {/* Campos Condicionales */}
            {tipoProductoId === '1' && (
                <fieldset className={styles.fieldset}>
                    <legend>Detalles de Planta</legend>
                    <div className={styles.formGrid}>
                       <input name="nombreCientifico" value={detallesPlanta.nombreCientifico} onChange={e => handleDetailsChange(e, 'planta')} placeholder="Nombre Científico" />
                       <input name="nivelLuz" value={detallesPlanta.nivelLuz} onChange={e => handleDetailsChange(e, 'planta')} placeholder="Nivel de Luz" />
                       <input name="frecuenciaRiego" value={detallesPlanta.frecuenciaRiego} onChange={e => handleDetailsChange(e, 'planta')} placeholder="Frecuencia de Riego" />
                    </div>
                </fieldset>
            )}
            {tipoProductoId === '2' && (
                 <fieldset className={styles.fieldset}>
                    <legend>Detalles de Herramienta</legend>
                    <div className={styles.formGrid}>
                        <input name="material" value={detallesHerramienta.material} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Material" />
                        {/* CORREGIDO: 'dimensiones' a 'dimensione' en el name y value */}
                        <input name="dimensione" value={detallesHerramienta.dimensione} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Dimensiones" />
                        <input name="peso" value={detallesHerramienta.peso} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Peso" />
                    </div>
                </fieldset>
            )}
             {tipoProductoId === '3' && (
                <fieldset className={styles.fieldset}>
                    <legend>Detalles de Semilla</legend>
                    <div className={styles.formGrid}>
                        <input name="tiempoDeGerminacion" value={detallesSemilla.tiempoDeGerminacion} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Tiempo de Germinación" />
                        <input name="profundidadDeSiembra" value={detallesSemilla.profundidadDeSiembra} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Profundidad de Siembra" />
                        <input name="espaciado" value={detallesSemilla.espaciado} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Espaciado" />
                    </div>
                </fieldset>
            )}

            {/* Campo para subir imágenes */}
            <div className={styles.formGroup}>
                <label>Imágenes {isEditing && "(Opcional: solo si desea reemplazar las existentes)"}</label>
                <input type="file" multiple onChange={e => setImagenes(e.target.files)} />
            </div>

            {/* Botones */}
            <div className={styles.buttonContainer}>
                 <button type="button" onClick={onCancel} className={styles.cancelButton}>Cancelar</button>
                 <button type="submit" className={styles.submitButton} disabled={isSubmitting}>
                     {isSubmitting ? 'Guardando...' : (isEditing ? 'Guardar Cambios' : 'Crear Producto')}
                 </button>
            </div>
        </form>
    );
};

export default ProductForm;