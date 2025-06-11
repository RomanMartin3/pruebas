package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "CostosProductoHistorial") // Nombre de la nueva tabla
public class CostoProductoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistorialCostoID")
    private Integer historialCostoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;

    @Column(name = "PrecioCosto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDateTime fechaInicioVigencia;

    @Column(name = "FechaFinVigencia", nullable = false) // Siempre será una fecha pasada real
    private LocalDateTime fechaFinVigencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin", nullable = false)
    private Administrador administrador;

    // Constructores
    public CostoProductoHistorial() {
    }

    public CostoProductoHistorial(Producto producto, BigDecimal precioCosto, LocalDateTime fechaInicioVigencia, LocalDateTime fechaFinVigencia, Administrador administrador) {
        this.producto = producto;
        this.precioCosto = precioCosto;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.fechaFinVigencia = fechaFinVigencia;
        this.administrador = administrador;
    }

    // Getters y Setters
    public Integer getHistorialCostoId() {
        return historialCostoId;
    }

    public void setHistorialCostoId(Integer historialCostoId) {
        this.historialCostoId = historialCostoId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public LocalDateTime getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public void setFechaInicioVigencia(LocalDateTime fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public LocalDateTime getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(LocalDateTime fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CostoProductoHistorial that = (CostoProductoHistorial) o;
        return Objects.equals(historialCostoId, that.historialCostoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historialCostoId);
    }

    @Override
    public String toString() {
        return "CostoProductoHistorial{" +
                "historialCostoId=" + historialCostoId +
                ", productoId=" + (producto != null ? producto.getProductoId() : "null") +
                ", precioCosto=" + precioCosto +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                ", fechaFinVigencia=" + fechaFinVigencia +
                '}';
    }
}
