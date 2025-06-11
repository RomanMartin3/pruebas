package com.projectfinal.greenthumb_backend.dto;

public class EstadoPedidoUpdateRequestDTO {
    private String nuevoEstado;
    private String notas;

    // Getters y Setters
    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}