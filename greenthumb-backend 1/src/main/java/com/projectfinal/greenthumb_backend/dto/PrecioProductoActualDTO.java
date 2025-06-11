package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrecioProductoActualDTO {
    private BigDecimal precioVenta;
    private LocalDateTime fechaInicioVigencia;

    public PrecioProductoActualDTO() {}

    public PrecioProductoActualDTO(BigDecimal precioVenta, LocalDateTime fechaInicioVigencia) {
        this.precioVenta = precioVenta;
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    // Getters y Setters
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public LocalDateTime getFechaInicioVigencia() { return fechaInicioVigencia; }
    public void setFechaInicioVigencia(LocalDateTime fechaInicioVigencia) { this.fechaInicioVigencia = fechaInicioVigencia; }
}