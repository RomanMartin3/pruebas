// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/entities/CarritoItem.java
package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "carritoitems")
// @IdClass(CarritoItemId.class) // No es necesario si se usa @EmbeddedId
public class CarritoItem implements Serializable {

    @EmbeddedId
    private CarritoItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clienteId") // Mapea clienteId de CarritoItemId
    @JoinColumn(name = "UsuarioID_Cliente", referencedColumnName = "UsuarioID")
    private Cliente cliente;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productoId") // Mapea productoId de CarritoItemId
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "PrecioAlAgregar", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAlAgregar;

    @Column(name = "FechaAgregado", nullable = false, updatable = false)
    private LocalDateTime fechaAgregado;

    @Column(name = "FechaUltimaModificacion", nullable = false)
    private LocalDateTime fechaUltimaModificacion;

    // Constructores
    public CarritoItem() {
    }

    public CarritoItem(Cliente cliente, Producto producto, Integer cantidad, BigDecimal precioAlAgregar) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioAlAgregar = precioAlAgregar;
        this.id = new CarritoItemId(cliente.getUsuarioId(), producto.getProductoId()); // Inicializa el ID compuesto
    }

    // Constructor con parámetros para usar en la capa de servicio donde el precio se obtiene
    public CarritoItem(Cliente cliente, Producto producto, Integer cantidad) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.id = new CarritoItemId(cliente.getUsuarioId(), producto.getProductoId()); // Inicializa el ID compuesto
    }

    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaAgregado = now;
        this.fechaUltimaModificacion = now;
        // Asegurarse de que el ID compuesto se inicialice si no lo fue en el constructor
        if (this.id == null && this.cliente != null && this.producto != null) {
            this.id = new CarritoItemId(this.cliente.getUsuarioId(), this.producto.getProductoId());
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    // Getters y Setters
    public CarritoItemId getId() {
        return id;
    }

    public void setId(CarritoItemId id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null && this.producto != null) { // Actualiza el ID compuesto si se cambia el cliente
            this.id = new CarritoItemId(cliente.getUsuarioId(), this.producto.getProductoId());
        }
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null && this.cliente != null) { // Actualiza el ID compuesto si se cambia el producto
            this.id = new CarritoItemId(this.cliente.getUsuarioId(), producto.getProductoId());
        }
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioAlAgregar() {
        return precioAlAgregar;
    }

    public void setPrecioAlAgregar(BigDecimal precioAlAgregar) {
        this.precioAlAgregar = precioAlAgregar;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    // equals y hashCode para entidades con claves compuestas
    // Se basan en el objeto CarritoItemId id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarritoItem that = (CarritoItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarritoItem{" +
                "clienteId=" + (cliente != null ? cliente.getUsuarioId() : "null") +
                ", productoId=" + (producto != null ? producto.getProductoId() : "null") +
                ", cantidad=" + cantidad +
                ", precioAlAgregar=" + precioAlAgregar +
                '}';
    }
}