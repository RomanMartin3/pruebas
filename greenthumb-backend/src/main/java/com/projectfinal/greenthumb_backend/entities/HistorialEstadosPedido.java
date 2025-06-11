package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "HistorialEstadosPedido") // Nueva tabla
public class HistorialEstadosPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistorialID")
    private Integer historialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PedidoID", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EstadoPedidoID", nullable = false)
    private EstadosPedido estadoPedido; // Asumiendo que tienes la entidad EstadosPedido

    @Column(name = "FechaCambio", nullable = false, updatable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY) // Puede ser null si el cambio es automático por el sistema
    @JoinColumn(name = "UsuarioID_Admin")
    private Administrador administrador; // Quién realizó el cambio, si aplica

    @Column(name = "Notas", columnDefinition = "TEXT") // Notas adicionales sobre el cambio de estado
    private String notas; // Puede ser null si no hay notas

    // Constructores
    public HistorialEstadosPedido() {
    }

    public HistorialEstadosPedido(Pedido pedido, EstadosPedido estadoPedido, Administrador administrador, String notas) {
        this.pedido = pedido;
        this.estadoPedido = estadoPedido;
        this.administrador = administrador;
        this.notas = notas;
    }

    @PrePersist
    protected void onPrePersist() {
        this.fechaCambio = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getHistorialId() {
        return historialId;
    }

    public void setHistorialId(Integer historialId) {
        this.historialId = historialId;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public EstadosPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadosPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialEstadosPedido that = (HistorialEstadosPedido) o;
        return Objects.equals(historialId, that.historialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historialId);
    }

    @Override
    public String toString() {
        return "HistorialEstadosPedido{" +
                "historialId=" + historialId +
                ", pedidoId=" + (pedido != null ? pedido.getPedidoId() : "null") +
                ", estadoPedido=" + (estadoPedido != null ? estadoPedido.getNombreEstado() : "null") + // Asumiendo getNombreEstado() en EstadosPedido
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
