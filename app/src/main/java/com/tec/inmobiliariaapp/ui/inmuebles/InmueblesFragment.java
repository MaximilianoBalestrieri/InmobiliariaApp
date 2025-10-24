package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tec.inmobiliariaapp.CrearInmuebleFragment;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.databinding.FragmentInmueblesBinding;
import com.tec.inmobiliariaapp.model.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel vm;
    private InmuebleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el RecyclerView con un GridLayoutManager de 2 columnas
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        binding.recyclerInmuebles.setLayoutManager(glm);
        binding.fabAgregarInmueble.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
   //     Navigation.findNavController((Activity)getContext(), R.id.nav_host_fragment_content_main).navigate(R.id.crearInmuebleFragment);

        CrearInmuebleFragment crearInmuebleFragment = new CrearInmuebleFragment();
        //detalleFragment.setArguments(bundle);

        // para navegar fragment actual por el de Crear Inmueble
        getParentFragmentManager().beginTransaction()
                .replace(com.tec.inmobiliariaapp.R.id.fragment_container, crearInmuebleFragment)
                .addToBackStack(null)
                .commit();
    }



});

        // Observar la lista de inmuebles desde el ViewModel
        vm.getlistaInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                if (inmuebles != null) {

                    // Listener de clic para cada card
                    InmuebleAdapter.OnItemClickListener clickListener = new InmuebleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Inmueble inmuebleSeleccionado) {
                            // 👉 Navegar al fragmento de detalle
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("inmueble", inmuebleSeleccionado);

                            DetalleInmuebleFragment detalleFragment = new DetalleInmuebleFragment();
                            detalleFragment.setArguments(bundle);

                            // Reemplazamos el fragment actual por el de detalle
                            getParentFragmentManager().beginTransaction()
                                    .replace(com.tec.inmobiliariaapp.R.id.fragment_container, detalleFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    };

                    // Creamos el adapter con el listener
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
