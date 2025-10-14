package com.tec.inmobiliariaapp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.request.ApiService;
import com.tec.inmobiliariaapp.utils.FileUtils; // 💡 Necesaria para obtener la ruta del archivo
//import com.tec.inmobiliariaapp.utils.MimeTypeUtils; // 💡 Podrías necesitar una utilidad para MIME Types más adelante.

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleRepository {

    private final ApiService apiService;
    private final Context context;

    public InmuebleRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    private String getToken() {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return "Bearer " + token; // Importante el prefijo para la cabecera
    }

    // ----------------------------------------------------
    // METODO: OBTENER TODOS LOS INMUEBLES (GET)
    // ----------------------------------------------------
    public LiveData<List<Inmueble>> obtenerInmuebles() {
        final MutableLiveData<List<Inmueble>> data = new MutableLiveData<>();
        String token = getToken();

        apiService.obtenerInmuebles(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    Log.e("INMUEBLE_REPO", "Error al obtener inmuebles. Código: " + response.code() + ", Mensaje: " + response.message());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("INMUEBLE_REPO", "Fallo de red al obtener inmuebles: " + t.getMessage(), t);
                data.setValue(null);
            }
        });
        return data;
    }

    // ----------------------------------------------------
    // METODO: AGREGAR INMUEBLE (POST MULTIPART)
    // ----------------------------------------------------
    public LiveData<Boolean> agregarInmueble(Inmueble inmueble, Uri imagenUri) {
        final MutableLiveData<Boolean> resultado = new MutableLiveData<>();
        String token = getToken();

        // 1. Convertir la Uri a un archivo File y crear el Part Body de la imagen
        String path = FileUtils.getPath(context, imagenUri);
        if (path == null) {
            Log.e("INMUEBLE_REPO", "No se pudo obtener la ruta del archivo (Uri a Path fallido).");
            resultado.setValue(false);
            return resultado;
        }

        File file = new File(path);

        // El tipo de medio para archivos binarios (imagen)
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // Campo del archivo: Debe coincidir con el nombre esperado por la API (asumo 'imagenFile')
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagenFile", file.getName(), requestFile);


        // 2. Crear los Part Bodies para los datos textuales del inmueble

        // Se crea un RequestBody de tipo "text/plain" por cada campo del modelo.
        RequestBody direccionPart = RequestBody.create(MediaType.parse("text/plain"),
                inmueble.getDireccion() != null ? inmueble.getDireccion() : "");

        RequestBody valorPart = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(inmueble.getValor()));

        RequestBody ambientesPart = RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(inmueble.getAmbientes()));

        RequestBody usoPart = RequestBody.create(MediaType.parse("text/plain"),
                inmueble.getUso() != null ? inmueble.getUso() : "");

        RequestBody tipoPart = RequestBody.create(MediaType.parse("text/plain"),
                inmueble.getTipo() != null ? inmueble.getTipo() : "");
        // 🚨 Agregar aquí más campos RequestBody según el modelo Inmueble y la API.

        // 3. Llamar a la API
        apiService.crearInmueble(
                token,
                imagenPart,
                direccionPart,
                valorPart,
                ambientesPart,
                usoPart,
                tipoPart
                // Pasar los demás campos RequestBody
        ).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Log.d("INMUEBLE_REPO", "Inmueble creado con éxito: " + response.body().getIdInmueble());
                    resultado.setValue(true);
                } else {
                    Log.e("INMUEBLE_REPO", "Fallo al crear inmueble: " + response.code() + ", Body de error: " + response.errorBody().toString());
                    resultado.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e("INMUEBLE_REPO", "Error de red/conexión al crear inmueble.", t);
                resultado.setValue(false);
            }
        });

        return resultado;
    }
}