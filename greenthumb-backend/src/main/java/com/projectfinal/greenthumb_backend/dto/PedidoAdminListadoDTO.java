package com.projectfinal.greenthumb_backend.dto;

import java.time.LocalDateTime;

public class PedidoAdminListadoDTO {
    private Integer pedidoId;
    private LocalDateTime fechaPedido;
    private String nombreCliente;
    private String estadoActual;
    private String metodoPago;

    // Constructor, Getters y Setters
    public PedidoAdminListadoDTO(Integer pedidoId, LocalDateTime fechaPedido, String nombreCliente, String estadoActual, String metodoPago) {
        this.pedidoId = pedidoId;
        this.fechaPedido = fechaPedido;
        this.nombreCliente = nombreCliente;
        this.estadoActual = estadoActual;
        this.metodoPago = metodoPago;
    }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}