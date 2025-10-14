package com.tec.inmobiliariaapp.data.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.model.Propietario;
import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropietarioRepository {
    private final ApiService apiService;
    private final Context context;

    public PropietarioRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    private String getToken() {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return "Bearer " + token; // Agregar prefijo "Bearer "
    }

    // ----------------------------------------------------
    // METODO 1: OBTENER PROPIETARIO (GET)
    // ----------------------------------------------------
    public LiveData<Propietario> getPropietario() {
        MutableLiveData<Propietario> data = new MutableLiveData<>();
        String token = getToken();

        apiService.getPropietarios(token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    Log.e("PROPIETARIO_REPO", "Error al obtener perfil: " + response.code());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PROPIETARIO_REPO", "Fallo de red al obtener perfil", t);
                data.setValue(null);
            }
        });
        return data;
    }

    // ----------------------------------------------------
    // METODO 2: ACTUALIZAR PROPIETARIO (PUT)
    // ----------------------------------------------------
    public LiveData<Boolean> actualizarPropietario(Propietario propietario) {
        MutableLiveData<Boolean> resultado = new MutableLiveData<>();
        String token = getToken();

        apiService.actualizarPropietario(token, propietario).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    // La API puede devolver el Propietario actualizado o un 204 No Content
                    resultado.setValue(true);
                } else {
                    Log.e("PROPIETARIO_REPO", "Error al actualizar: " + response.code() + " - " + response.message());
                    resultado.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PROPIETARIO_REPO", "Fallo de red al actualizar", t);
                resultado.setValue(false);
            }
        });
        return resultado;
    }
}