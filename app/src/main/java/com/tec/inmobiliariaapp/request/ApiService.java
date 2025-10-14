package com.tec.inmobiliariaapp.request;

import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.model.Propietario;
import com.tec.inmobiliariaapp.model.UsuarioRequest;
import com.tec.inmobiliariaapp.model.UsuarioResponse; // <--- Asegúrate de tener este modelo

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

// **********************************************
// Esta debe ser la interfaz que usa Retrofit.
// **********************************************
public interface ApiService {

   
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


    @Multipart
    @POST("api/Inmuebles/crear") // 🚨 CONFIRMA ESTA RUTA CON TU BACKEND (asumo 'crear')
    Call<Inmueble> crearInmueble(
            @Header("Authorization") String token,
            @Part MultipartBody.Part imagenFile, // La imagen binaria
            @Part("direccion") RequestBody direccion,
            @Part("valor") RequestBody valor,
            @Part("ambientes") RequestBody ambientes,
            @Part("uso") RequestBody uso,
            @Part("tipo") RequestBody tipo
            // Agrega aquí todas las demás propiedades del inmueble que debas enviar
    );
}