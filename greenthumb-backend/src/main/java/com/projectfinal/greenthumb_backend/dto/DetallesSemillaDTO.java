package com.projectfinal.greenthumb_backend.dto;

import java.math.BigDecimal;

public class DetallesSemillaDTO {
    private String especieVariedad;
    private String epocaSiembraIdeal;
    private BigDecimal profundidadSiembraCM;
    private String tiempoGerminacionDias;
    private String instruccionesSiembra;

    public DetallesSemillaDTO() {}

    public DetallesSemillaDTO(String especieVariedad, String epocaSiembraIdeal, BigDecimal profundidadSiembraCM, String tiempoGerminacionDias, String instruccionesSiembra) {
        this.especieVariedad = especieVariedad;
        this.epocaSiembraIdeal = epocaSiembraIdeal;
        this.profundidadSiembraCM = profundidadSiembraCM;
        this.tiempoGerminacionDias = tiempoGerminacionDias;
        this.instruccionesSiembra = instruccionesSiembra;
    }
    // Getters y Setters
    public String getEspecieVariedad() { return especieVariedad; }
    public void setEspecieVariedad(String especieVariedad) { this.especieVariedad = especieVariedad; }
    public String getEpocaSiembraIdeal() { return epocaSiembraIdeal; }
    public void setEpocaSiembraIdeal(String epocaSiembraIdeal) { this.epocaSiembraIdeal = epocaSiembraIdeal; }
    public BigDecimal getProfundidadSiembraCM() { return profundidadSiembraCM; }
    public void setProfundidadSiembraCM(BigDecimal profundidadSiembraCM) { this.profundidadSiembraCM = profundidadSiembraCM; }
    public String getTiempoGerminacionDias() { return tiempoGerminacionDias; }
    public void setTiempoGerminacionDias(String tiempoGerminacionDias) { this.tiempoGerminacionDias = tiempoGerminacionDias; }
    public String getInstruccionesSiembra() { return instruccionesSiembra; }
    public void setInstruccionesSiembra(String instruccionesSiembra) { this.instruccionesSiembra = instruccionesSiembra; }
}