package com.projectfinal.greenthumb_backend.dto;

import java.time.LocalDateTime;

public class HistorialEstadoDTO {
    private String estado;
    private LocalDateTime fechaCambio;
    private String adminEmail;
    private String notas;

    // Constructor, Getters y Setters
    public HistorialEstadoDTO(String estado, LocalDateTime fechaCambio, String adminEmail, String notas) {
        this.estado = estado;
        this.fechaCambio = fechaCambio;
        this.adminEmail = adminEmail;
        this.notas = notas;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}