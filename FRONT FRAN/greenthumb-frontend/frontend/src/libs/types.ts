export interface Categoria {
    categoriaId: number;
    nombreCategoria: string;
    descripcionCategoria: string;
}

export interface ImagenProducto {
    id: number;
    url: string;
    principal: boolean;
}

export interface ProductoListado {
    productoId: number;
    nombreProducto: string;
    categoriaNombre: string;
    tipoProductoNombre: string;
    precioVentaActual: number;
    imagenUrlPrincipal: string | null;
    stockActual: number;
}

export interface DetallesPlanta {
    id: number;
    nombreCientifico: string;
    nivelLuz: string;
    frecuenciaRiego: string;
}

export interface DetallesSemilla {
    id: number;
    tiempoDeGerminacion: string;
    profundidadDeSiembra: string;
    espaciado: string;
}

export interface DetallesHerramienta {
    id: number;
    material: string;
    dimensione: string;
    peso: string;
}

export interface ProductoDetalle {
    productoId: number;
    nombre: string;
    descripcion: string;
    stockActual: number;
    categoriaId: number;
    tipoProductoId: number;
    precio: number;
    costo: number;
    imagenes: ImagenProducto[];
    detallesPlanta?: DetallesPlanta;
    detallesSemilla?: DetallesSemilla;
    detallesHerramienta?: DetallesHerramienta;
}

export interface CarritoItem {
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number; 
    imagenUrl: string | null;
}

export interface CheckoutRequest {
    metodoPago: string;
    notasCliente?: string;
}

export interface Pedido {
    pedidoId: number;
    fechaPedido: string; 
    total: number;
    estadoActual: string;
    metodoPago: string;
    notasCliente: string;
    detalles: DetallePedido[];
}

export interface DetallePedido {
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
}

export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number; 
}

export interface UsuarioDTO {
    id: number | null;
    email: string;
    nombre: string;
    apellido: string;
    rol: string;
    registroCompleto: boolean;
}


export interface ClienteRegistroDTO {
    email: string;
    nombre: string;
    apellido: string;
    telefono: string;
    calle: string;
    numero: string;
    ciudad: string;
    provincia: string;
    codigoPostal: string;
}


export interface ProductoMasVendidoDTO {
    nombreProducto: string;
    cantidadTotalVendida: number;
}


export interface DashboardDTO {
    totalVentas: number;
    cantidadPedidos: number;
    ticketPromedio: number;
    productosMasVendidos: ProductoMasVendidoDTO[];
}
