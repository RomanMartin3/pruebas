package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detallespedido")
public class DetallesPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DetallePedidoID")
    private Integer detallePedidoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PedidoID", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;

    @Column(name = "CantidadComprada", nullable = false)
    private Integer cantidadComprada;

    @Column(name = "PrecioUnitarioAlComprar", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioAlComprar;

    // Constructores, Getters, Setters, equals, hashCode, toString...
    public DetallesPedido() {}

    public DetallesPedido(Pedido pedido, Producto producto, Integer cantidadComprada) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidadComprada = cantidadComprada;
    }

    // Getters y Setters
    public Integer getDetallePedidoId() { return detallePedidoId; }
    public void setDetallePedidoId(Integer detallePedidoId) { this.detallePedidoId = detallePedidoId; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidadComprada() { return cantidadComprada; }
    public void setCantidadComprada(Integer cantidadComprada) { this.cantidadComprada = cantidadComprada; }
    public BigDecimal getPrecioUnitarioAlComprar() {
        return precioUnitarioAlComprar;
    }

    public void setPrecioUnitarioAlComprar(BigDecimal precioUnitarioAlComprar) {
        this.precioUnitarioAlComprar = precioUnitarioAlComprar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesPedido that = (DetallesPedido) o;
        return Objects.equals(detallePedidoId, that.detallePedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detallePedidoId);
    }
}