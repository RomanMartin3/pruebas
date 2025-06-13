"use client";

import React, { useState, useEffect, useCallback } from 'react'; // Importar useCallback
// Importar NivelLuz y FrecuenciaRiego
import { ProductoDetalle, Categoria, DetallesPlanta, DetallesHerramienta, DetallesSemilla, NivelLuz, FrecuenciaRiego } from '@/libs/types'; 
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

    // Estados para detalles específicos, inicializados para coincidir con las interfaces
    const [detallesPlanta, setDetallesPlanta] = useState<DetallesPlanta>({ 
        nombreCientifico: '', 
        nivelLuzDescripcion: '', 
        frecuenciaRiegoDescripcion: '', 
        tipoAmbiente: '', 
        esVenenosa: false, 
        cuidadosEspeciales: '' 
    });
    const [detallesHerramienta, setDetallesHerramienta] = useState<DetallesHerramienta>({ 
        materialPrincipal: '', 
        dimensiones: '', 
        pesoKG: 0, 
        usoRecomendado: '', 
        requiereMantenimiento: false 
    });
    const [detallesSemilla, setDetallesSemilla] = useState<DetallesSemilla>({ 
        especieVariedad: '', 
        epocaSiembraIdeal: '', 
        profundidadSiembraCM: 0, 
        tiempoGerminacionDias: '', // String en el DTO de Java
        instruccionesSiembra: '' 
    });

    // Estado para imágenes
    const [imagenes, setImagenes] = useState<FileList | null>(null);

    // NUEVOS ESTADOS para las listas de niveles de luz y frecuencias de riego
    const [availableNivelesLuz, setAvailableNivelesLuz] = useState<NivelLuz[]>([]);
    const [availableFrecuenciasRiego, setAvailableFrecuenciasRiego] = useState<FrecuenciaRiego[]>([]);

    // Estado para cargar datos auxiliares (categorías)
    const [availableCategories, setAvailableCategories] = useState<Categoria[]>([]);

    // --- EFECTOS ---
    // Cargar categorías, niveles de luz y frecuencias de riego
    useEffect(() => {
        const fetchData = async () => {
            try {
                // Cargar categorías
                const { data: cats } = await api.getCategories(); 
                if (cats) setAvailableCategories(cats);

                // Cargar niveles de luz
                const { data: nivelesLuz } = await api.getNivelesLuz();
                if (nivelesLuz) setAvailableNivelesLuz(nivelesLuz);

                // Cargar frecuencias de riego
                const { data: frecuenciasRiego } = await api.getFrecuenciasRiego();
                if (frecuenciasRiego) setAvailableFrecuenciasRiego(frecuenciasRiego);

            } catch (error) {
                console.error("Error al cargar datos auxiliares:", error);
            }
        };
        fetchData();
    }, []); // Se ejecuta solo una vez al montar el componente

    // Llenar el formulario si se está editando
    useEffect(() => {
        if (initialData) {
            setNombre(initialData.nombreProducto || '');
            setDescripcion(initialData.descripcionGeneral || '');
            setStockActual(initialData.stockActual || 0);

            setPrecio(initialData.precioActual?.precioVenta || 0); 
            setCosto(initialData.costoActual?.precioCosto || 0);

            setCategoriaId(String(initialData.categoria?.categoriaId || ''));
            setTipoProductoId(String(initialData.tipoProducto?.tipoProductoId || ''));

            if (initialData.detallesPlanta) {
                setDetallesPlanta({
                    nombreCientifico: initialData.detallesPlanta.nombreCientifico || '',
                    nivelLuzDescripcion: initialData.detallesPlanta.nivelLuzDescripcion || '',
                    frecuenciaRiegoDescripcion: initialData.detallesPlanta.frecuenciaRiegoDescripcion || '',
                    tipoAmbiente: initialData.detallesPlanta.tipoAmbiente || '',
                    esVenenosa: initialData.detallesPlanta.esVenenosa || false,
                    cuidadosEspeciales: initialData.detallesPlanta.cuidadosEspeciales || ''
                });
            } else { 
                setDetallesPlanta({ nombreCientifico: '', nivelLuzDescripcion: '', frecuenciaRiegoDescripcion: '', tipoAmbiente: '', esVenenosa: false, cuidadosEspeciales: '' });
            }

            if (initialData.detallesHerramienta) {
                setDetallesHerramienta({
                    materialPrincipal: initialData.detallesHerramienta.materialPrincipal || '',
                    dimensiones: initialData.detallesHerramienta.dimensiones || '',
                    pesoKG: initialData.detallesHerramienta.pesoKG || 0, 
                    usoRecomendado: initialData.detallesHerramienta.usoRecomendado || '',
                    requiereMantenimiento: initialData.detallesHerramienta.requiereMantenimiento || false
                });
            } else { 
                setDetallesHerramienta({ materialPrincipal: '', dimensiones: '', pesoKG: 0, usoRecomendado: '', requiereMantenimiento: false });
            }

            if (initialData.detallesSemilla) {
                setDetallesSemilla({
                    especieVariedad: initialData.detallesSemilla.especieVariedad || '',
                    epocaSiembraIdeal: initialData.detallesSemilla.epocaSiembraIdeal || '',
                    profundidadSiembraCM: initialData.detallesSemilla.profundidadSiembraCM || 0,
                    tiempoGerminacionDias: initialData.detallesSemilla.tiempoGerminacionDias || '',
                    instruccionesSiembra: initialData.detallesSemilla.instruccionesSiembra || ''
                });
            } else { 
                setDetallesSemilla({ especieVariedad: '', epocaSiembraIdeal: '', profundidadSiembraCM: 0, tiempoGerminacionDias: '', instruccionesSiembra: '' });
            }

        } else {
            setNombre('');
            setDescripcion('');
            setStockActual(0);
            setPrecio(0);
            setCosto(0);
            setCategoriaId('');
            setTipoProductoId('');
            setDetallesPlanta({ nombreCientifico: '', nivelLuzDescripcion: '', frecuenciaRiegoDescripcion: '', tipoAmbiente: '', esVenenosa: false, cuidadosEspeciales: '' });
            setDetallesHerramienta({ materialPrincipal: '', dimensiones: '', pesoKG: 0, usoRecomendado: '', requiereMantenimiento: false });
            setDetallesSemilla({ especieVariedad: '', epocaSiembraIdeal: '', profundidadSiembraCM: 0, tiempoGerminacionDias: '', instruccionesSiembra: '' });
        }
    }, [initialData]);

    // --- MANEJADORES ---
    const handleDetailsChange = useCallback((e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>, type: 'planta' | 'herramienta' | 'semilla') => {
        const { name, value, type: inputType, checked } = e.target as HTMLInputElement;

        switch (type) {
            case 'planta':
                setDetallesPlanta(prev => ({ 
                    ...prev, 
                    [name]: inputType === 'checkbox' ? checked : value 
                }));
                break;
            case 'herramienta':
                setDetallesHerramienta(prev => ({ 
                    ...prev, 
                    // Convertir a número solo si la propiedad es pesoKG
                    [name]: (name === 'pesoKG') ? Number(value) : (inputType === 'checkbox' ? checked : value)
                }));
                break;
            case 'semilla':
                setDetallesSemilla(prev => ({ 
                    ...prev, 
                    // Convertir a número solo si la propiedad es profundidadSiembraCM
                    [name]: (name === 'profundidadSiembraCM') ? Number(value) : (inputType === 'checkbox' ? checked : value) 
                    // tiempoGerminacionDias es string en DTO de Java, no se convierte a Number
                }));
                break;
        }
    }, []); // Dependencias: vacías para que la función sea estable

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const formData = new FormData();

        const productoData = { 
            nombreProducto: nombre,
            descripcionGeneral: descripcion,
            stockActual: stockActual,
            precioVenta: precio,
            costo: costo,
            categoriaId: Number(categoriaId),
            tipoProductoId: Number(tipoProductoId)
        };
        formData.append('producto', new Blob([JSON.stringify(productoData)], { type: 'application/json' }));

        let detallesAEnviar: any = null; 
        if (tipoProductoId === '1') { // Planta
            detallesAEnviar = detallesPlanta; // Usar el estado directamente
            formData.append('detallesPlanta', new Blob([JSON.stringify(detallesAEnviar)], { type: 'application/json' }));
        } else if (tipoProductoId === '2') { // Herramienta
            detallesAEnviar = detallesHerramienta; // Usar el estado directamente
            formData.append('detallesHerramienta', new Blob([JSON.stringify(detallesAEnviar)], { type: 'application/json' }));
        } else if (tipoProductoId === '3') { // Semilla
            detallesAEnviar = detallesSemilla; // Usar el estado directamente
            formData.append('detallesSemilla', new Blob([JSON.stringify(detallesAEnviar)], { type: 'application/json' }));
        }

        if (imagenes && imagenes.length > 0) {
            formData.append('imagen', imagenes[0]);
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
                        
                        {/* CAMBIO: Selector para Nivel de Luz */}
                        <select name="nivelLuzDescripcion" value={detallesPlanta.nivelLuzDescripcion} onChange={e => handleDetailsChange(e, 'planta')} required>
                            <option value="">Seleccione Nivel de Luz</option>
                            {availableNivelesLuz.map(nivel => (
                                <option key={nivel.nivelLuzId} value={nivel.descripcionNivelLuz}>{nivel.descripcionNivelLuz}</option>
                            ))}
                        </select>

                        {/* CAMBIO: Selector para Frecuencia de Riego */}
                        <select name="frecuenciaRiegoDescripcion" value={detallesPlanta.frecuenciaRiegoDescripcion} onChange={e => handleDetailsChange(e, 'planta')} required>
                            <option value="">Seleccione Frecuencia de Riego</option>
                            {availableFrecuenciasRiego.map(frec => (
                                <option key={frec.frecuenciaRiegoId} value={frec.descripcionFrecuenciaRiego}>{frec.descripcionFrecuenciaRiego}</option>
                            ))}
                        </select>

                        <input name="tipoAmbiente" value={detallesPlanta.tipoAmbiente} onChange={e => handleDetailsChange(e, 'planta')} placeholder="Tipo de Ambiente" />
                        <label>Es Venenosa: <input type="checkbox" name="esVenenosa" checked={detallesPlanta.esVenenosa} onChange={e => handleDetailsChange(e, 'planta')} /></label>
                        <textarea name="cuidadosEspeciales" value={detallesPlanta.cuidadosEspeciales} onChange={e => handleDetailsChange(e, 'planta')} placeholder="Cuidados Especiales" rows={2} />
                    </div>
                </fieldset>
            )}
            {tipoProductoId === '2' && (
                <fieldset className={styles.fieldset}>
                    <legend>Detalles de Herramienta</legend>
                    <div className={styles.formGrid}>
                        <input name="materialPrincipal" value={detallesHerramienta.materialPrincipal} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Material Principal" />
                        <input name="dimensiones" value={detallesHerramienta.dimensiones} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Dimensiones" />
                        <input type="number" step="0.01" name="pesoKG" value={detallesHerramienta.pesoKG} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Peso (KG)" />
                        <input name="usoRecomendado" value={detallesHerramienta.usoRecomendado} onChange={e => handleDetailsChange(e, 'herramienta')} placeholder="Uso Recomendado" />
                        <label>Requiere Mantenimiento: <input type="checkbox" name="requiereMantenimiento" checked={detallesHerramienta.requiereMantenimiento} onChange={e => handleDetailsChange(e, 'herramienta')} /></label>
                    </div>
                </fieldset>
            )}
            {tipoProductoId === '3' && (
                <fieldset className={styles.fieldset}>
                    <legend>Detalles de Semilla</legend>
                    <div className={styles.formGrid}>
                        <input name="especieVariedad" value={detallesSemilla.especieVariedad} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Especie/Variedad" />
                        <input name="epocaSiembraIdeal" value={detallesSemilla.epocaSiembraIdeal} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Época de Siembra Ideal" />
                        <input type="number" step="0.01" name="profundidadSiembraCM" value={detallesSemilla.profundidadSiembraCM} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Profundidad de Siembra (CM)" />
                        <input type="text" name="tiempoGerminacionDias" value={detallesSemilla.tiempoGerminacionDias} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Tiempo de Germinación (Días)" />
                        <textarea name="instruccionesSiembra" value={detallesSemilla.instruccionesSiembra} onChange={e => handleDetailsChange(e, 'semilla')} placeholder="Instrucciones de Siembra" rows={2} />
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