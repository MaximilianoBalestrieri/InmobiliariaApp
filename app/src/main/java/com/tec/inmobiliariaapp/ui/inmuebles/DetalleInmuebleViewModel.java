package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class DetalleInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Inmueble> inmuebleMutable;
    private final MutableLiveData<String> mensajeMutable; // Para mensajes de éxito/error

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        inmuebleMutable = new MutableLiveData<>();
        mensajeMutable = new MutableLiveData<>();
    }

    public LiveData<Inmueble> getInmueble() {
        return inmuebleMutable;
    }

    public LiveData<String> getMensaje() {
        return mensajeMutable;
    }

    public void recuperarInmueble(@NonNull Bundle bundle) {
        Inmueble inmueble = (Inmueble) bundle.getSerializable("inmueble");
        inmuebleMutable.setValue(inmueble);
    }

    // Método para actualizar un inmueble via API
    public void actualizarInmueble(Inmueble inmueble) {
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        String token = ApiClient.leerToken(getApplication()); // Recupera el token guardado

        // Llamada al endpoint de actualizar inmueble (supongamos que es tipo PUT)
        Call<Inmueble> call = api.actualizarInmueble(token, inmueble.getIdInmueble(), inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizamos el LiveData con la info nueva
                    inmuebleMutable.setValue(response.body());
                    mensajeMutable.setValue("Inmueble actualizado correctamente");
                } else {
                    mensajeMutable.setValue("Error al actualizar: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mensajeMutable.setValue("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}
