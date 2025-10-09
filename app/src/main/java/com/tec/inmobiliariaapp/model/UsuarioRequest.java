package com.tec.inmobiliariaapp.model;

public class UsuarioRequest {
    private String username;
    private String password;

    public UsuarioRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Getters si tu API los necesita
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

