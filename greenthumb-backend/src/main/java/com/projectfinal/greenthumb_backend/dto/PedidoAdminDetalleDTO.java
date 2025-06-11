package com.projectfinal.greenthumb_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoAdminDetalleDTO {
    private Integer pedidoId;
    private LocalDateTime fechaPedido;
    private String estadoActual;

    private Integer clienteId;
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String direccionCliente;

    private String metodoPago;
    private String notasCliente;
    private String notasAdmin;

    private List<DetallePedidoDTO> detalles;
    private List<HistorialEstadoDTO> historialEstados;

    // Getters y Setters
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
    public String getNotasAdmin() { return notasAdmin; }
    public void setNotasAdmin(String notasAdmin) { this.notasAdmin = notasAdmin; }
    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
    public List<HistorialEstadoDTO> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoDTO> historialEstados) { this.historialEstados = historialEstados; }
}