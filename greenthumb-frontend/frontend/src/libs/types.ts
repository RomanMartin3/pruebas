
/**
 * Represents a product category.
 * Corresponds to: CategoriaDTO.java
 */
export interface Categoria {
    categoriaId: number;
    nombreCategoria: string;
    descripcionCategoria: string;
}

/**
 * Represents a product image.
 * Corresponds to: ImagenProductoDTO.java
 */
export interface ImagenProducto {
    id: number;
    url: string;
    principal: boolean;
}

/**
 * Represents a product in a list view.
 * Corresponds to: ProductoListadoDTO.java
 */
export interface ProductoListado {
    productoId: number;
    nombreProducto: string;
    categoriaNombre: string;
    tipoProductoNombre: string;
    precioVentaActual: number;
    imagenUrlPrincipal: string | null;
    stockActual: number;
}

/**
 * Detailed information for a Plant product.
 * Corresponds to: DetallesPlantaDTO.java
 */
export interface DetallesPlanta {
    id: number;
    nombreCientifico: string;
    nivelLuz: string;
    frecuenciaRiego: string;
}

/**
 * Detailed information for a Seed product.
 * Corresponds to: DetallesSemillaDTO.java
 */
export interface DetallesSemilla {
    id: number;
    tiempoDeGerminacion: string;
    profundidadDeSiembra: string;
    espaciado: string;
}

/**
 * Detailed information for a Tool product.
 * Corresponds to: DetallesHerramientaDTO.java
 */
export interface DetallesHerramienta {
    id: number;
    material: string;
    dimensiones: string;
    peso: string;
}

/**
 * Represents the full details of a single product.
 * Corresponds to: ProductoDetalleDTO.java
 */
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

/**
 * Represents an item in the shopping cart.
 * Corresponds to: CarritoItemDTO.java
 */
export interface CarritoItem {
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number; // El precio por unidad que envía el backend
    imagenUrl: string | null;
    // Se eliminó el campo 'subtotal' porque no viene de la API
}

/**
 * Represents the shopping cart checkout request body.
 */
export interface CheckoutRequest {
    metodoPago: string;
    notasCliente?: string;
}

/**
 * Represents a created order.
 * Corresponds to: PedidoDTO.java
 */
export interface Pedido {
    pedidoId: number;
    fechaPedido: string; // ISO date string
    total: number;
    estadoActual: string;
    metodoPago: string;
    notasCliente: string;
    detalles: DetallePedido[];
}

/**
 * Represents a single item within an order.
 * Corresponds to: DetallePedidoDTO.java
 */
export interface DetallePedido {
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
}

/**
 * Represents the API response for paginated results.
 */
export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number; // current page number
}
/**
 * Represents a user.
 * Corresponds to: UsuarioDTO.java
 */
export interface UsuarioDTO {
  id: number | null;
  email: string;
  nombre: string;
  apellido: string;
  auth0Id: string;
  roles: string[];
  registroCompleto: boolean;
}
/**
 * Represents a user registration request body.
 * Corresponds to: ClienteRegistroDTO.java
 */
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