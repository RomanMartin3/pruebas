package com.projectfinal.greenthumb_backend.listeners;

import com.projectfinal.greenthumb_backend.entities.Cliente;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class ClienteEntityListener {
    @PrePersist
    public void prePersist(Cliente cliente) {
        cliente.setFechaUltimaModificacionPerfil(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Cliente cliente) {
        cliente.setFechaUltimaModificacionPerfil(LocalDateTime.now());
    }
}