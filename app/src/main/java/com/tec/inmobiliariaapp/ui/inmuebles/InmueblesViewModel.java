package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.model.Propietario;
import com.tec.inmobiliariaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> listaInmuebles = new MutableLiveData<>();
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Inmueble>> getlistaInmuebles(){
        return listaInmuebles;
    }

    public void obtenerListaInmuebles(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call <List<Inmueble>> call = api.getInmueble("Bearer "+ token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()){
                    listaInmuebles.postValue(response.body());
                }else {
                    Toast.makeText(getApplication(),"no se obtuvieron Inmuebles",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                Log.d("errorInmueble",throwable.getMessage());

                Toast.makeText(getApplication(),"Error al obtener Inmuebles",Toast.LENGTH_LONG).show();
            }
        });
    }


}