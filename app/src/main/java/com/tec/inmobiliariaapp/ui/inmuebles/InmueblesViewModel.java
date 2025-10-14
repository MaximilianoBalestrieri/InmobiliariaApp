package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.data.repository.InmuebleRepository;
import com.tec.inmobiliariaapp.model.Inmueble;

import java.util.List;

public class InmueblesViewModel extends AndroidViewModel {

    private final InmuebleRepository repository;
    private LiveData<List<Inmueble>> listaInmueblesLiveData; // LiveData que observará la UI

    public InmueblesViewModel(Application application) {
        super(application);

        // 1. Inicializar el repositorio con el Context de la aplicación.
        repository = new InmuebleRepository(application.getApplicationContext());

        // 2. Iniciar la carga de los inmuebles automáticamente al crear el ViewModel.
        listaInmueblesLiveData = repository.obtenerInmuebles();
    }

    /**
     * Expone la lista de inmuebles a la UI (Fragment).
     * El fragment observará este LiveData para actualizar el RecyclerView.
     */
    public LiveData<List<Inmueble>> getInmuebles() {
        return listaInmueblesLiveData;
    }

    /**
     * Método para recargar la lista de inmuebles, útil al volver al fragment
     * o después de realizar una acción como agregar/editar un inmueble.
     */
    public void recargarInmuebles() {
        // Al llamar a obtenerInmuebles(), el repositorio hace la llamada GET a la API
        // y actualiza este LiveData.
        listaInmueblesLiveData = repository.obtenerInmuebles();
    }
}