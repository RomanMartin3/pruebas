package com.projectfinal.greenthumb_backend.listeners;

import com.projectfinal.greenthumb_backend.entities.Usuario;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;

public class UsuarioEntityListener {
    @PrePersist
    public void prePersist(Usuario usuario) {
        if (usuario.getFechaAlta() == null) {
            usuario.setFechaAlta(LocalDateTime.now());
        }
    }
}