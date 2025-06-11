package com.projectfinal.greenthumb_backend.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PreciosProductoHistorial")
public class PrecioProductoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistorialPrecioID")
    private Integer historialPrecioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;

    @Column(name = "PrecioVenta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "FechaInicioVigencia", nullable = false)
    private LocalDateTime fechaInicioVigencia;

    @Column(name = "FechaFinVigencia", nullable = false)
    private LocalDateTime fechaFinVigencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin", nullable = false)
    private Administrador administrador;

    // Constructores
    public PrecioProductoHistorial() {
    }

    public PrecioProductoHistorial(Producto producto, BigDecimal precioVenta, LocalDateTime fechaInicioVigencia, LocalDateTime fechaFinVigencia, Administrador administrador) {
        this.producto = producto;
        this.precioVenta = precioVenta;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.fechaFinVigencia = fechaFinVigencia;
        this.administrador = administrador;
    }

    // Getters y Setters
    public Integer getHistorialPrecioId() {
        return historialPrecioId;
    }

    public void setHistorialPrecioId(Integer historialPrecioId) {
        this.historialPrecioId = historialPrecioId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getPrecioVenta() {
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
        if (!(o instanceof PrecioProductoHistorial)) return false;
        PrecioProductoHistorial that = (PrecioProductoHistorial) o;
        return Objects.equals(getHistorialPrecioId(), that.getHistorialPrecioId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHistorialPrecioId());
    }

    @Override
    public String toString() {
        return "PrecioProductoHistorial{" +
                "historialPrecioId=" + historialPrecioId +
                ", productoId=" + (producto != null ? producto.getProductoId() : "null") +
                ", precioVenta=" + precioVenta +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                ", fechaFinVigencia=" + fechaFinVigencia +
                '}';
    }
}
