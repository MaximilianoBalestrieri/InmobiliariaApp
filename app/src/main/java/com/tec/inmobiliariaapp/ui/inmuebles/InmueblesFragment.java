package com.tec.inmobiliariaapp.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // 💡 Nuevo import para mensajes
import androidx.annotation.NonNull; // 💡 Import añadido
import androidx.annotation.Nullable; // 💡 Import añadido
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // 💡 Nuevo import para ViewModel
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tec.inmobiliariaapp.InmuebleAdapter;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;
import java.util.ArrayList;
import java.util.List;

public class InmueblesFragment extends Fragment {

    private InmueblesViewModel viewModel; // 💡 ViewModel
    private RecyclerView recyclerView;
    private InmuebleAdapter adapter;
    // La lista se inicializa vacía, el ViewModel la llenará
    private List<Inmueble> listaInmuebles = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);

        // 1. Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        // 2. Configurar RecyclerView y Adaptador
        recyclerView = view.findViewById(R.id.recyclerInmuebles); // 💡 Confirma que este es el ID correcto (recyclerInmuebles)
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar el adaptador con la lista vacía.
        // Se usará el mismo listener de click que ya tenías.
        adapter = new InmuebleAdapter(listaInmuebles, inmueble -> {
            // Lógica de click para navegar al detalle
            // Debes asegurarte de que DetalleInmuebleFragment y newInstance existan
            // DetalleInmuebleFragment detalle = DetalleInmuebleFragment.newInstance(inmueble);
            // getParentFragmentManager().beginTransaction()
            //         .replace(R.id.fragment_container, detalle)
            //         .addToBackStack(null)
            //         .commit();
            Toast.makeText(getContext(), "Click en Inmueble: " + inmueble.getDireccion(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        // 3. Observar los datos del ViewModel (Llamada a la API)
        viewModel.getInmuebles().observe(getViewLifecycleOwner(), listaInmueblesRecibida -> {
            if (listaInmueblesRecibida != null && !listaInmueblesRecibida.isEmpty()) {
                // Si recibimos datos, los pasamos al adaptador
                adapter.actualizarLista(listaInmueblesRecibida);
            } else {
                // Si la lista es nula o vacía (error de API o no hay inmuebles)
                adapter.actualizarLista(new ArrayList<>()); // Limpia la lista por si acaso
                Toast.makeText(getContext(), "No se encontraron inmuebles o hubo un error de carga.", Toast.LENGTH_LONG).show();
            }
        });

        // 4. Configurar FAB
        FloatingActionButton fab = view.findViewById(R.id.fabAgregarInmueble);
        fab.setOnClickListener(v -> {
            // Lógica de navegación para agregar inmueble
            // Asegúrate de que AgregarInmuebleFragment exista
            // AgregarInmuebleFragment agregarFragment = new AgregarInmuebleFragment();
            // requireActivity().getSupportFragmentManager().beginTransaction()
            //         .replace(R.id.fragment_container, agregarFragment)
            //         .addToBackStack(null)
            //         .commit();
            Toast.makeText(getContext(), "Navegando a Agregar Inmueble", Toast.LENGTH_SHORT).show();
        });

        // 5. Devolver la vista
        return view;
    }

    // Si necesitas que la lista se recargue al volver al fragment, puedes hacer esto
    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.recargarInmuebles();
        }
    }
}