package com.tec.inmobiliariaapp.ui.inicio;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends AndroidViewModel implements OnMapReadyCallback {

    // Se recomienda usar 'private final'
    private final LatLng SANLUIS = new LatLng(-33.280576, -66.332482);
    private final LatLng ULP = new LatLng(-33.150720, -66.306864);
    private GoogleMap mMap;

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. CORRECCIÓN PRINCIPAL: Usar MAP_TYPE_NORMAL para reducir la carga de CPU/GPU
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // 2. Agregar los dos marcadores (no son pesados)
        mMap.addMarker(new MarkerOptions().position(SANLUIS).title("San Luis"));
        mMap.addMarker(new MarkerOptions().position(ULP).title("ULP"));

        // 3. Mover la cámara (Zoom 10 es un buen nivel para ver ambos puntos)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SANLUIS, 10));
    }
}