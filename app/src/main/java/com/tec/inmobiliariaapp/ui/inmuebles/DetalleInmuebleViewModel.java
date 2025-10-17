package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.request.ApiClient;

import okhttp3.ResponseBody;
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
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.leerToken(context); //

        if (token == null || token.isEmpty()) {
            mensajeMutable.setValue("Token no encontrado ");
            return;
        }

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<ResponseBody> call = api.actualizarInmueble("Bearer " + token, inmueble.getIdInmueble(), inmueble);
        Log.d("API_JSON", new Gson().toJson(inmueble));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mensajeMutable.setValue("Inmueble actualizado correctamente ️");
                    Log.e("API_SUCCESS", "Actualización exitosa");
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                        Log.e("API_ERROR", "Código: " + response.code() + " - " + errorBody);
                        mensajeMutable.setValue("Error al actualizar (" + response.code() + ")");
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo de error: " + e.getMessage());
                        mensajeMutable.setValue("Error inesperado al procesar la respuesta");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "Fallo la conexión: " + t.getMessage());
                mensajeMutable.setValue("No se pudo conectar con el servidor");
            }
        });


    }

}

