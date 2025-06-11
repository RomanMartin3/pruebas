package com.projectfinal.greenthumb_backend.listeners;

import com.projectfinal.greenthumb_backend.entities.Administrador;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AdministradorEntityListener {
    @PrePersist
    public void prePersist(Administrador admin) {
        admin.setFechaUltimoAcceso(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Administrador admin) {
        admin.setFechaUltimoAcceso(LocalDateTime.now());
    }
}