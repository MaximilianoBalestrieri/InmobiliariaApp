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

public class AgregarInmuebleFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivFoto;
    private EditText etDireccion, etPrecio, etAmbientes, etUso, etTipo;
    private Uri imagenSeleccionadaUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_inmueble, container, false);

        ivFoto = view.findViewById(R.id.ivFotoInmueble);
        etDireccion = view.findViewById(R.id.etDireccion);
        etPrecio = view.findViewById(R.id.etPrecio);
        etAmbientes = view.findViewById(R.id.etAmbientes);
        etUso = view.findViewById(R.id.etUso);
        etTipo = view.findViewById(R.id.etTipo);
        Button btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);
        Button btnGuardar = view.findViewById(R.id.btnGuardarInmueble);

        // 📷 Seleccionar imagen
        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // 💾 Guardar inmueble
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

        if (direccion.isEmpty() || precioStr.isEmpty() || ambientesStr.isEmpty() || uso.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        int ambientes = Integer.parseInt(ambientesStr);

        // 🔹 Creamos el nuevo inmueble (disponible = false por defecto)
        Inmueble nuevoInmueble = new Inmueble(
                0, // id temporal
                ambientes,
                direccion,
                precio,
                uso,
                false, // 🔒 Deshabilitado por defecto
                tipo,
                R.drawable.baseline_home_24 // imagen por defecto si no se selecciona ninguna
        );

        Toast.makeText(getContext(), "Inmueble agregado (por defecto no disponible)", Toast.LENGTH_LONG).show();

        // Podés agregarlo a tu lista o enviar al servidor acá

        // Vuelve al fragment anterior
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
