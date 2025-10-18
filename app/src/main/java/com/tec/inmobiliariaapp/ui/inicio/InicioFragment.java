package com.tec.inmobiliariaapp.ui.inicio;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment; // Importar SupportMapFragment
import com.tec.inmobiliariaapp.R;

public class InicioFragment extends Fragment {

    private InicioViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout que contiene el fragmento del mapa (fragment_inicio.xml)
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar el ViewModel
        viewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // 2. Obtener el SupportMapFragment usando getChildFragmentManager()
        // (Esto es CRUCIAL en un Fragmento)
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map); // Asegúrate de que el ID del mapa es 'map'

        // 3. Solicitar el mapa y pasar el ViewModel
        if (mapFragment != null) {
            mapFragment.getMapAsync(viewModel);
        }
    }
}