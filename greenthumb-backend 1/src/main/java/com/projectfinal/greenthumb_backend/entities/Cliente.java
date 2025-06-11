// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/entities/Cliente.java
package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.projectfinal.greenthumb_backend.listeners.ClienteEntityListener;
import java.util.Set; // Added import
import java.util.HashSet; // Added import


@Entity
@EntityListeners(ClienteEntityListener.class)
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "UsuarioID") // FK a usuarios.UsuarioID
public class Cliente extends Usuario {

    @Column(name = "Calle", nullable = false, length = 255)
    private String calle;

    @Column(name = "Numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "Ciudad", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "Provincia", nullable = false, length = 100)
    private String provincia;

    @Column(name = "CodigoPostal", nullable = false, length = 4)
    private String codigoPostal;

    @Column(name = "FechaUltimaModificacionPerfil", nullable = false)
    private LocalDateTime fechaUltimaModificacionPerfil;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    // Corrected to use Set<CarritoItem> and named carritoItems
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CarritoItem> carritoItems = new HashSet<>();


    // Constructores
    public Cliente() {
        super();
    }

    public Cliente(String nombre, String apellido, String email, String contrasena, String telefono, String calle, String numero, String ciudad, String provincia, String codigoPostal) {
        super(nombre, apellido, email, contrasena, telefono);
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
    }

    // Getters y Setters (como los tenías)
    public String getcalle() {
        return calle;
    }

    public void setcalle(String calle) {
        this.calle = calle;
    }

    public String getnumero() {
        return numero;
    }

    public void setnumero(String numero) {
        this.numero = numero;
    }

    public String getciudad() {
        return ciudad;
    }

    public void setciudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getprovincia() {
        return provincia;
    }

    public void setprovincia(String provincia) {
        this.provincia = provincia;
    }

    public String getcodigoPostal() {
        return codigoPostal;
    }

    public void setcodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public LocalDateTime getFechaUltimaModificacionPerfil() {
        return fechaUltimaModificacionPerfil;
    }

    public void setFechaUltimaModificacionPerfil(LocalDateTime fechaUltimaModificacionPerfil) {
        this.fechaUltimaModificacionPerfil = fechaUltimaModificacionPerfil;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Corrected getter and setter for carritoItems
    public Set<CarritoItem> getCarritoItems() {
        return carritoItems;
    }

    public void setItemsCarrito(Set<CarritoItem> carritoItems) { // Renamed from setItemsCarrito
        this.carritoItems = carritoItems;
    }

    // Optional convenience methods for CarritoItem (highly recommended)
    public void addCarritoItem(CarritoItem item) {
        this.carritoItems.add(item);
        item.setCliente(this);
    }

    public void removeCarritoItem(CarritoItem item) {
        this.carritoItems.remove(item);
        item.setCliente(null);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "usuarioId=" + getUsuarioId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", calle='" + calle + '\'' +
                '}';
    }
}