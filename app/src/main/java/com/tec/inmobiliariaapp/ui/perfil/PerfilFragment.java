package com.tec.inmobiliariaapp.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;



    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentPerfilBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View root=binding.getRoot();
        mViewModel.getPropietario().observe(getViewLifecycleOwner(),propietario ->  {

                //binding.etCodigo.setText(propietario.getIdPropietario()); // (Línea comentada, se transcribe pero no se usa)
              //  binding.etCodigo.setText(String.valueOf(propietario.getIdPropietario()));
                binding.etDni.setText(propietario.getDni());
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etEmail.setText(propietario.getEmail());
               // binding.etContrasena.setText(propietario.getClave());
                binding.etTelefono.setText(propietario.getTelefono());

        });
mViewModel.obtenerPerfil();

        mViewModel.getmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etDni.setEnabled(aBoolean);
                binding.etNombre.setEnabled(aBoolean);
                binding.etApellido.setEnabled(aBoolean);
                binding.etTelefono.setEnabled(aBoolean);
                binding.etEmail.setEnabled(aBoolean);

            }
        });
        mViewModel.getmNombreBoton().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnEditarGuardar.setText(s);
            }
        });
        binding.btnEditarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cambioBoton(
                        binding.btnEditarGuardar.getText().toString(),
                        binding.etNombre.getText().toString(),
                        binding.etApellido.getText().toString(),
                        binding.etDni.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString()
                );
            }
        });
                return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}