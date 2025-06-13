import { CarritoItem, Categoria, CheckoutRequest, Page, Pedido, ProductoDetalle, ProductoListado, DashboardDTO, TipoProducto, ApiResponse, NivelLuz, FrecuenciaRiego, Usuario, AuthRequest, ClienteRegistro} from "@/libs/types"; // Importar nuevos tipos para auth

const API_BASE_URL = "http://localhost:8080";
const API_PREFIX = "/api";

// --- HELPER PARA OBTENER ID DE CLIENTE ANÓNIMO ---
const getAnonymousClientId = (): string => {
    let clientId = localStorage.getItem('greenthumb_anonymous_id');
    if (!clientId) {
        // En un entorno de producción, esto debería ser un ID de cliente anónimo real o un sistema de sesión.
        // Para la presentación, estamos usando un ID fijo.
        clientId = "3"; // Usamos el cliente con ID 3 "Ana Gomez" de tu SQL por defecto para carritos anónimos
        localStorage.setItem('greenthumb_anonymous_id', clientId);
    }
    return clientId;
};

// --- FUNCIÓN FETCH REFACTORIZADA Y MEJORADA ---
// El tipo de retorno ahora es explícitamente Promise<ApiResponse<T>>
async function fetchAPI<T>(endpoint: string, options: RequestInit = {}): Promise<ApiResponse<T>> {
    const url = `${API_BASE_URL}${API_PREFIX}${endpoint}`;

    const headers = new Headers(options.headers);

    // Si el body no es FormData, asegúrate de que el Content-Type sea application/json
    if (!(options.body instanceof FormData)) {
        if (!headers.has('Content-Type')) {
            headers.append('Content-Type', 'application/json');
        }
    }
    // Si el body es un objeto y no FormData/Blob, lo convertimos a JSON Blob
    if (options.body && !(options.body instanceof FormData) && !(options.body instanceof Blob) && typeof options.body === 'object') {
        options.body = new Blob([JSON.stringify(options.body)], { type: 'application/json' });
    }

    const config: RequestInit = { ...options, headers };

    try {
        const res = await fetch(url, config);

        if (!res.ok) {
            let errorBody: any;
            const contentType = res.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                try {
                    errorBody = await res.json();
                } catch (e: any) {
                    errorBody = { message: await res.text() || `Error al parsear la respuesta de error JSON: ${e.message}` };
                }
            } else {
                errorBody = { message: await res.text() || res.statusText || `Error de conexión: ${res.status}` };
            }
            
            console.error(`Error en la petición a ${endpoint}: ${res.status}`, errorBody);
            // Intenta extraer el error de 'error' o 'message' o usa un mensaje genérico
            const errorMessage = typeof errorBody === 'object' && errorBody.error ? errorBody.error : (typeof errorBody.message === 'string' && errorBody.message.length > 0 ? errorBody.message : `Error en la petición: ${res.statusText}`);
            return { error: errorMessage, status: res.status };
        }

        const contentType = res.headers.get('content-type');
        const contentLength = res.headers.get('content-length');

        // Manejar respuestas sin contenido (HTTP 204 No Content, o Content-Length 0)
        if (res.status === 204 || (contentLength === '0') || (!contentType && !res.bodyUsed)) {
            return { data: undefined, status: res.status };
        }

        if (contentType && contentType.includes('application/json')) {
            const data: T = await res.json();
            return { data: data, status: res.status };
        } else {
            // Si no es JSON y no es 204/sin contenido, devolvemos undefined
            return { data: undefined, status: res.status };
        }

    } catch (error: any) {
        console.error("Error de conexión:", error);
        return { error: error.message, status: undefined };
    }
}

// --- API de Categorías (actualizadas para devolver ApiResponse) ---
export const getCategories = (): Promise<ApiResponse<Categoria[]>> => fetchAPI('/categorias');
export const getCategoryById = (id: number): Promise<ApiResponse<Categoria>> => fetchAPI(`/categorias/${id}`);
export const createCategory = (categoryData: Omit<Categoria, 'categoriaId'>): Promise<ApiResponse<Categoria>> => fetchAPI('/categorias', { method: 'POST', body: categoryData as any });
export const updateCategory = (id: number, categoryData: Omit<Categoria, 'categoriaId'>): Promise<ApiResponse<Categoria>> => fetchAPI(`/categorias/${id}`, { method: 'PUT', body: categoryData as any });
export const deleteCategory = (id: number): Promise<ApiResponse<void>> => fetchAPI(`/categorias/${id}`, { method: 'DELETE', body: { motivoBaja: "Eliminado desde panel de admin" } as any });

