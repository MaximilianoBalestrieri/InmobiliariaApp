package com.tec.inmobiliariaapp.request;

import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.model.Propietario;
import com.tec.inmobiliariaapp.model.UsuarioRequest;
import com.tec.inmobiliariaapp.model.UsuarioResponse; // <--- Asegúrate de tener este modelo

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

// **********************************************
// Esta debe ser la interfaz que usa Retrofit.
// **********************************************
public interface ApiService {

    // 1. Método para el LOGIN (JSON Body)
    // El endpoint de tu API parece ser para Propietarios, vamos a usar el que espera un Body
    // si usamos UsuarioRequest.

    //@FormUrlEncoded
    //@POST("api/Propietarios/login")
    //Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

    // ESTO GENERA JSON Body (Content-Type: application/json)
    @FormUrlEncoded
    @POST("api/Propietarios/Login")
    Call<String> login(
            @Field("Usuario") String usuario, // Mayúsculas
            @Field("Clave") String clave      // Mayúsculas
    );

    @GET("api/Propietarios")
    Call<Propietario> getPropietarios(@Header("Authorization") String token);

    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);

    @PUT("api/Propietarios/actualizar")
    Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);


}