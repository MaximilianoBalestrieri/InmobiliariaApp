package com.tec.inmobiliariaapp.ui.contratos;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation; // Importante para la navegación

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.inmobiliariaapp.R;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tec.inmobiliariaapp.databinding.FragmentContratosBinding;
import com.tec.inmobiliariaapp.ui.MainActivity;

import java.util.ArrayList;

// 1. EL FRAGMENTO IMPLEMENTA LA INTERFAZ DEL ADAPTER
public class ContratosFragment extends Fragment implements ContratosAdapter.OnContratoClickListener {

    private ContratosViewModel mViewModel;
    private FragmentContratosBinding binding;
    private static final String TAG = "ContratosFragment";
    public static ContratosFragment newInstance() {
        return new ContratosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding.rvContratosVigentes.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. OBSERVACIÓN Y ASIGNACIÓN DEL ADAPTER CON EL LISTENER
        mViewModel.getContratosVigentes().observe(getViewLifecycleOwner(), lista -> {
            // Pasamos 'this' (el Fragmento) como listener al constructor del Adaptador
            ContratosAdapter adapter = new ContratosAdapter(lista != null ? lista : new ArrayList<>(), this);
            binding.rvContratosVigentes.setAdapter(adapter);
        });

        mViewModel.getUIState().observe(getViewLifecycleOwner(), uiState -> {
            binding.rvContratosVigentes.setVisibility(uiState.rvVisibility);
            binding.tvEmpty.setVisibility(uiState.tvEmptyVisibility);
        });

        mViewModel.cargarContratosVigentes();
    }

    // 3. IMPLEMENTACIÓN DEL MÉTODO onPagosClick para manejar la navegación de forma segura
    @Override
    public void onPagosClick(int idContrato) {
        Log.d(TAG, "ID de Contrato para Pagos: " + idContrato);
        // 1. Crear el Fragmento de destino
        Fragment pagosFragment = new com.tec.inmobiliariaapp.ui.pagos.PagosFragment();

        // 2. Crear y llenar el Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("idContrato", idContrato);

        // 3. LLAMADA CLAVE: Usar la Activity para manejar la navegación
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToFragment(
                    pagosFragment,
                    "Pagos del Contrato", // Título a mostrar en la Toolbar
                    bundle
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}