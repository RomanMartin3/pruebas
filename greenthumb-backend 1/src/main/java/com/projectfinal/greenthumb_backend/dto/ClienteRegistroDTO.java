package com.projectfinal.greenthumb_backend.dto;

public class ClienteRegistroDTO {
    // Nuevos campos que vienen de Auth0
    private String auth0Id;
    private String email;
    private String nombre;
    private String apellido;

    // Campos que vienen del formulario
    private String calle;
    private String numero;
    private String codigoPostal;
    private String ciudad;
    private String provincia;
    private String telefono;

    // Constructor vacío
    public ClienteRegistroDTO() {}

    // Getters y Setters para todos los campos
    public String getAuth0Id() { return auth0Id; }
    public void setAuth0Id(String auth0Id) { this.auth0Id = auth0Id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}