package com.tec.inmobiliariaapp.data.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobiliariaapp.model.Usuario;
import com.tec.inmobiliariaapp.model.UsuarioRequest;
import com.tec.inmobiliariaapp.model.UsuarioResponse;
import com.tec.inmobiliariaapp.request.ApiClient;
import com.tec.inmobiliariaapp.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginRepository {
    private ApiService apiService;

    public LoginRepository() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<UsuarioResponse> login(String username, String password) {
        MutableLiveData<UsuarioResponse> data = new MutableLiveData<>();
        UsuarioRequest request = new UsuarioRequest(username, password);

        apiService.login(request).enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
