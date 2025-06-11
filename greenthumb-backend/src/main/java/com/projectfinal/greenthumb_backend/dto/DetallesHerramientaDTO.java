package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;

public class DetallesHerramientaDTO {
    private String materialPrincipal;
    private String dimensiones;
    private BigDecimal pesoKG;
    private String usoRecomendado;
    private boolean requiereMantenimiento;

    public DetallesHerramientaDTO() {}

    public DetallesHerramientaDTO(String materialPrincipal, String dimensiones, BigDecimal pesoKG, String usoRecomendado, boolean requiereMantenimiento) {
        this.materialPrincipal = materialPrincipal;
        this.dimensiones = dimensiones;
        this.pesoKG = pesoKG;
        this.usoRecomendado = usoRecomendado;
        this.requiereMantenimiento = requiereMantenimiento;
    }
    // Getters y Setters
    public String getMaterialPrincipal() { return materialPrincipal; }
    public void setMaterialPrincipal(String materialPrincipal) { this.materialPrincipal = materialPrincipal; }
    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
    public BigDecimal getPesoKG() { return pesoKG; }
    public void setPesoKG(BigDecimal pesoKG) { this.pesoKG = pesoKG; }
    public String getUsoRecomendado() { return usoRecomendado; }
    public void setUsoRecomendado(String usoRecomendado) { this.usoRecomendado = usoRecomendado; }
    public boolean isRequiereMantenimiento() { return requiereMantenimiento; }
    public void setRequiereMantenimiento(boolean requiereMantenimiento) { this.requiereMantenimiento = requiereMantenimiento; }
}