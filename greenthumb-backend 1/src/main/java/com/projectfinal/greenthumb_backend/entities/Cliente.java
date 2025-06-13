package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.projectfinal.greenthumb_backend.listeners.ClienteEntityListener;
import java.util.Set;
import java.util.HashSet;


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

    // Getters y Setters (Corregidos para seguir la convención de JavaBeans)
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

    public Set<CarritoItem> getCarritoItems() {
        return carritoItems;
    }

    public void setCarritoItems(Set<CarritoItem> carritoItems) { // Nombre del setter corregido
        this.carritoItems = carritoItems;
    }

    // Métodos de conveniencia opcionales para CarritoItem (altamente recomendados)
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