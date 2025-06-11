// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/dto/CarritoItemDTO.java
package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;

public class CarritoItemDTO {
    private Integer productoId; // Changed to Long for consistency with CarritoItemId and Producto.productoId
    private Integer cantidad;
    private String nombreProducto; // Para mostrar en el carrito
    private Double precioUnitario; // Para mostrar en el carrito
    private String imagenProductoUrl; // Para mostrar en el carrito

    // Constructor por defecto
    public CarritoItemDTO() {
    }

    // Constructor con parámetros
    public CarritoItemDTO(Integer productoId, String nombreProducto, Integer cantidad, BigDecimal precioUnitario, String imagenProductoUrl) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        // Convertimos de BigDecimal a Double
        this.precioUnitario = (precioUnitario != null) ? precioUnitario.doubleValue() : null;
        this.imagenProductoUrl = imagenProductoUrl;
    }

    // Constructor con más detalles para la vista del carrito
    public CarritoItemDTO(Integer clienteId, Integer productoId, Integer cantidad, String nombreProducto, Double precioUnitario, String imagenProductoUrl) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.imagenProductoUrl = imagenProductoUrl;
    }

    // Getters y Setters

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
