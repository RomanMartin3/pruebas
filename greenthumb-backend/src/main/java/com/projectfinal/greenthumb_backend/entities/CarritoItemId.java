// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/entities/CarritoItemId.java
package com.projectfinal.greenthumb_backend.entities;
import jakarta.persistence.Embeddable; // Importar Embeddable

import java.io.Serializable;
import java.util.Objects;
@Embeddable // Añadir esta anotación
public class CarritoItemId implements Serializable {
    private Integer clienteId; // Cambiado a clienteId para consistencia
    private Integer productoId; // Cambiado a productoId para consistencia

    // Constructor
    public CarritoItemId() {
    }

    public CarritoItemId(Integer clienteId, Integer productoId) {
        this.clienteId = clienteId;
        this.productoId = productoId;
    }

    // Getters
    public Integer getClienteId() { // Cambiado a getClienteId
        return clienteId;
    }
    public void setClienteId(Integer clienteId) { // Cambiado a setClienteId
        this.clienteId = clienteId;
    }

    public Integer getProductoId() { // Cambiado a getProductoId
        return productoId;
    }
    public void setProductoId(Integer productoId) { // Cambiado a setProductoId
        this.productoId = productoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarritoItemId that = (CarritoItemId) o;
        return Objects.equals(clienteId, that.clienteId) &&
                Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clienteId, productoId);
    }
}