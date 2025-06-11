package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "frecuenciasriego")
public class FrecuenciasRiego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FrecuenciaRiegoID")
    private Integer frecuenciaRiegoId;

    @Column(name = "DescripcionFrecuenciaRiego", nullable = false, unique = true, length = 100)
    private String descripcionFrecuenciaRiego;

    @OneToMany(mappedBy = "frecuenciaRiego", fetch = FetchType.LAZY)
    private List<DetallesPlanta> detallesPlantas = new ArrayList<>();

    // Constructores
    public FrecuenciasRiego() {
    }

    public FrecuenciasRiego(String descripcionFrecuenciaRiego) {
        this.descripcionFrecuenciaRiego = descripcionFrecuenciaRiego;
    }

    // Getters y Setters
    public Integer getFrecuenciaRiegoId() {
        return frecuenciaRiegoId;
    }

    public void setFrecuenciaRiegoId(Integer frecuenciaRiegoId) {
        this.frecuenciaRiegoId = frecuenciaRiegoId;
    }

    public String getDescripcionFrecuenciaRiego() {
        return descripcionFrecuenciaRiego;
    }

    public void setDescripcionFrecuenciaRiego(String descripcionFrecuenciaRiego) {
        this.descripcionFrecuenciaRiego = descripcionFrecuenciaRiego;
    }

    public List<DetallesPlanta> getDetallesPlantas() {
        return detallesPlantas;
    }

    public void setDetallesPlantas(List<DetallesPlanta> detallesPlantas) {
        this.detallesPlantas = detallesPlantas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrecuenciasRiego that = (FrecuenciasRiego) o;
        return Objects.equals(frecuenciaRiegoId, that.frecuenciaRiegoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frecuenciaRiegoId);
    }

    @Override
    public String toString() {
        return "FrecuenciasRiego{" +
                "frecuenciaRiegoId=" + frecuenciaRiegoId +
                ", descripcionFrecuenciaRiego='" + descripcionFrecuenciaRiego + '\'' +
                '}';
    }
}
