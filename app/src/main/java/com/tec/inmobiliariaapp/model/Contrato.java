package com.tec.inmobiliariaapp.model;

import java.util.Date;

public class Contrato {
    public int idContrato;
    public Date fechaInicio;
    public Date fechaFinalizacion;
    public double montoAlquiler;
    public boolean estado;
    public int idInquilino;
    public int idInmueble;
    public Inquilino inquilino;
    public Inmueble inmueble;

    // agrego constructor vacio para que gson pueda deserializar correctamente
    public Contrato() {}
}