package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "estadospedido")
public class EstadosPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EstadoPedidoID")
    private Integer estadoPedidoId;

    @Column(name = "NombreEstado", nullable = false, unique = true, length = 50)
    private String nombreEstado;

    @Column(name = "DescripcionEstado", nullable = false, columnDefinition = "TEXT")
    private String descripcionEstado;

    @Column(name = "EsEstadoFinal", nullable = false) // Original: TINYINT(1) DEFAULT 0
    private boolean esEstadoFinal;

    @OneToMany(mappedBy = "estadoPedido", fetch = FetchType.LAZY)
    private List<Pedido> pedidosConEsteEstado = new ArrayList<>();

    @OneToMany(mappedBy = "estadoPedido", fetch = FetchType.LAZY)
    private List<HistorialEstadosPedido> historialesConEsteEstado = new ArrayList<>();


    // Constructores
    public EstadosPedido() {
    }

    public EstadosPedido(String nombreEstado, String descripcionEstado, boolean esEstadoFinal) {
        this.nombreEstado = nombreEstado;
        this.descripcionEstado = descripcionEstado;
        this.esEstadoFinal = esEstadoFinal;
    }

    // Getters y Setters
    public Integer getEstadoPedidoId() {
        return estadoPedidoId;
    }

    public void setEstadoPedidoId(Integer estadoPedidoId) {
        this.estadoPedidoId = estadoPedidoId;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public boolean isEsEstadoFinal() {
        return esEstadoFinal;
    }

    public void setEsEstadoFinal(boolean esEstadoFinal) {
        this.esEstadoFinal = esEstadoFinal;
    }

    public List<Pedido> getPedidosConEsteEstado() {
        return pedidosConEsteEstado;
    }

    public void setPedidosConEsteEstado(List<Pedido> pedidosConEsteEstado) {
        this.pedidosConEsteEstado = pedidosConEsteEstado;
    }

    public List<HistorialEstadosPedido> getHistorialesConEsteEstado() {
        return historialesConEsteEstado;
    }

    public void setHistorialesConEsteEstado(List<HistorialEstadosPedido> historialesConEsteEstado) {
        this.historialesConEsteEstado = historialesConEsteEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadosPedido that = (EstadosPedido) o;
        return Objects.equals(estadoPedidoId, that.estadoPedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estadoPedidoId);
    }

    @Override
    public String toString() {
        return "EstadosPedido{" +
                "estadoPedidoId=" + estadoPedidoId +
                ", nombreEstado='" + nombreEstado + '\'' +
                '}';
    }
}