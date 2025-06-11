package com.projectfinal.greenthumb_backend.dto;

// Ejemplo para DetallesPlantaDTO
public class DetallesPlantaDTO {
    private String nombreCientifico;
    private String tipoAmbiente;
    private String nivelLuzDescripcion; // En lugar del ID
    private String frecuenciaRiegoDescripcion; // En lugar del ID
    private boolean esVenenosa;
    private String cuidadosEspeciales;

    public DetallesPlantaDTO() {}

    public DetallesPlantaDTO(String nombreCientifico, String tipoAmbiente, String nivelLuzDescripcion, String frecuenciaRiegoDescripcion, boolean esVenenosa, String cuidadosEspeciales) {
        this.nombreCientifico = nombreCientifico;
        this.tipoAmbiente = tipoAmbiente;
        this.nivelLuzDescripcion = nivelLuzDescripcion;
        this.frecuenciaRiegoDescripcion = frecuenciaRiegoDescripcion;
        this.esVenenosa = esVenenosa;
        this.cuidadosEspeciales = cuidadosEspeciales;
    }

    // Getters y Setters
    public String getNombreCientifico() { return nombreCientifico; }
    public void setNombreCientifico(String nombreCientifico) { this.nombreCientifico = nombreCientifico; }
    public String getTipoAmbiente() { return tipoAmbiente; }
    public void setTipoAmbiente(String tipoAmbiente) { this.tipoAmbiente = tipoAmbiente; }
    public String getNivelLuzDescripcion() { return nivelLuzDescripcion; }
    public void setNivelLuzDescripcion(String nivelLuzDescripcion) { this.nivelLuzDescripcion = nivelLuzDescripcion; }
    public String getFrecuenciaRiegoDescripcion() { return frecuenciaRiegoDescripcion; }
    public void setFrecuenciaRiegoDescripcion(String frecuenciaRiegoDescripcion) { this.frecuenciaRiegoDescripcion = frecuenciaRiegoDescripcion; }
    public boolean isEsVenenosa() { return esVenenosa; }
    public void setEsVenenosa(boolean esVenenosa) { this.esVenenosa = esVenenosa; }
    public String getCuidadosEspeciales() { return cuidadosEspeciales; }
    public void setCuidadosEspeciales(String cuidadosEspeciales) { this.cuidadosEspeciales = cuidadosEspeciales; }
}
