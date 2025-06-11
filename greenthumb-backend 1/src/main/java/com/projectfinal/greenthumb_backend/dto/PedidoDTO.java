package com.projectfinal.greenthumb_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {
    private Integer pedidoId;
    private Integer clienteId;
    private String nombreCliente;
    private LocalDateTime fechaPedido;
    private String estadoPedido;
    private String metodoPagoSimulado;
    private String notasCliente;
    private List<DetallePedidoDTO> detalles; // Un nuevo DTO para los detalles del pedido

    // Constructor
    public PedidoDTO() {}

    public PedidoDTO(Integer pedidoId, Integer clienteId, String nombreCliente, LocalDateTime fechaPedido, String estadoPedido, String metodoPagoSimulado, String notasCliente, List<DetallePedidoDTO> detalles) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.metodoPagoSimulado = metodoPagoSimulado;
        this.notasCliente = notasCliente;
        this.detalles = detalles;
    }

    // Getters y Setters
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }
    public String getMetodoPagoSimulado() { return metodoPagoSimulado; }
    public void setMetodoPagoSimulado(String metodoPagoSimulado) { this.metodoPagoSimulado = metodoPagoSimulado; }
    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
}