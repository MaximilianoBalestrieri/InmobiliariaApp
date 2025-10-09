package com.tec.inmobiliariaapp.model;

public class Usuario {
    private String email;
    private String password;
    private String nombre;

    public Usuario(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
}
