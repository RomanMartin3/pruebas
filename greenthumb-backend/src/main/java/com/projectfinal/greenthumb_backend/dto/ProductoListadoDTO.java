package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal; // Para el precio

public class ProductoListadoDTO {
    private Integer productoId;
    private String nombreProducto;
    private String categoriaNombre; // Nombre de la categoría
    private String tipoProductoNombre; // Nombre del tipo de producto
    private BigDecimal precioVentaActual; // Precio de venta actual
    private String imagenUrlPrincipal; // URL de la imagen principal (si aplica)
    private Integer stockActual;


    public ProductoListadoDTO() {}

    // Constructor, getters y setters
    public ProductoListadoDTO(Integer productoId, String nombreProducto, String categoriaNombre, String tipoProductoNombre, BigDecimal precioVentaActual, String imagenUrlPrincipal, Integer stockActual) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.categoriaNombre = categoriaNombre;
        this.tipoProductoNombre = tipoProductoNombre;
        this.precioVentaActual = precioVentaActual;
        this.imagenUrlPrincipal = imagenUrlPrincipal;
        this.stockActual = stockActual;
    }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    public String getTipoProductoNombre() { return tipoProductoNombre; }
    public void setTipoProductoNombre(String tipoProductoNombre) { this.tipoProductoNombre = tipoProductoNombre; }
    public BigDecimal getPrecioVentaActual() { return precioVentaActual; }
    public void setPrecioVentaActual(BigDecimal precioVentaActual) { this.precioVentaActual = precioVentaActual; }
    public String getImagenUrlPrincipal() { return imagenUrlPrincipal; }
    public void setImagenUrlPrincipal(String imagenUrlPrincipal) { this.imagenUrlPrincipal = imagenUrlPrincipal; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}