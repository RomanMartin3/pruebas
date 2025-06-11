package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import com.projectfinal.greenthumb_backend.listeners.AdministradorEntityListener;


@Entity
@EntityListeners(AdministradorEntityListener.class)
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "UsuarioID") // FK a usuarios.UsuarioID
public class Administrador extends Usuario {

    @Column(name = "NivelAcceso", nullable = false, length = 50)
    private String nivelAcceso;

    @Column(name = "FechaUltimoAcceso", nullable = false)
    private LocalDateTime fechaUltimoAcceso;

    // Constructores
    public Administrador() {
        super();
    }
    public Administrador(String nombre, String apellido, String email, String contrasena, String telefono,
                         String nivelAcceso) {
        super(nombre, apellido, email, contrasena, telefono);
        this.nivelAcceso = nivelAcceso;
    }
    // Getters y Setters (como los tenías)
    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "usuarioId=" + getUsuarioId() +
                ", nombre='" + getNombre() + '\'' +
                ", nivelAcceso='" + nivelAcceso + '\'' +
                ", fechaUltimoAcceso=" + fechaUltimoAcceso +
                '}';
    }
}