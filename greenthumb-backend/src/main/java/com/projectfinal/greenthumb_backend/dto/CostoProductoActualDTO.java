package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CostoProductoActualDTO {
    private BigDecimal CostoVenta;
    private LocalDateTime fechaInicioVigencia;

    public CostoProductoActualDTO() {}

    public CostoProductoActualDTO(BigDecimal CostoVenta, LocalDateTime fechaInicioVigencia) {
        this.CostoVenta = CostoVenta;
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    // Getters y Setters
    public BigDecimal getCostoVenta() { return CostoVenta; }
    public void setCostoVenta(BigDecimal CostoVenta) { this.CostoVenta = CostoVenta; }
    public LocalDateTime getFechaInicioVigencia() { return fechaInicioVigencia; }
    public void setFechaInicioVigencia(LocalDateTime fechaInicioVigencia) { this.fechaInicioVigencia = fechaInicioVigencia; }
}