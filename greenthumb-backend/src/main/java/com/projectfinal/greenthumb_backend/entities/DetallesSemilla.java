package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detallessemilla")
public class DetallesSemilla {

    @Id
    @Column(name = "ProductoID") // PK y FK a Producto
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "EspecieVariedad", nullable = false, length = 150) // Original: DEFAULT 'No especificada'
    private String especieVariedad;

    @Column(name = "EpocaSiembraIdeal", nullable = false, length = 100) // Original: DEFAULT 'Consultar'
    private String epocaSiembraIdeal;

    @Column(name = "ProfundidadSiembraCM", nullable = false, precision = 5, scale = 2) // Original: DEFAULT 0.00
    private BigDecimal profundidadSiembraCM;

    @Column(name = "TiempoGerminacionDias", nullable = false, length = 50) // Original: DEFAULT 'N/A'
    private String tiempoGerminacionDias;

    @Column(name = "InstruccionesSiembra", nullable = false, columnDefinition = "TEXT")
    private String instruccionesSiembra;

    // Constructores
    public DetallesSemilla() {
    }

    public DetallesSemilla(Producto producto, String especieVariedad, String epocaSiembraIdeal, BigDecimal profundidadSiembraCM, String tiempoGerminacionDias, String instruccionesSiembra) {
        this.producto = producto;
        this.especieVariedad = especieVariedad;
        this.epocaSiembraIdeal = epocaSiembraIdeal;
        this.profundidadSiembraCM = profundidadSiembraCM;
        this.tiempoGerminacionDias = tiempoGerminacionDias;
        this.instruccionesSiembra = instruccionesSiembra;
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

    public String getEspecieVariedad() {
        return especieVariedad;
    }

    public void setEspecieVariedad(String especieVariedad) {
        this.especieVariedad = especieVariedad;
    }

    public String getEpocaSiembraIdeal() {
        return epocaSiembraIdeal;
    }

    public void setEpocaSiembraIdeal(String epocaSiembraIdeal) {
        this.epocaSiembraIdeal = epocaSiembraIdeal;
    }

    public BigDecimal getProfundidadSiembraCM() {
        return profundidadSiembraCM;
    }

    public void setProfundidadSiembraCM(BigDecimal profundidadSiembraCM) {
        this.profundidadSiembraCM = profundidadSiembraCM;
    }

    public String getTiempoGerminacionDias() {
        return tiempoGerminacionDias;
    }

    public void setTiempoGerminacionDias(String tiempoGerminacionDias) {
        this.tiempoGerminacionDias = tiempoGerminacionDias;
    }

    public String getInstruccionesSiembra() {
        return instruccionesSiembra;
    }

    public void setInstruccionesSiembra(String instruccionesSiembra) {
        this.instruccionesSiembra = instruccionesSiembra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesSemilla that = (DetallesSemilla) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DetallesSemilla{" +
                "id=" + id +
                ", especieVariedad='" + especieVariedad + '\'' +
                '}';
    }
}