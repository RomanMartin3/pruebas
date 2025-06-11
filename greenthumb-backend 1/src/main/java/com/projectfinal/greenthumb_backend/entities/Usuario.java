package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import com.projectfinal.greenthumb_backend.listeners.UsuarioEntityListener;


@Entity
@EntityListeners(UsuarioEntityListener.class)
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioID")
    private Integer usuarioId;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "Contrasena", nullable = false, length = 255) // Almacenar HASH
    private String contrasena;

    @Column(name = "Telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "FechaAlta", nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    // Constructores
    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String email, String contrasena, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    // Getters y Setters (como los tenías)
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getUsuarioId(), usuario.getUsuarioId()) ||
                (getUsuarioId() == null && Objects.equals(getEmail(), usuario.getEmail()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsuarioId(), getEmail());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}