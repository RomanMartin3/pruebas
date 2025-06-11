package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "RegistrosBaja")
public class RegistroBaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BajaID")
    private Integer bajaId;

    @Column(name = "NombreTabla", nullable = false, length = 100)
    private String nombreTabla;

    @Column(name = "RegistroID", nullable = false)
    private Integer registroId;

    @Column(name = "FechaBaja", nullable = false, updatable = false)
    private LocalDateTime fechaBaja;

    @Column(name = "MotivoBaja", columnDefinition = "TEXT")
    private String motivoBaja; // Puede ser nullable = true si el motivo es opcional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin") // Nullable si el sistema hace la baja
    private Administrador administrador;

    // Constructores
    public RegistroBaja() {
    }

    public RegistroBaja(String nombreTabla, Integer registroId, String motivoBaja, Administrador administrador) {
        this.nombreTabla = nombreTabla;
        this.registroId = registroId;
        this.motivoBaja = motivoBaja;
        this.administrador = administrador;
    }

    @PrePersist
    protected void onPrePersist() {
        this.fechaBaja = LocalDateTime.now();
        // Si MotivoBaja es NOT NULL y puede estar vacío:
        // if (this.motivoBaja == null) {
        //     this.motivoBaja = "";
        // }
    }

    // Getters y Setters
    public Integer getBajaId() {
        return bajaId;
    }

    public void setBajaId(Integer bajaId) {
        this.bajaId = bajaId;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public Integer getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Integer registroId) {
        this.registroId = registroId;
    }

    public LocalDateTime getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDateTime fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistroBaja)) return false;
        RegistroBaja that = (RegistroBaja) o;
        return Objects.equals(getBajaId(), that.getBajaId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBajaId());
    }

    @Override
    public String toString() {
        return "RegistroBaja{" +
                "bajaId=" + bajaId +
                ", nombreTabla='" + nombreTabla + '\'' +
                ", registroId=" + registroId +
                ", fechaBaja=" + fechaBaja +
                '}';
    }
}

