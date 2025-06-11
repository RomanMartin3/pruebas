package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PedidoID")
    private Integer pedidoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "FechaPedido", nullable = false, updatable = false)
    private LocalDateTime fechaPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EstadoPedidoID",referencedColumnName = "EstadoPedidoID" ,nullable = false)
    private EstadosPedido estadoPedido; // Asumir entidad EstadosPedido

    @Column(name = "MetodoPagoSimulado", nullable = false, length = 50)
    private String metodoPagoSimulado;

    @Column(name = "NotasCliente", nullable = false, columnDefinition = "TEXT")
    private String notasCliente;

    @Column(name = "NotasAdmin", nullable = false, columnDefinition = "TEXT")
    private String notasAdmin;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetallesPedido> detalles = new ArrayList<>();

    // Si se implementa HistorialEstadosPedido:
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistorialEstadosPedido> historialEstados = new ArrayList<>();

    // Constructores, Getters, Setters, @PrePersist, equals, hashCode, toString...
    public Pedido() {
        // inicializar listas si es necesario
        this.detalles = new ArrayList<>();
        this.historialEstados = new ArrayList<>();
    }

    @PrePersist
    protected void onPrePersist() {
        this.fechaPedido = LocalDateTime.now();
        if (this.notasCliente == null) this.notasCliente = "";
        if (this.notasAdmin == null) this.notasAdmin = "";
        // Lógica para estado inicial si es necesario, o se setea desde el servicio.
    }

    // Getters y Setters para todos los campos...
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public EstadosPedido getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(EstadosPedido estadoPedido) { this.estadoPedido = estadoPedido; }
    public String getMetodoPagoSimulado() { return metodoPagoSimulado; }
    public void setMetodoPagoSimulado(String metodoPagoSimulado) { this.metodoPagoSimulado = metodoPagoSimulado; }
    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
    public String getNotasAdmin() { return notasAdmin; }
    public void setNotasAdmin(String notasAdmin) { this.notasAdmin = notasAdmin; }
    public List<DetallesPedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallesPedido> detalles) { this.detalles = detalles; }
    public List<HistorialEstadosPedido> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadosPedido> historialEstados) { this.historialEstados = historialEstados; }

    // Helper methods para la colección de detalles
    public void addDetalle(DetallesPedido detalle) {
        this.detalles.add(detalle);
        detalle.setPedido(this);
    }

    public void removeDetalle(DetallesPedido detalle) {
        this.detalles.remove(detalle);
        detalle.setPedido(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(pedidoId, pedido.pedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId);
    }
}
