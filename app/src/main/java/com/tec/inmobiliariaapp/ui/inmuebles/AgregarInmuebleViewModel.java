package com.tec.inmobiliariaapp.ui.inmuebles;

import androidx.lifecycle.ViewModel;
import com.tec.inmobiliariaapp.model.Inmueble;
import java.util.ArrayList;
import java.util.List;

public class AgregarInmuebleViewModel extends ViewModel {

    private static final List<Inmueble> listaInmuebles = new ArrayList<>();

    public void agregarInmueble(Inmueble inmueble) {
        listaInmuebles.add(inmueble);
    }

    public List<Inmueble> obtenerInmuebles() {
        return listaInmuebles;
    }
}
