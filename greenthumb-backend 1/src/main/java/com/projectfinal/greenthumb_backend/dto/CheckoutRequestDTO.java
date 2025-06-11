package com.projectfinal.greenthumb_backend.dto;

public class CheckoutRequestDTO {
    private Integer clienteId;
    private String metodoPago;
    private String notasCliente;

    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    public String getNotasCliente() {
        return notasCliente;
    }
    public void setNotasCliente(String notasCliente) {
        this.notasCliente = notasCliente;
    }

    public Integer getClienteId() {return clienteId;}

    public void setClienteId(Integer clienteId) {this.clienteId = clienteId;}

    public CheckoutRequestDTO() {
    }

    public CheckoutRequestDTO(String metodoPago, String notasCliente) {
        this.metodoPago = metodoPago;
        this.notasCliente = notasCliente;
    }
}
