package com.tec.inmobiliariaapp.ui.inmuebles;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // Importado para el Toast de ejemplo

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import com.tec.inmobiliariaapp.databinding.FragmentInmueblesBinding;
import com.tec.inmobiliariaapp.model.Inmueble;
// No es necesario importar PerfilViewModel aquí:
// import com.tec.inmobiliariaapp.ui.perfil.PerfilViewModel;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel vm;
    private InmuebleAdapter adapter; // Declarar el adaptador a nivel de clase o del onChanged si es mejor

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 1. Configurar el RecyclerView (se puede hacer una sola vez aquí)
        // Usar GridLayoutManager con 2 columnas
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
       // binding.listaInmuebles.setLayoutManager(glm);
        binding.recyclerInmuebles.setLayoutManager(glm);

        vm.getlistaInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                if (inmuebles != null) {
                    // Implementación del OnItemClickListener
                    InmuebleAdapter.OnItemClickListener clickListener = new InmuebleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Inmueble inmueble) {
                            // Lógica para manejar el click (ej: navegar a la vista de detalle)
                            Toast.makeText(getContext(), "Has seleccionado: " + inmueble.getDireccion(), Toast.LENGTH_SHORT).show();
                        }
                    };

                    // CORRECCIÓN: Llamada al constructor correcta (Lista y Listener)
                    adapter = new InmuebleAdapter(inmuebles, clickListener);

                    binding.recyclerInmuebles.setAdapter(adapter);
                }
            }
        });

        vm.obtenerListaInmuebles();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}