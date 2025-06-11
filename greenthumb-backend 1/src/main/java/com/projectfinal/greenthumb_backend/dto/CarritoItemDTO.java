// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/dto/CarritoItemDTO.java
package com.projectfinal.greenthumb_backend.dto;

public class CarritoItemDTO {
    private Integer clienteId;
    private Integer productoId; // Changed to Long for consistency with CarritoItemId and Producto.productoId
    private Integer cantidad;
    private String nombreProducto; // Para mostrar en el carrito
    private Double precioUnitario; // Para mostrar en el carrito
    private String imagenProductoUrl; // Para mostrar en el carrito

    // Constructor por defecto
    public CarritoItemDTO() {
    }

    // Constructor con parámetros
    public CarritoItemDTO(Integer clienteId, Integer productoId, Integer cantidad) {
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    // Constructor con más detalles para la vista del carrito
    public CarritoItemDTO(Integer clienteId, Integer productoId, Integer cantidad, String nombreProducto, Double precioUnitario, String imagenProductoUrl) {
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.imagenProductoUrl = imagenProductoUrl;
    }

    // Getters y Setters
    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getImagenProductoUrl() {
        return imagenProductoUrl;
    }

    public void setImagenProductoUrl(String imagenProductoUrl) {
        this.imagenProductoUrl = imagenProductoUrl;
    }
}
