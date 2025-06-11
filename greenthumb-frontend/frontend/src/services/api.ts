import { CarritoItem, Categoria, CheckoutRequest, Page, Pedido, ProductoDetalle, ProductoListado, UsuarioDTO, ClienteRegistroDTO } from "@/libs/types";

const API_BASE_URL = "http://localhost:8080";
const API_PREFIX = "/api";

// Helper to construct URLs

/**
 * Generic fetch wrapper to handle requests and errors.
 * Supports both JSON and FormData requests.
 */
async function fetchAPI<T>(endpoint: string, options: RequestInit = {}, token?: string): Promise<T> {
    const url = `${API_BASE_URL}${API_PREFIX}${endpoint}`;

    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    // Si se proporciona un token, lo añadimos al header de Autorización
    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    }

    const res = await fetch(url, { ...options, headers });

    if (!res.ok) {
        const errorBody = await res.text();
        console.error(`Error en la petición a ${endpoint}: ${res.status} ${res.statusText}`, errorBody);
        throw new Error(`Error en la petición: ${res.statusText}`);
    }

    return res.json();
}

// --- Category API ---
// Controller: CategoriaController.java

export const getCategories = (): Promise<Categoria[]> => {
    return fetchAPI<Categoria[]>('/categorias');
};

export const getCategoryById = (id: number): Promise<Categoria> => {
    return fetchAPI<Categoria>(`/categorias/${id}`);
};

export const createCategory = (categoryData: Omit<Categoria, 'categoriaId'>): Promise<Categoria> => {
    return fetchAPI<Categoria>('/categorias', {
        method: 'POST',
        body: JSON.stringify(categoryData),
    });
};

export const updateCategory = (id: number, categoryData: Categoria): Promise<Categoria> => {
    return fetchAPI<Categoria>(`/categorias/${id}`, {
        method: 'PUT',
        body: JSON.stringify(categoryData),
    });
};

export const deleteCategory = (id: number): Promise<void> => {
    return fetchAPI<void>(`/categorias/${id}`, { method: 'DELETE' });
};


// --- Product API ---
// Controller: ProductoController.java

export const getProducts = (params: { [key: string]: string | number } = {}): Promise<Page<ProductoListado>> => {
    const query = new URLSearchParams(params as Record<string, string>).toString();
    return fetchAPI<Page<ProductoListado>>(`/productos?${query}`);
};

export const getProductById = (id: number): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>(`/productos/${id}`);
};

/**
 * Creates a new product. Handles multipart form data for image upload.
 */
export const createProduct = (formData: FormData): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>('/productos', {
        method: 'POST',
        body: formData, // FormData for multipart request
    });
};

/**
 * Updates an existing product. Handles multipart form data for image upload.
 */
export const updateProduct = (id: number, formData: FormData): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>(`/productos/${id}`, {
        method: 'PUT',
        body: formData, // FormData for multipart request
    });
};

export const deleteProduct = (id: number): Promise<void> => {
    return fetchAPI<void>(`/productos/${id}`, { method: 'DELETE' });
};


// --- Cart API ---
// Controller: CarritoController.java
// NOTE: We assume a static clienteId for now. This should be dynamic with authentication.
const CLIENTE_ID = 1;

export const getCart = (): Promise<CarritoItem[]> => {
    return fetchAPI<CarritoItem[]>(`/carrito/2`);
};

export const addToCart = (productoId: number, cantidad: number): Promise<CarritoItem> => {
    const query = new URLSearchParams({ productoId: String(productoId), cantidad: String(cantidad) }).toString();
    return fetchAPI<CarritoItem>(`/carrito/2/agregar?${query}`, {
        method: 'POST',
    });
};

export const updateCartItemQuantity = (productoId: number, nuevaCantidad: number): Promise<CarritoItem> => {
    const query = new URLSearchParams({ productoId: String(productoId), nuevaCantidad: String(nuevaCantidad) }).toString();
    return fetchAPI<CarritoItem>(`/carrito/2/actualizar?${query}`, {
        method: 'PUT',
    });
};

export const removeFromCart = (productoId: number): Promise<void> => {
    return fetchAPI<void>(`/carrito/2/eliminar/${productoId}`, {
        method: 'DELETE',
    });
};

export const clearCart = (): Promise<void> => {
    return fetchAPI<void>(`/carrito/2/vaciar`, {
        method: 'DELETE',
    });
};

export const checkoutCart = (checkoutData: CheckoutRequest): Promise<Pedido> => {
    return fetchAPI<Pedido>(`/carrito/2/checkout`, {
        method: 'POST',
        body: JSON.stringify(checkoutData),
    });
};

// --- Auth API ---
// Controller: AuthController.java
export const sincronizarUsuario = (token: string): Promise<UsuarioDTO> => {
    console.log("LOG: [API] Llamando a /auth/sincronizar-usuario");
    // Pasamos el token a fetchAPI
    return fetchAPI<UsuarioDTO>('/auth/sincronizar-usuario', {}, token);
};

export const registrarNuevoCliente = (data: ClienteRegistroDTO, token: string): Promise<UsuarioDTO> => {
    console.log("LOG: [API] Llamando a /auth/registrar-cliente");
    return fetchAPI<UsuarioDTO>('/auth/registrar-cliente', {
        method: 'POST',
        body: JSON.stringify(data),
    }, token);
};