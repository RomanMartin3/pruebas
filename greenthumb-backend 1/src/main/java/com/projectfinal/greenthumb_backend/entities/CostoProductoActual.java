package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "CostosProductoActual") // Nombre de la nueva tabla
public class CostoProductoActual {

    @Id
    @Column(name = "ProductoID") // El ID del producto es la PK de esta tabla también
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indica que 'id' usa el mismo valor que la PK de la entidad 'producto'
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "PrecioCosto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDateTime fechaInicioVigencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin", nullable = false) // Quién registró/actualizó este costo
    private Administrador administrador;

    // Constructores
    public CostoProductoActual() {
    }

    public CostoProductoActual(Producto producto, BigDecimal precioCosto, Administrador administrador) {
        this.producto = producto;
        this.precioCosto = precioCosto;
        this.administrador = administrador;
    }

    @PrePersist
    protected void onPrePersist() {
        if (this.fechaInicioVigencia == null) {
            this.fechaInicioVigencia = LocalDateTime.now();
        }
        if (this.producto != null && this.id == null) {
            this.id = this.producto.getProductoId();
        }
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
        CostoProductoActual that = (CostoProductoActual) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CostoProductoActual{" +
                "id=" + id +
                ", precioCosto=" + precioCosto +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                '}';
    }
}
