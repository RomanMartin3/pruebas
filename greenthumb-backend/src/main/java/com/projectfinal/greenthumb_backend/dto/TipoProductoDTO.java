package com.projectfinal.greenthumb_backend.dto;

public class TipoProductoDTO {
    private Integer tipoProductoId;
    private String nombreTipoProducto;
    // private String tablaDetalleAsociada; // Podrías incluirlo si es relevante para el frontend

    public TipoProductoDTO() {}

    public TipoProductoDTO(Integer tipoProductoId, String nombreTipoProducto) {
        this.tipoProductoId = tipoProductoId;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // Getters y Setters
    public Integer getTipoProductoId() { return tipoProductoId; }
    public void setTipoProductoId(Integer tipoProductoId) { this.tipoProductoId = tipoProductoId; }
    public String getNombreTipoProducto() { return nombreTipoProducto; }
    public void setNombreTipoProducto(String nombreTipoProducto) { this.nombreTipoProducto = nombreTipoProducto; }
}