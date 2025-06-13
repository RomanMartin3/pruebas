// src/libs/types.ts

// Interfaces para Categorías y Tipos de Producto
export interface Categoria {
    categoriaId: number;
    nombreCategoria: string;
    descripcionCategoria: string;
}

export interface TipoProducto {
    tipoProductoId: number;
    nombreTipoProducto: string;
}

// Interfaces para el Dashboard
export interface ProductoMasVendidoDTO {
    productoId: number;
    nombreProducto: string;
    cantidadVendida: number;
}

export interface DashboardDTO {
    totalProductos: number;
    totalUsuariosActivos: number;
    totalPedidos: number;
    top5ProductosMasVendidos: ProductoMasVendidoDTO[];
}

// Interfaces para Productos
export interface ImagenProducto {
    imagenId: number;
    urlImagen: string;
    textoAlternativo: string;
}

export interface ProductoListado {
    productoId: number;
    nombreProducto: string;
    categoriaNombre: string;
    precioVentaActual: number;
    imagenUrlPrincipal: string | null;
    stockActual: number;
}

// --- NUEVAS INTERFACES PARA PRECIO Y COSTO ACTUAL ---
// Coinciden con PrecioProductoActualDTO.java y CostoProductoActualDTO.java
export interface PrecioProductoActual {
    precioVenta: number;
    fechaInicioVigencia: string; // Asumiendo que LocalDateTime se mapea a string en JSON
}

export interface CostoProductoActual {
    precioCosto: number;
    fechaInicioVigencia: string; // Asumiendo que LocalDateTime se mapea a string en JSON
}


// --- DETALLES DE PRODUCTO (ACTUALIZADOS PARA COINCIDIR CON DTOS DE JAVA) ---
export interface DetallesPlanta {
    nombreCientifico: string;
    nivelLuzDescripcion: string; // Corregido para coincidir con DetallesPlantaDTO.java
    frecuenciaRiegoDescripcion: string; // Corregido para coincidir con DetallesPlantaDTO.java
    esVenenosa: boolean;
    cuidadosEspeciales: string;
    tipoAmbiente: string;
}

export interface DetallesHerramienta {
    materialPrincipal: string;
    dimensiones: string;
    pesoKG: number; // Correcto: BigDecimal en Java se mapea a number en TypeScript
    usoRecomendado: string;
    requiereMantenimiento: boolean;
}

export interface DetallesSemilla {
    especieVariedad: string;
    tiempoGerminacionDias: string; // Coincide con String en DetallesSemilla.java
    profundidadSiembraCM: number; // Correcto: BigDecimal en Java se mapea a number en TypeScript
    epocaSiembraIdeal: string;
    instruccionesSiembra: string;
}

// --- PRODUCTO DETALLE UNIFICADO (ACTUALIZADO) ---
// Ahora incluye las interfaces PrecioProductoActual y CostoProductoActual
export interface ProductoDetalle {
    productoId: number;
    nombreProducto: string;
    descripcionGeneral: string;
    stockActual: number;
    puntoDeReorden: number;
    categoria: Categoria;
    tipoProducto: TipoProducto;
    precioActual?: PrecioProductoActual; // <--- Correcto, opcional
    costoActual?: CostoProductoActual;    // <--- Correcto, opcional
    imagenes: ImagenProducto[];
    // Detalles específicos del tipo de producto
    detallesPlanta?: DetallesPlanta;
    detallesHerramienta?: DetallesHerramienta;
    detallesSemilla?: DetallesSemilla;
}


// Interfaces para el Carrito y Pedidos
export interface CarritoItem {
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
    imagenProductoUrl: string | null;
}

export interface CheckoutRequest {
    metodoPago: string;
    notasCliente?: string;
    clienteId: number;
}

export interface Pedido {
    pedidoId: number;
    fechaPedido: string;
    estadoActual: string;
    // ... otros campos del pedido si son necesarios
}

// Interfaz genérica para paginación de la API
export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
}

// --- Interfaz de respuesta de la API (para fetchAPI) ---
// Esta interfaz debe ser consistente con lo que fetchAPI devuelve
export interface ApiResponse<T> {
    data?: T;
    error?: string; // Para mensajes de error del backend
    status?: number; // Para el código de estado HTTP
}

// --- NUEVAS INTERFACES PARA NIVELES DE LUZ Y FRECUENCIAS DE RIEGO ---
export interface NivelLuz {
    nivelLuzId: number;
    descripcionNivelLuz: string;
}

export interface FrecuenciaRiego {
    frecuenciaRiegoId: number;
    descripcionFrecuenciaRiego: string;
}

// --- INTERFACES AÑADIDAS PARA EL LOGIN Y REGISTRO ---

// Interfaz para el usuario autenticado (UsuarioDTO del backend)
export interface Usuario {
    id: number;
    email: string;
    nombre: string;
    apellido: string;
    auth0Id?: string; // Opcional, ya que puede ser el ID interno si no usas Auth0
    roles: string[]; // Por ejemplo, ['CLIENTE'], ['ADMIN']
    registroCompleto: boolean; // Flag para indicar si el perfil está completo
}

// Interfaz para la solicitud de login (AuthRequestDTO del backend)
export interface AuthRequest {
    email: string;
    contrasena: string;
}

// Interfaz para la solicitud de registro de cliente (ClienteRegistroDTO del backend)
export interface ClienteRegistro {
    nombre: string;
    apellido: string;
    email: string;
    contrasena: string; // Añadido: la contraseña es parte del registro
    telefono: string;
    calle: string;
    numero: string;
    codigoPostal: string;
    ciudad: string;
    provincia: string;
    // auth0Id?: string; // Opcional, si Auth0 es usado y este campo se genera en el frontend o es opcional
}