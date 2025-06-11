// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/dto/MercadoPagoPreferenceRequestDTO.java
package com.projectfinal.greenthumb_backend.dto;

import java.util.List;
import java.math.BigDecimal;

public class MercadoPagoPreferenceRequestDTO {
    private Integer clienteId;
    private String metodoPago;
    private String notasCliente;
    private String successUrl;
    private String failureUrl;
    private String pendingUrl;

    // Representa los ítems del carrito que se enviarán a Mercado Pago
    public static class Item {
        private String id;
        private String title;
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    private List<Item> items;


    // Getters and Setters for main DTO
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
    public String getSuccessUrl() { return successUrl; }
    public void setSuccessUrl(String successUrl) { this.successUrl = successUrl; }
    public String getFailureUrl() { return failureUrl; }
    public void setFailureUrl(String failureUrl) { this.failureUrl = failureUrl; }
    public String getPendingUrl() { return pendingUrl; }
    public void setPendingUrl(String pendingUrl) { this.pendingUrl = pendingUrl; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}