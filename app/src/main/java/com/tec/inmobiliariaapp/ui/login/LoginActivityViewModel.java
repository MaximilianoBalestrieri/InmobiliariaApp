package com.tec.inmobiliariaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Void> mSolicitarPermisoLlamada;
    private MutableLiveData<Boolean> mLlamadaIniciada;


    public LiveData<String> getMMensaje(){
        return mMensaje;
    }

    public LiveData<Void> getMSolicitarPermisoLlamada() {
        if (mSolicitarPermisoLlamada == null) {
            mSolicitarPermisoLlamada = new MutableLiveData<>();
        }
        return mSolicitarPermisoLlamada;
    }

    public LiveData<Boolean> getMLlamadaIniciada() {
        if (mLlamadaIniciada == null) {
            mLlamadaIniciada = new MutableLiveData<>();
        }
        return mLlamadaIniciada;
    }


    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mMensaje = new MutableLiveData<>();
        mSolicitarPermisoLlamada = new MutableLiveData<>();
        mLlamadaIniciada = new MutableLiveData<>();
    }

    /**
     * Intenta iniciar una llamada al número de la inmobiliaria.
     * Si no tiene el permiso, notifica a la Actividad para que lo solicite.
     */
    public void llamarInmobiliaria(String numeroTelefono) {
        Context context = getApplication().getApplicationContext();

        // 1. Verificar si ya tenemos el permiso
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Si NO tenemos permiso, indicamos a la Actividad que lo solicite.
            mSolicitarPermisoLlamada.setValue(null);
            return;
        }

        // 2. Si tenemos el permiso, procedemos a hacer la llamada
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + numeroTelefono));
            // Necesario cuando se llama a startActivity desde un Context de Aplicación
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            // Notificar que la llamada fue iniciada (para el Toast)
            mLlamadaIniciada.setValue(true);

        } catch (Exception e) {
            mMensaje.setValue("Error al intentar llamar: Asegúrate de tener un dispositivo/emulador con soporte de llamadas.");
            Log.e("Llamada", "Error al iniciar la llamada", e);
            mLlamadaIniciada.setValue(false);
        }
    }

    // El método logueo se mantiene para la funcionalidad original
    public void logueo(String usuario, String contrasenia){
        if (usuario.isEmpty() || contrasenia.isEmpty()){
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
                                         .putString("email", usuario)
                                         .apply();

                                 Intent intent = new Intent(getApplication(), MainActivity.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 getApplication().startActivity(intent);
                             }else {
                                 mMensaje.setValue("Credenciales incorrectas o error en el servidor.");

                                 Log.d("token", response.message());
                                 Log.d("token", response.code()+ "");
                                 Log.d("token", response.errorBody()+"");
                             }
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             mMensaje.setValue("Error de conexión: " + t.getMessage());
                             Log.d("token", t.getMessage());
                         }
                     }
        );

    }
}