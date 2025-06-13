package com.projectfinal.greenthumb_backend.dto;

public class MercadoPagoPreferenceResponseDTO {
    private String id; // ID de la preferencia de Mercado Pago
    private String initPoint; // URL para redirigir al usuario
    private String sandboxInitPoint; // URL de sandbox para pruebas
    private String preferenceId;


    // Constructor
    public MercadoPagoPreferenceResponseDTO(String id, String initPoint, String sandboxInitPoint) {
        this.id = id;
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
    }

    public MercadoPagoPreferenceResponseDTO(String id) {
        this.id = id;
    }

    // Getters
    public String getId() { return id; }
    public String getInitPoint() { return initPoint; }
    public String getSandboxInitPoint() { return sandboxInitPoint; }

    // Setters (opcional, si se necesita modificar después de la creación)
    public void setId(String id) { this.id = id; }
    public void setInitPoint(String initPoint) { this.initPoint = initPoint; }
    public void setSandboxInitPoint(String sandboxInitPoint) { this.sandboxInitPoint = sandboxInitPoint; }
    public String getPreferenceId() { return preferenceId; }
    public void setPreferenceId(String preferenceId) { this.preferenceId = preferenceId; }
}