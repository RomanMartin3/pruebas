package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private Integer productoId;
    private String nombreProducto;
    private Integer cantidadComprada;
    private BigDecimal precioUnitarioEnMomentoCompra; // Almacenar el precio para registro histórico

    // Constructor
    public DetallePedidoDTO() {}

    public DetallePedidoDTO(Integer productoId, String nombreProducto, Integer cantidadComprada, BigDecimal precioUnitarioEnMomentoCompra) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidadComprada = cantidadComprada;
        this.precioUnitarioEnMomentoCompra = precioUnitarioEnMomentoCompra;
    }

    // Getters y Setters
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Integer getCantidadComprada() { return cantidadComprada; }
    public void setCantidadComprada(Integer cantidadComprada) { this.cantidadComprada = cantidadComprada; }
    public BigDecimal getPrecioUnitarioEnMomentoCompra() { return precioUnitarioEnMomentoCompra; }
    public void setPrecioUnitarioEnMomentoCompra(BigDecimal precioUnitarioEnMomentoCompra) { this.precioUnitarioEnMomentoCompra = precioUnitarioEnMomentoCompra; }
}