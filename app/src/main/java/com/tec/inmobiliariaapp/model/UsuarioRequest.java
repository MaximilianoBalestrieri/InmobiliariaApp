package com.tec.inmobiliariaapp.model;

import com.google.gson.annotations.SerializedName; // 👈 Importa esto

public class UsuarioRequest {

    // Indica que "username" debe serializarse como "Usuario" en el JSON
    @SerializedName("Usuario")
    private String username;

    // Indica que "password" debe serializarse como "Clave" en el JSON
    @SerializedName("Clave")
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