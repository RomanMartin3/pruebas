package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "nivelesluz")
public class NivelesLuz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NivelLuzID")
    private Integer nivelLuzId;

    @Column(name = "DescripcionNivelLuz", nullable = false, unique = true, length = 100)
    private String descripcionNivelLuz;

    @OneToMany(mappedBy = "nivelLuz", fetch = FetchType.LAZY)
    private List<DetallesPlanta> detallesPlantas = new ArrayList<>();


    // Constructores
    public NivelesLuz() {
    }

    public NivelesLuz(String descripcionNivelLuz) {
        this.descripcionNivelLuz = descripcionNivelLuz;
    }

    // Getters y Setters
    public Integer getNivelLuzId() {
        return nivelLuzId;
    }

    public void setNivelLuzId(Integer nivelLuzId) {
        this.nivelLuzId = nivelLuzId;
    }

    public String getDescripcionNivelLuz() {
        return descripcionNivelLuz;
    }

    public void setDescripcionNivelLuz(String descripcionNivelLuz) {
        this.descripcionNivelLuz = descripcionNivelLuz;
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
        NivelesLuz that = (NivelesLuz) o;
        return Objects.equals(nivelLuzId, that.nivelLuzId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nivelLuzId);
    }

    @Override
    public String toString() {
        return "NivelesLuz{" +
                "nivelLuzId=" + nivelLuzId +
                ", descripcionNivelLuz='" + descripcionNivelLuz + '\'' +
                '}';
    }
}