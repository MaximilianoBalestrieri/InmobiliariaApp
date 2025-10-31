package com.tec.inmobiliariaapp.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider; // Mantenemos esta
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tec.inmobiliariaapp.R;
// import androidx.lifecycle.ViewModelProvider; // <--- ELIMINADO: Duplicado

import androidx.recyclerview.widget.LinearLayoutManager;
import com.tec.inmobiliariaapp.databinding.FragmentInquilinosBinding; // importo la clase de binding

import com.tec.inmobiliariaapp.model.Inquilino;

import java.util.ArrayList;


public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;
    private FragmentInquilinosBinding binding;
    private InquilinosAdapter adapter;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 1. Inicialización del Adapter y RecyclerView aquí (solo configuración de vista)
        // Se mantiene aquí ya que la vista (binding) ya está disponible.
        adapter = new InquilinosAdapter(new ArrayList<Inquilino>());
        binding.rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvInquilinos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Inicialización del ViewModel y Observación aquí (en onViewCreated)
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        // 3. Observo los cambios en la lista de inquilinos
        mViewModel.getInquilinos().observe(getViewLifecycleOwner(), inquilinos -> {
            // Verifica que la lista no sea null y actualiza el adaptador.
            if (adapter != null) {
                adapter.setInquilinos(inquilinos != null ? inquilinos : new ArrayList<>());
            }
        });

        // 4. Inicio la carga de inquilinos
        mViewModel.cargarInquilinos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null; // También liberamos la referencia al adaptador si es necesario
    }
}