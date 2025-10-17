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

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        // Recuperar Bundle de InmueblesFragment
        if (getArguments() != null) {
            viewModel.recuperarInmueble(getArguments());
        }

        // Observamos el inmueble
        viewModel.getInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble != null) {
                    binding.etDireccion.setText(inmueble.getDireccion());
                    binding.etPrecio.setText(String.valueOf(inmueble.getValor()));
                    binding.etTipo.setText(inmueble.getTipo());
                    binding.etUso.setText(inmueble.getUso());
                    binding.tvDisponible.setText(inmueble.isDisponible() ? "DISPONIBLE" : "ALQUILADO");

                    // Cargar imagen
                    String urlImagen = inmueble.getImagen();
                    if (urlImagen != null && !urlImagen.isEmpty()) {
                        String fullUrl = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net" + urlImagen;
                        Glide.with(requireContext())
                                .load(fullUrl)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(binding.ivInmueble);
                    }

                    habilitarCampos(false);
                }
            }
        });

        // Observamos mensajes de éxito/error
        viewModel.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                // Si la actualización fue exitosa, volver a modo ver
                if (msg.contains("actualizado correctamente")) {
                    habilitarCampos(false);
                    binding.btnEditarActualizar.setText("Editar");
                    editando = false;
                }
            }
        });

        // Botón Editar / Actualizar
        binding.btnEditarActualizar.setOnClickListener(v -> {
            Inmueble inmueble = viewModel.getInmueble().getValue();
            if (inmueble == null) return;

            if (!editando) {
                habilitarCampos(true);
                binding.btnEditarActualizar.setText("Actualizar");
                editando = true;
            } else {
                // Tomar datos de los EditText
                String direccion = binding.etDireccion.getText().toString().trim();
                String tipo = binding.etTipo.getText().toString().trim();
                String uso = binding.etUso.getText().toString().trim();
                String precioStr = binding.etPrecio.getText().toString().trim();

                if (direccion.isEmpty() || tipo.isEmpty() || uso.isEmpty() || precioStr.isEmpty()) {
                    Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                double valor;
                try {
                    valor = Double.parseDouble(precioStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Actualizar objeto Inmueble
                inmueble.setDireccion(direccion);
                inmueble.setTipo(tipo);
                inmueble.setUso(uso);
                inmueble.setValor(valor);

                // Llamada al ViewModel para actualizar vía API
                viewModel.actualizarInmueble(inmueble);

                // No deshabilitamos campos ni cambiamos botón hasta recibir respuesta
            }
        });

        return root;
    }

    private void habilitarCampos(boolean habilitar) {
        binding.etDireccion.setEnabled(habilitar);
        binding.etPrecio.setEnabled(habilitar);
        binding.etTipo.setEnabled(habilitar);
        binding.etUso.setEnabled(habilitar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
