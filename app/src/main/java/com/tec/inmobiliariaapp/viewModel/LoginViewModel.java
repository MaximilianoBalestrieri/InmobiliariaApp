package com.tec.inmobiliariaapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobiliariaapp.data.repository.LoginRepository;
import com.tec.inmobiliariaapp.model.Usuario;
import com.tec.inmobiliariaapp.model.UsuarioResponse;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private MutableLiveData<UsuarioResponse> usuarioLiveData;

    public LoginViewModel(@NonNull Application application) {  // <- obligatorio
        super(application);
        repository = new LoginRepository();
        usuarioLiveData = new MutableLiveData<>();
    }

    public LiveData<UsuarioResponse> getLoginResponse() {
        return usuarioLiveData;
    }

    public void login(String username, String password) {
        repository.login(username, password).observeForever(response -> {
            usuarioLiveData.setValue(response);
        });
    }
}