package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Application;
import android.net.Uri; // Necesario para recibir la URI de la imagen
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

// 🚨 Asegúrate de tener esta dependencia o la ruta correcta a tu repositorio
import com.tec.inmobiliariaapp.data.repository.InmuebleRepository;
import com.tec.inmobiliariaapp.model.Inmueble;

public class AgregarInmuebleViewModel extends AndroidViewModel {

    private final InmuebleRepository repository;
    // LiveData para notificar a la UI si el guardado fue exitoso o falló (true/false)
    private final MutableLiveData<Boolean> resultadoGuardado = new MutableLiveData<>();

    public AgregarInmuebleViewModel(Application application) {
        super(application);

        // 1. Inicializar el repositorio con el Context de la aplicación
        repository = new InmuebleRepository(application.getApplicationContext());
    }

    /**
     * Expone el resultado de la operación de guardado a la UI (Fragment).
     */
    public LiveData<Boolean> getResultadoGuardado() {
        return resultadoGuardado;
    }

    /**
     * Inicia el proceso de guardar un nuevo inmueble en el servidor,
     * incluyendo la subida de la imagen.
     * * @param inmueble El objeto Inmueble con los datos del formulario.
     * @param imagenUri La URI local de la imagen seleccionada por el usuario.
     */
    public void guardarInmueble(Inmueble inmueble, Uri imagenUri) {

        // 🚨 Lógica futura: Llamarías al repositorio para hacer la llamada a la API

        /* repository.agregarInmueble(inmueble, imagenUri).observeForever(exito -> {
            resultadoGuardado.setValue(exito);
        });
        */

        // 💡 Por ahora, solo simulación de éxito para que el fragment funcione
        resultadoGuardado.setValue(true);
    }

    // El método 'obtenerInmuebles()' y la lista local ya no son necesarios
    // porque ahora usaremos el repositorio para interactuar con la API.
}