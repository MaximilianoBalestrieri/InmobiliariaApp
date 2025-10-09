package com.tec.inmobiliariaapp.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobiliariaapp.model.UsuarioResponse;


public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private MutableLiveData<UsuarioResponse> usuarioResponse;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository();
        usuarioResponse = new MutableLiveData<>();
    }

    public LiveData<UsuarioResponse> getusuarioResponse() {
        return usuarioResponse;
    }

    public void login(String username, String password) {
        repository.login(username, password).observeForever(response -> {
            usuarioResponse.setValue(response);
        });
    }
}