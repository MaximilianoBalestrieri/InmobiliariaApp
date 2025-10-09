package com.tec.inmobiliariaapp.ui.inmuebles;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.inmobiliariaapp.InmuebleAdapter;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;
import java.util.ArrayList;
import java.util.List;

public class InmueblesFragment extends Fragment {

    private RecyclerView recyclerView;
    private InmuebleAdapter adapter;
    private List<Inmueble> listaInmuebles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);

        recyclerView = view.findViewById(R.id.recyclerInmuebles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Datos de prueba
        listaInmuebles = new ArrayList<>();
        listaInmuebles.add(new Inmueble(1, 3, "Belgrano 123", 150000.0, "Residencial", true, "Departamento", R.drawable.casa1));
        listaInmuebles.add(new Inmueble(2, 2, "Av. San Martin 742", 120000.0, "Residencial", true, "Casa", R.drawable.casa3));

        adapter = new InmuebleAdapter(listaInmuebles, inmueble -> {
            // Aquí navegás al detalle
            DetalleInmuebleFragment detalle = DetalleInmuebleFragment.newInstance(inmueble);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detalle)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fabAgregarInmueble);
        fab.setOnClickListener(v -> {
            AgregarInmuebleFragment agregarFragment = new AgregarInmuebleFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, agregarFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
}
