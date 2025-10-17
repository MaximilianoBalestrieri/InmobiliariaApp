package com.tec.inmobiliariaapp.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    // La variable que mantiene el estado del mensaje (MutableLiveData para poder cambiar el valor internamente)
    private MutableLiveData<String> mMensaje;

    // --- INICIO DE LA CORRECCIÓN ---

    // Línea 22 corregida: Devuelve la variable mMensaje, no se llama a sí mismo.
    public LiveData<String> getMMensaje(){
        return mMensaje;
    }

    // --- FIN DE LA CORRECCIÓN ---

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        // Es crucial inicializar la variable aquí para evitar NullPointerException.
        mMensaje = new MutableLiveData<>();
    }

    public void logueo(String usuario, String contrasenia){
        if (usuario.isEmpty() || contrasenia.isEmpty()){
            // Aquí ya no habrá NullPointerException porque mMensaje ya está inicializado.
            mMensaje.setValue("Error, campos vacíos");
            return;
        }

        ApiClient.InmoServicio inmoServicio=ApiClient.getInmoServicio();
        Call<String> call= inmoServicio.loginForm(usuario, contrasenia);
        call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(Call<String> call, Response<String> response) {
                             if (response.isSuccessful()){
                                 String token= response.body();
                                 ApiClient.guardarToken(getApplication(),token);
                                Log.d("token", token);

                                 //  Guardar el email del usuario logueado
                                 getApplication()
                                         .getSharedPreferences("MisPreferencias", Application.MODE_PRIVATE)
                                         .edit()
                                         .putString("email", usuario) // el "usuario" que ingresó
                                         .apply();

                                 Intent intent = new Intent(getApplication(), MainActivity.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 getApplication().startActivity(intent);
                             }else {
                                 // Si el login falla por credenciales o error de servidor, actualiza el mensaje.
                                 mMensaje.setValue("Credenciales incorrectas o error en el servidor.");

                                 Log.d("token", response.message());
                                 Log.d("token", response.code()+ "");
                                 Log.d("token", response.errorBody()+"");
                             }
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             // Si hay un fallo de red o conexión, actualiza el mensaje.
                             mMensaje.setValue("Error de conexión: " + t.getMessage());
                             Log.d("token", t.getMessage());
                         }
                     }
        );

    }
}