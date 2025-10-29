package com.tec.inmobiliariaapp.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.model.Propietario;
import com.tec.inmobiliariaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    // LiveData para manejar el estado de edición (true=editable, false=solo lectura)
    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    // LiveData para manejar el texto del botón (EDITAR/GUARDAR)
    private MutableLiveData<String> mNombreBoton = new MutableLiveData<>();
    // LiveData para almacenar y exponer el objeto Propietario
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        // Establecer estado inicial si es necesario (ej: no editable, botón dice "EDITAR")
        mEstado.setValue(false);
        mNombreBoton.setValue("EDITAR");
    }

    // ----------------------------------------------------------------------
    // GETTERS PARA EXPONER LOS LIVEDATA
    // ----------------------------------------------------------------------

    public LiveData<Boolean> getmEstado() {
        return mEstado;
    }

    public LiveData<String> getmNombreBoton() {
        return mNombreBoton; // Corregido: usa mNombreBoton
    }

    public LiveData<Propietario> getPropietario() {
        return mPropietario; // Nuevo: expone el LiveData del propietario
    }


    public void obtenerPerfil() {

        String token = ApiClient.leerToken(getApplication());

        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Token no disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<Propietario> call = api.obtenerPerfil("Bearer " + token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener perfil: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("PerfilViewModel", "Error en respuesta de obtenerPerfil: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                Log.e("PerfilViewModel", "Fallo de conexión obtenerPerfil: " + throwable.getMessage());
                Toast.makeText(getApplication(), "Error de red: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void cambioBoton(String nombreBoton, String nombre, String apellido, String dni, String telefono, String email) {

        // 1. Alternar estado (EDITAR -> GUARDAR)
        if (nombreBoton.equalsIgnoreCase("EDITAR")) {
            mEstado.setValue(true);
            mNombreBoton.setValue("GUARDAR");
        }
        // 2. Intentar guardar (GUARDAR -> EDITAR)
        else {
            mEstado.setValue(false);
            mNombreBoton.setValue("EDITAR");

            // VERIFICACIÓN: Nos aseguramos de tener datos actuales del propietario.
            Propietario propietarioActual = mPropietario.getValue();
            if (propietarioActual == null) {
                Toast.makeText(getApplication(), "No hay datos de propietario para actualizar.", Toast.LENGTH_SHORT).show();
                return;
            }
// ===== VALIDACIONES =====
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || dni.trim().isEmpty()
                    || telefono.trim().isEmpty() || email.trim().isEmpty()) {
                Toast.makeText(getApplication(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }

            // Validar DNI como número entero
            try {
                Integer.parseInt(dni);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplication(), "El DNI debe ser un número válido.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }

            // Validar formato de correo electrónico
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplication(), "El correo electrónico no tiene un formato válido.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }

            Propietario actualizado = new Propietario();
            // Mantenemos el ID original
            actualizado.setIdPropietario(propietarioActual.getIdPropietario());
            // Usamos los parámetros recibidos del Fragment para los nuevos valores
            actualizado.setNombre(nombre);
            actualizado.setApellido(apellido);
            actualizado.setDni(dni);
            actualizado.setTelefono(telefono);
            actualizado.setEmail(email);

            // LLAMADA A LA API PARA ACTUALIZAR
            String token = ApiClient.leerToken(getApplication());
            if (token == null || token.isEmpty()) {
                Toast.makeText(getApplication(), "Token no disponible.", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiClient.InmoServicio api = ApiClient.getInmoServicio();
            Call<Propietario> call = api.actualizarPropietario("Bearer " + token, actualizado);

            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Actualizamos el LiveData con los datos recién guardados por si hay cambios del servidor
                        mPropietario.postValue(response.body());
                        Toast.makeText(getApplication(), "ACTUALIZADO CON ÉXITO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show();
                        Log.e("PerfilViewModel", "Error al actualizar: " + response.toString());
                        // En caso de error, volvemos al estado editable para que el usuario pueda corregir
                        mEstado.postValue(true);
                        mNombreBoton.postValue("GUARDAR");
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.e("PerfilViewModel", "Fallo de conexión actualizarPropietario: " + t.getMessage());
                    Toast.makeText(getApplication(), "Error de red al actualizar", Toast.LENGTH_SHORT).show();
                    // En caso de error, volvemos al estado editable
                    mEstado.postValue(true);
                    mNombreBoton.postValue("GUARDAR");
                }
            });
        }
    }
}