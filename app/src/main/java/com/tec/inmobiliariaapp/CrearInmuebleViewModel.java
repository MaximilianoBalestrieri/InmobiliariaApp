package com.tec.inmobiliariaapp;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tec.inmobiliariaapp.model.Inmueble;
import com.tec.inmobiliariaapp.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData;
    private MutableLiveData<Inmueble> mInmueble;
    private static Inmueble inmueblelleno;

    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
        inmueblelleno = new Inmueble();
    }

    // TODO: Implement the ViewModel
    public LiveData<Uri> getUriMutable() {
        if (uriMutableLiveData == null) {
            uriMutableLiveData = new MutableLiveData<>();
        }
        return uriMutableLiveData;

    }

    public LiveData<Inmueble> getmInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri uri = data.getData();
            Log.d("salada", uri.toString());
            uriMutableLiveData.setValue(uri);
        }
    }

    public void guardarInmueble(String direccion, String uso, String tipo, String precio, String ambientes, String superficie, boolean disponible) {
        try {
            int amb = Integer.parseInt(ambientes);
            int superf = Integer.parseInt(superficie);
            double prec = Double.parseDouble(precio);
            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setValor(prec);
            inmueble.setSuperficie(superf);
            inmueble.setAmbientes(amb);
            inmueble.setDisponible(disponible);
            // convertir la imagen en bits
            byte[] imagen=transformarImagen();
            if (imagen.length == 0){
                Toast.makeText(getApplication(), "Ustes debe ingresar una imagen", Toast.LENGTH_SHORT).show();
                return;
            }
            String inmuebleJson = new Gson().toJson(inmueble);
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);
            ApiClient.InmoServicio api = ApiClient.getInmoServicio();
            String token=ApiClient.leerToken(getApplication());
            Call<Inmueble> llamada=api.CargarInmueble("Bearer " + token, imagenPart, inmuebleBody);
            llamada.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplication(), "Inmueble guardado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Error al guardar inmueble", Toast.LENGTH_SHORT).show();
                }
            });



        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "Error, debe ingresar un numero", Toast.LENGTH_SHORT).show();

        }


    }

    private byte[] transformarImagen() {
        try {
            Uri uri = uriMutableLiveData.getValue();  //lo puedo usar porque estoy en viewmodel
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                FileNotFoundException er) {
            Toast.makeText(getApplication(), "No ha seleccinado una foto", Toast.LENGTH_SHORT).show();
            return new byte[]{};
        }

    }


}