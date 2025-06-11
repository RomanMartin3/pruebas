package com.projectfinal.greenthumb_backend.dto;

public class ImagenProductoDTO {
    private Integer imagenId;
    private String urlImagen;
    private String textoAlternativo;

    public ImagenProductoDTO() {}

    public ImagenProductoDTO(Integer imagenId, String urlImagen, String textoAlternativo) {
        this.imagenId = imagenId;
        this.urlImagen = urlImagen;
        this.textoAlternativo = textoAlternativo;
    }

    // Getters y Setters
    public Integer getImagenId() { return imagenId; }
    public void setImagenId(Integer imagenId) { this.imagenId = imagenId; }
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    public String getTextoAlternativo() { return textoAlternativo; }
    public void setTextoAlternativo(String textoAlternativo) { this.textoAlternativo = textoAlternativo; }
}
