// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/entities/PrecioProductoActual.java
package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PreciosProductoActual")
public class PrecioProductoActual {

    @Id
    @Column(name = "ProductoID")
    private Integer id; // Mapea a Producto.productoId

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indica que 'id' usa el mismo valor que la PK de la entidad 'producto' y es la FK.
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "PrecioVenta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDateTime fechaInicioVigencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin", nullable = false)
    private Administrador administrador;

    // Constructores
    public PrecioProductoActual() {
    }
    public PrecioProductoActual(Producto producto, BigDecimal precioVenta, Administrador administrador) {
        this.producto = producto;
        this.precioVenta = precioVenta;
        this.administrador = administrador;
    }
    @PrePersist
    protected void onPrePersist() {
        if (this.fechaInicioVigencia == null) {
            this.fechaInicioVigencia = LocalDateTime.now();
        }
        if (this.producto != null && this.id == null) { // Asegurar que el ID se establece si se pasa el producto
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
            this.id = producto.getProductoId(); // Mantener sincronizado el ID
        } else {
            this.id = null;
        }
    }

    public BigDecimal getPrecioVenta() { // Se usa getPrecioVenta
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
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
        if (!(o instanceof PrecioProductoActual)) return false;
        PrecioProductoActual that = (PrecioProductoActual) o;
        return Objects.equals(getId(), that.getId()); // La PK es el ID del producto
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PrecioProductoActual{" +
                "id=" + id +
                ", precioVenta=" + precioVenta +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                '}';
    }
}