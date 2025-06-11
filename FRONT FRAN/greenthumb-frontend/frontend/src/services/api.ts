import { CarritoItem, Categoria, CheckoutRequest, Page, Pedido, ProductoDetalle, ProductoListado, UsuarioDTO, ClienteRegistroDTO, DashboardDTO } from "@/libs/types";

const API_BASE_URL = "http://localhost:8080";
const API_PREFIX = "/api";

async function fetchAPI<T>(endpoint: string, options: RequestInit = {}, token?: string): Promise<T> {
    const url = `${API_BASE_URL}${API_PREFIX}${endpoint}`;
    
    const headers = new Headers();
    if (!(options.body instanceof FormData)) {
        headers.append('Content-Type', 'application/json');
    }

    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    }

    const config: RequestInit = {
        ...options,
        headers,
    };

    const res = await fetch(url, config);

    if (!res.ok) {
        let errorBody;
        try {
            errorBody = await res.json();
        } catch (e) {
            errorBody = await res.text();
        }
        console.error(`Error en la petición a ${endpoint}: ${res.status} ${res.statusText}`, errorBody);
        const errorMessage = typeof errorBody === 'object' && errorBody.error ? errorBody.error : `Error en la petición: ${res.statusText}`;
        throw new Error(errorMessage);
    }

    if (res.status === 204) {
        return Promise.resolve(null as T);
    }

    return res.json();
}

export const getCategories = (): Promise<Categoria[]> => {
    return fetchAPI<Categoria[]>('/categorias');
};

export const getCategoryById = (id: number): Promise<Categoria> => {
    return fetchAPI<Categoria>(`/categorias/${id}`);
};

export const createCategory = (categoryData: Omit<Categoria, 'categoriaId'>, token: string): Promise<Categoria> => {
    return fetchAPI<Categoria>('/categorias', {
        method: 'POST',
        body: JSON.stringify(categoryData),
    }, token);
};

export const updateCategory = (id: number, categoryData: Omit<Categoria, 'categoriaId'>, token: string): Promise<Categoria> => {
    return fetchAPI<Categoria>(`/categorias/${id}`, {
        method: 'PUT',
        body: JSON.stringify(categoryData),
    }, token);
};

export const deleteCategory = (id: number, token: string): Promise<void> => {
    return fetchAPI<void>(`/categorias/${id}`, { 
        method: 'DELETE',
        body: JSON.stringify({ motivoBaja: "Eliminado desde panel de admin" })
    }, token);
};


export const getProducts = (params: { [key: string]: string | number } = {}): Promise<Page<ProductoListado>> => {
    const query = new URLSearchParams(params as Record<string, string>).toString();
    return fetchAPI<Page<ProductoListado>>(`/productos?${query}`);
};

export const getProductById = (id: number): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>(`/productos/${id}`);
};

export const createProduct = (formData: FormData, token: string): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>('/productos', {
        method: 'POST',
        body: formData,
    }, token);
};

export const updateProduct = (id: number, formData: FormData, token: string): Promise<ProductoDetalle> => {
    return fetchAPI<ProductoDetalle>(`/productos/${id}`, {
        method: 'PUT',
        body: formData,
    }, token);
};

export const deleteProduct = (id: number, token: string): Promise<void> => {
    return fetchAPI<void>(`/productos/${id}`, { 
        method: 'DELETE',
        body: JSON.stringify({ motivoBaja: "Eliminado desde panel de admin" })
    }, token);
};


export const getDashboardData = (token: string): Promise<DashboardDTO> => {
    return fetchAPI<DashboardDTO>('/dashboard', {}, token);
};


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

export const sincronizarUsuario = (token: string): Promise<UsuarioDTO> => {
    console.log("LOG: [API] Llamando a /auth/sincronizar-usuario");
    return fetchAPI<UsuarioDTO>('/auth/sincronizar-usuario', {}, token);
};

export const registrarNuevoCliente = (data: Omit<ClienteRegistroDTO, 'auth0Id' | 'email' | 'nombre' | 'apellido'>, token: string): Promise<UsuarioDTO> => {
    console.log("LOG: [API] Llamando a /auth/registrar-cliente");
    return fetchAPI<UsuarioDTO>('/auth/registrar-cliente', {
        method: 'POST',
        body: JSON.stringify(data),
    }, token);
};