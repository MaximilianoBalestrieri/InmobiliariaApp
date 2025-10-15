package com.tec.inmobiliariaapp.ui.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean>mEstado = new MutableLiveData<>();
    private MutableLiveData<String>mNombre=new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);

    }
    public LiveData<Boolean>getmEstado(){
        return mEstado;
    }
    public LiveData<String>getmNombre(){
        return mNombre;
    }
    public void cambioBoton(String nombreBoton){
        if(nombreBoton.equalsIgnoreCase("EDITAR")){
            mEstado.setValue(true);
            mNombre.setValue("GUARDAR");
        }else{
            mEstado.setValue(false);
            mNombre.setValue("EDITAR");

        }
    }
}