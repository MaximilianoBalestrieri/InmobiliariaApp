package com.tec.inmobiliariaapp.data.repository;

import android.content.Context; // Necesario para SharedPreferences
import android.content.SharedPreferences; // Necesario para guardar
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log; // Para nuestro DEBUG

import com.tec.inmobiliariaapp.model.UsuarioResponse;
import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private ApiService apiService;
    // 💡 NECESARIO: Guardar el Context para SharedPreferences
    private Context context;

    // Modificamos el constructor para recibir el Context (si es posible)
    // Sino, usa un método setContext(Context context)
    public LoginRepository(Context context) {
        this.context = context;
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    // Método privado para guardar el token
    private void guardarToken(String token, String email) {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("token", token)
                .putString("email", email) // 👈 ¡Debe ser "email"!
                .apply();
    }

    public LiveData<UsuarioResponse> login(String username, String password) {
        MutableLiveData<UsuarioResponse> data = new MutableLiveData<>();

        apiService.login(username, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    Log.d("TOKEN_DEBUG", "Token recibido y guardando: " + token); // Confirmación

                    // 🚨 CAMBIO CLAVE: GUARDAR EL TOKEN AQUÍ
                    guardarToken(token, username);

                    // Solo devolvemos el UsuarioResponse para notificar éxito (el token ya está guardado)
                    data.setValue(new UsuarioResponse(token));
                } else {
                    Log.e("TOKEN_DEBUG", "Fallo: Status=" + response.code() + ", Body=" + response.body());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TOKEN_DEBUG", "Error de red: " + t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
}