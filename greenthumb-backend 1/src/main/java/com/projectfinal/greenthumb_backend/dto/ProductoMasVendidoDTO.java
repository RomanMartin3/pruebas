package com.projectfinal.greenthumb_backend.dto;

public class ProductoMasVendidoDTO {
    private Integer productoId;
    private String nombreProducto;
    private long cantidadVendida;

    public ProductoMasVendidoDTO(Integer productoId, String nombreProducto, long cantidadVendida) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
    }

    // Getters y Setters
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
}