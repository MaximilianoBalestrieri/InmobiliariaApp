package com.tec.inmobiliariaapp.model;



import com.google.gson.annotations.SerializedName;

public class UsuarioResponse {
    @SerializedName("Token")
    private String token;

    private String mensaje;
    private Usuario usuario; // opcional según tu API

    public String getToken() { return token; }
    public String getMensaje() { return mensaje; }
    public Usuario getUsuario() { return usuario; }
}

