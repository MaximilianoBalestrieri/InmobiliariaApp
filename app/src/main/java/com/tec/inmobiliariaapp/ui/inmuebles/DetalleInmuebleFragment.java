package com.tec.inmobiliariaapp.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.databinding.FragmentDetalleInmuebleBinding;
import com.tec.inmobiliariaapp.model.Inmueble;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private DetalleInmuebleViewModel viewModel;
    private boolean editando = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        if (getArguments() != null) {
            viewModel.recuperarInmueble(getArguments());
        }

        viewModel.getInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble != null) {
                    // Imagen
                    String urlImagen = inmueble.getImagen();
                    if (urlImagen != null && !urlImagen.isEmpty()) {
                        String fullUrl = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/" + urlImagen;
                        Glide.with(requireContext())
                                .load(fullUrl)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(binding.ivInmueble);
                    }

                    // Llenamos todos los campos
                    binding.etDireccion.setText(inmueble.getDireccion());
                    binding.etPrecio.setText(String.valueOf(inmueble.getValor()));
                    binding.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                    binding.etSuperficie.setText(String.valueOf(inmueble.getSuperficie()));
                    binding.etUso.setText(inmueble.getUso());
                    binding.etTipo.setText(inmueble.getTipo());
                    binding.cbDisponible.setChecked(inmueble.isDisponible());
                    binding.etLatitud.setText(String.valueOf(inmueble.getLatitud()));
                    binding.etLongitud.setText(String.valueOf(inmueble.getLongitud()));
                    binding.etIdPropietario.setText(String.valueOf(inmueble.getIdPropietario()));
                    binding.cbContratoVigente.setChecked(inmueble.isTieneContratoVigente());

                    habilitarCampos(false);
                }
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnEditarActualizar.setOnClickListener(v -> {
            Inmueble inmueble = viewModel.getInmueble().getValue();
            if (inmueble == null) return;

            if (!editando) {
                habilitarCampos(true);
                binding.btnEditarActualizar.setText("Actualizar");
                editando = true;
            } else {
                // Actualizamos objeto con valores de EditTexts y CheckBoxes
                inmueble.setDireccion(binding.etDireccion.getText().toString());
                inmueble.setTipo(binding.etTipo.getText().toString());
                inmueble.setUso(binding.etUso.getText().toString());
                inmueble.setDisponible(binding.cbDisponible.isChecked());
                inmueble.setTieneContratoVigente(binding.cbContratoVigente.isChecked());

                inmueble.setValor(parseDoubleOrZero(binding.etPrecio.getText().toString()));
                inmueble.setAmbientes(parseIntOrZero(binding.etAmbientes.getText().toString()));
                inmueble.setSuperficie(Integer.parseInt(binding.etSuperficie.getText().toString()));

                inmueble.setLatitud(parseDoubleOrZero(binding.etLatitud.getText().toString()));
                inmueble.setLongitud(parseDoubleOrZero(binding.etLongitud.getText().toString()));
                inmueble.setIdPropietario(parseIntOrZero(binding.etIdPropietario.getText().toString()));

                // Enviar a API
                viewModel.actualizarInmueble(inmueble);

                habilitarCampos(false);
                binding.btnEditarActualizar.setText("Editar");
                editando = false;
            }
        });

        return root;
    }

    private void habilitarCampos(boolean habilitar) {
        binding.etDireccion.setEnabled(habilitar);
        binding.etPrecio.setEnabled(habilitar);
        binding.etAmbientes.setEnabled(habilitar);
        binding.etSuperficie.setEnabled(habilitar);
        binding.etUso.setEnabled(habilitar);
        binding.etTipo.setEnabled(habilitar);
        binding.cbDisponible.setEnabled(habilitar);
        binding.etLatitud.setEnabled(habilitar);
        binding.etLongitud.setEnabled(habilitar);
        binding.etIdPropietario.setEnabled(habilitar);
        binding.cbContratoVigente.setEnabled(habilitar);
    }

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
