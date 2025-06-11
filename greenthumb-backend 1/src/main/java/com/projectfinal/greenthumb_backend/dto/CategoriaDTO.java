package com.projectfinal.greenthumb_backend.dto;

public class CategoriaDTO {
    private Integer categoriaId;
    private String nombreCategoria;
    private String descripcionCategoria;

    // Constructores, Getters, Setters
    public CategoriaDTO(Integer categoriaId, String nombreCategoria, String descripcionCategoria) {
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
    }

    public CategoriaDTO() {
    }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public String getDescripcionCategoria() { return descripcionCategoria; }
    public void setDescripcionCategoria(String descripcionCategoria) { this.descripcionCategoria = descripcionCategoria; }

}