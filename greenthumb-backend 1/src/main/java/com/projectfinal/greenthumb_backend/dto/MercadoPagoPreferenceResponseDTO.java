// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/dto/MercadoPagoPreferenceResponseDTO.java
package com.projectfinal.greenthumb_backend.dto;

public class MercadoPagoPreferenceResponseDTO {
    private String id; // ID de la preferencia de Mercado Pago
    private String initPoint; // URL para redirigir al usuario
    private String sandboxInitPoint; // URL de sandbox para pruebas

    // Constructor
    public MercadoPagoPreferenceResponseDTO(String id, String initPoint, String sandboxInitPoint) {
        this.id = id;
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
    }

    // Getters
    public String getId() { return id; }
    public String getInitPoint() { return initPoint; }
    public String getSandboxInitPoint() { return sandboxInitPoint; }

    // Setters (opcional, si se necesita modificar después de la creación)
    public void setId(String id) { this.id = id; }
    public void setInitPoint(String initPoint) { this.initPoint = initPoint; }
    public void setSandboxInitPoint(String sandboxInitPoint) { this.sandboxInitPoint = sandboxInitPoint; }
}