// --- API de Productos (actualizadas para devolver ApiResponse) ---
export const getProducts = (params: { [key: string]: string | number } = {}): Promise<ApiResponse<Page<ProductoListado>>> => {
    const query = new URLSearchParams(params as Record<string, string>).toString();
    return fetchAPI(`/productos?${query}`);
};
export const getProductById = (id: number): Promise<ApiResponse<ProductoDetalle>> => fetchAPI(`/productos/${id}`);
export const createProduct = (formData: FormData): Promise<ApiResponse<ProductoDetalle>> => fetchAPI('/productos', { method: 'POST', body: formData });
export const updateProduct = (id: number, formData: FormData): Promise<ApiResponse<ProductoDetalle>> => fetchAPI(`/productos/${id}`, { method: 'PUT', body: formData });
export const deleteProduct = (id: number): Promise<ApiResponse<void>> => fetchAPI(`/productos/${id}`, { method: 'DELETE', body: { motivoBaja: "Eliminado desde panel de admin" } as any });

// --- API de Tipos de Producto (Ahora usa fetchAPI y devuelve ApiResponse) ---
export const getTiposProducto = (): Promise<ApiResponse<TipoProducto[]>> => {
    return fetchAPI('/productos/tipos-producto');
};

// --- NUEVAS FUNCIONES DE API PARA NIVELES DE LUZ Y FRECUENCIAS DE RIEGO ---
export const getNivelesLuz = (): Promise<ApiResponse<NivelLuz[]>> => fetchAPI('/nivelesluz');
export const getFrecuenciasRiego = (): Promise<ApiResponse<FrecuenciaRiego[]>> => fetchAPI('/frecuenciasriego');

// --- API del Dashboard (actualizada para devolver ApiResponse) ---
export const getDashboardData = (): Promise<ApiResponse<DashboardDTO>> => fetchAPI('/dashboard/metrics');

// --- API del Carrito (usando cliente anónimo y devolviendo ApiResponse) ---
export const getCart = (): Promise<ApiResponse<CarritoItem[]>> => {
    const clienteId = getAnonymousClientId();
    return fetchAPI(`/carrito/${clienteId}`);
};

export const addToCart = (productoId: number, cantidad: number): Promise<ApiResponse<CarritoItem>> => {
    const clienteId = getAnonymousClientId();
    const query = new URLSearchParams({ productoId: String(productoId), cantidad: String(cantidad) }).toString();
    return fetchAPI(`/carrito/${clienteId}/agregar?${query}`, { method: 'POST' });
};

export const updateCartItemQuantity = (productoId: number, nuevaCantidad: number): Promise<ApiResponse<CarritoItem>> => {
    const clienteId = getAnonymousClientId();
    const query = new URLSearchParams({ productoId: String(productoId), nuevaCantidad: String(nuevaCantidad) }).toString();
    return fetchAPI(`/carrito/${clienteId}/actualizar?${query}`, { method: 'PUT' });
};

export const removeFromCart = (productoId: number): Promise<ApiResponse<void>> => {
    const clienteId = getAnonymousClientId();
    return fetchAPI(`/carrito/${clienteId}/eliminar/${productoId}`, { method: 'DELETE' });
};

export const clearCart = (): Promise<ApiResponse<void>> => {
    const clienteId = getAnonymousClientId();
    return fetchAPI(`/carrito/${clienteId}/vaciar`, { method: 'DELETE' });
};

export const checkoutCart = (checkoutData: CheckoutRequest): Promise<ApiResponse<Pedido>> => {
    const clienteId = getAnonymousClientId();
    const body = { ...checkoutData, clienteId: parseInt(clienteId) };
    return fetchAPI('/carrito/checkout', {
        method: 'POST',
        body: body as any, // <-- Añadir 'as any' aquí también
    });
};

export const createOrderAndGetPreference = async (): Promise<ApiResponse<{ id: string }>> => {
    const clienteId = getAnonymousClientId();
    const checkoutRequest = {
        clienteId: parseInt(clienteId),
        metodoPago: "Mercado Pago",
        notasCliente: "Pedido realizado desde la web."
    };

    return fetchAPI('/carrito/checkout', {
        method: 'POST',
        body: checkoutRequest as any, // <-- Añadir 'as any' aquí también
    });
};

// --- Nuevas APIs para Autenticación y Registro ---
export const loginUser = (credentials: AuthRequest): Promise<ApiResponse<Usuario>> => {
    return fetchAPI('/auth/login', {
        method: 'POST',
        body: credentials as any, // <-- Solución: Añadir 'as any'
    });
};

export const registerClient = (registrationData: ClienteRegistro): Promise<ApiResponse<Usuario>> => {
    return fetchAPI('/auth/register', {
        method: 'POST',
        body: registrationData as any, // <-- Solución: Añadir 'as any'
    });
};