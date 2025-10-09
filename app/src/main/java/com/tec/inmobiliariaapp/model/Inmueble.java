package com.tec.inmobiliariaapp.model;

import java.io.Serializable;

public class Inmueble implements Serializable {

        private int idInmueble;
        private int ambientes;
        private String direccion;
        private double precio;
        private String uso;
        private boolean disponible;
        private String tipo;
        private int imagen;

    public Inmueble(int idInmueble, int ambientes, String direccion, double precio, String uso, boolean disponible, String tipo, int imagen) {
        this.idInmueble = idInmueble;
        this.ambientes = ambientes;
        this.direccion = direccion;
        this.precio = precio;
        this.uso = uso;
        this.disponible = disponible;
        this.tipo = tipo;
        this.imagen = imagen;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
