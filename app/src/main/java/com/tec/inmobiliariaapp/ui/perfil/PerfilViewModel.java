package com.tec.inmobiliariaapp.ui.perfil;

import android.app.Application; // Necesario para obtener el Context
import androidx.lifecycle.AndroidViewModel; // Usar AndroidViewModel para el Context
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.data.repository.PropietarioRepository;
import com.tec.inmobiliariaapp.model.Propietario;

// Cambiamos a AndroidViewModel para tener acceso al Context
public class PerfilViewModel extends AndroidViewModel {

    private final PropietarioRepository repository;
    private LiveData<Propietario> propietarioLiveData;
    private final MutableLiveData<Boolean> actualizacionExitosa = new MutableLiveData<>();

    public PerfilViewModel(Application application) {
        super(application);
        // Inicializar el repositorio con el Context
        repository = new PropietarioRepository(application.getApplicationContext());

        // Cargar el perfil automáticamente al iniciar el ViewModel
        propietarioLiveData = repository.getPropietario();
    }

    // Exponer el LiveData del propietario que viene del Repository
    public LiveData<Propietario> getPropietario() {
        return propietarioLiveData;
    }

    // Exponer el LiveData del resultado de la actualización
    public LiveData<Boolean> getActualizacionExitosa() {
        return actualizacionExitosa;
    }

    // Método para recargar el perfil (útil después de actualizar)
    public void cargarPropietario() {
        propietarioLiveData = repository.getPropietario();
    }

    // Método para la actualización que llama al Repository
    public void actualizarPropietario(Propietario p) {
        // Llamar al repositorio y observar el resultado
        repository.actualizarPropietario(p).observeForever(exito -> {
            actualizacionExitosa.setValue(exito);

            // Si la actualización fue exitosa, recargar los datos para actualizar la UI
            if (exito) {
                cargarPropietario();
            }
        });
    }
}