package com.tec.inmobiliariaapp.ui.inmuebles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // 💡 Importante para MVVM

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;

import java.io.IOException;

public class AgregarInmuebleFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivFoto;
    private EditText etDireccion, etPrecio, etAmbientes, etUso, etTipo;
    private Button btnSeleccionarFoto, btnGuardar;
    private Uri imagenSeleccionadaUri;

    // 💡 Declaración del ViewModel
    private AgregarInmuebleViewModel viewModel;

    public AgregarInmuebleFragment() {
        super(R.layout.fragment_agregar_inmueble);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_inmueble, container, false);

        // 1. Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);

        // 2. Enlazar Vistas
        ivFoto = view.findViewById(R.id.ivFotoInmueble);
        etDireccion = view.findViewById(R.id.etDireccion);
        etPrecio = view.findViewById(R.id.etPrecio);
        etAmbientes = view.findViewById(R.id.etAmbientes);
        etUso = view.findViewById(R.id.etUso);
        etTipo = view.findViewById(R.id.etTipo);
        btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);
        btnGuardar = view.findViewById(R.id.btnGuardarInmueble);

        // 3. Observar el resultado del guardado desde el ViewModel
        viewModel.getResultadoGuardado().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null) {
                if (exito) {
                    Toast.makeText(getContext(), "✅ Inmueble agregado con éxito.", Toast.LENGTH_LONG).show();
                    // Vuelve al fragment anterior (InmueblesFragment)
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "❌ Error al guardar el inmueble. Intente de nuevo.", Toast.LENGTH_LONG).show();
                }
                // Habilitar el botón si hubo error para que puedan reintentar
                btnGuardar.setEnabled(true);
            }
        });

        // 4. Listeners
        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnGuardar.setOnClickListener(v -> guardarInmueble());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imagenSeleccionadaUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagenSeleccionadaUri);
                ivFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al cargar la imagen.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void guardarInmueble() {
        String direccion = etDireccion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String ambientesStr = etAmbientes.getText().toString().trim();
        String uso = etUso.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();

        if (direccion.isEmpty() || precioStr.isEmpty() || ambientesStr.isEmpty() || uso.isEmpty() || tipo.isEmpty() || imagenSeleccionadaUri == null) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos y seleccione una foto.", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor = Double.parseDouble(precioStr);
        int ambientes = Integer.parseInt(ambientesStr);

        // Deshabilitar botón para evitar envíos duplicados
        btnGuardar.setEnabled(false);
        Toast.makeText(getContext(), "Enviando inmueble...", Toast.LENGTH_SHORT).show();

        // 🔹 CORRECCIÓN: Crear el objeto Inmueble usando el constructor vacío y setters
        Inmueble nuevoInmueble = new Inmueble();

        // Asignamos los valores que el usuario proporcionó
        nuevoInmueble.setDireccion(direccion);
        nuevoInmueble.setAmbientes(ambientes);
        nuevoInmueble.setValor(valor);
        nuevoInmueble.setUso(uso);
        nuevoInmueble.setTipo(tipo);

        // Valores predeterminados/backend (se asume disponible al crear)
        nuevoInmueble.setDisponible(true);
        // El campo 'imagen' en el modelo es la URL final, pero aquí usamos el URI para el ViewModel
        nuevoInmueble.setImagen(imagenSeleccionadaUri.toString());

        // 💡 Llamada al ViewModel para iniciar el proceso de guardado (API)
        // El ViewModel debería encargarse de obtener el Path real del URI y subir la imagen.
        viewModel.guardarInmueble(nuevoInmueble, imagenSeleccionadaUri);
    }
}