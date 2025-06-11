package com.projectfinal.greenthumb_backend.dto;

import java.util.List;

public class UsuarioDTO {
    private Integer id;
    private String email;
    private String nombre;
    private String apellido;
    private String auth0Id;
    private List<String> roles; // Campo para los roles
    private boolean registroCompleto; // Añadir este flag

    // Constructor vacío
    public UsuarioDTO() {}

    // Constructor con todos los campos
    public UsuarioDTO(Integer id, String email, String nombre, String apellido, String auth0Id, List<String> roles) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.auth0Id = auth0Id;
        this.roles = roles;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getAuth0Id() { return auth0Id; }
    public void setAuth0Id(String auth0Id) { this.auth0Id = auth0Id; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public boolean isRegistroCompleto() {return registroCompleto;}
    public void setRegistroCompleto(boolean registroCompleto) {this.registroCompleto = registroCompleto;}


}
