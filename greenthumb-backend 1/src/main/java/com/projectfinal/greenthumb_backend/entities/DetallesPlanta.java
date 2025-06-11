package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "detallesplanta")
public class DetallesPlanta {

    @Id
    @Column(name = "ProductoID") // PK y FK a Producto
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "NombreCientifico", nullable = false, length = 150) // Original: DEFAULT 'No especificado'
    private String nombreCientifico;

    @Column(name = "TipoAmbiente", nullable = false, length = 50) // Original: DEFAULT 'Indiferente'
    private String tipoAmbiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NivelLuzID", nullable = false) // Original: DEFAULT 0. FK a nivelesluz.
    private NivelesLuz nivelLuz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FrecuenciaRiegoID", nullable = false) // Original: DEFAULT 0. FK a frecuenciasriego.
    private FrecuenciasRiego frecuenciaRiego;

    @Column(name = "EsVenenosa", nullable = false) // Original: TINYINT(1) DEFAULT 0
    private boolean esVenenosa;

    @Column(name = "CuidadosEspeciales", nullable = false, columnDefinition = "TEXT")
    private String cuidadosEspeciales;

    // Constructores
    public DetallesPlanta() {
    }

    public DetallesPlanta(Producto producto, String nombreCientifico, String tipoAmbiente, NivelesLuz nivelLuz, FrecuenciasRiego frecuenciaRiego, boolean esVenenosa, String cuidadosEspeciales) {
        this.producto = producto;
        this.nombreCientifico = nombreCientifico;
        this.tipoAmbiente = tipoAmbiente;
        this.nivelLuz = nivelLuz;
        this.frecuenciaRiego = frecuenciaRiego;
        this.esVenenosa = esVenenosa;
        this.cuidadosEspeciales = cuidadosEspeciales;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.id = producto.getProductoId();
        } else {
            this.id = null;
        }
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public String getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(String tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public NivelesLuz getNivelLuz() {
        return nivelLuz;
    }

    public void setNivelLuz(NivelesLuz nivelLuz) {
        this.nivelLuz = nivelLuz;
    }

    public FrecuenciasRiego getFrecuenciaRiego() {
        return frecuenciaRiego;
    }

    public void setFrecuenciaRiego(FrecuenciasRiego frecuenciaRiego) {
        this.frecuenciaRiego = frecuenciaRiego;
    }

    public boolean isEsVenenosa() {
        return esVenenosa;
    }

    public void setEsVenenosa(boolean esVenenosa) {
        this.esVenenosa = esVenenosa;
    }

    public String getCuidadosEspeciales() {
        return cuidadosEspeciales;
    }

    public void setCuidadosEspeciales(String cuidadosEspeciales) {
        this.cuidadosEspeciales = cuidadosEspeciales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesPlanta that = (DetallesPlanta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DetallesPlanta{" +
                "id=" + id +
                ", nombreCientifico='" + nombreCientifico + '\'' +
                '}';
    }
}
