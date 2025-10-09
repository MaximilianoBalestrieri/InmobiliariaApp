package com.tec.inmobiliariaapp.ui.perfil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tec.inmobiliariaapp.R;

public class PerfilFragment extends Fragment {

    private TextView tvCodigo;
    private EditText etDni, etNombre, etApellido, etEmail, etContrasena, etTelefono;
    private Button btnEditarGuardar;
    private boolean enEdicion = false;

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCodigo = view.findViewById(R.id.tvCodigo);
        etDni = view.findViewById(R.id.etDni);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        etContrasena = view.findViewById(R.id.etContrasena);
        etTelefono = view.findViewById(R.id.etTelefono);
        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);

        // Cargar datos simulados del propietario
        tvCodigo.setText("Código: 123");
        etDni.setText("12345678");
        etNombre.setText("Maximiliano");
        etApellido.setText("Balestrieri");
        etEmail.setText("maxi@email.com");
        etContrasena.setText("1234");
        etTelefono.setText("11165954613");

        // Botón Editar / Guardar
        btnEditarGuardar.setOnClickListener(v -> {
            if (!enEdicion) {
                activarEdicion(true);
            } else {
                guardarDatos();
            }
        });
    }

    private void activarEdicion(boolean editar) {
        enEdicion = editar;

        etDni.setEnabled(editar);
        etNombre.setEnabled(editar);
        etApellido.setEnabled(editar);
        etEmail.setEnabled(editar);
        etContrasena.setEnabled(editar);
        etTelefono.setEnabled(editar);

        btnEditarGuardar.setText(editar ? "Guardar" : "Editar");
    }

    private void guardarDatos() {
        // Obtener datos modificados
        String dni = etDni.getText().toString();
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String email = etEmail.getText().toString();
        String contrasena = etContrasena.getText().toString();
        String telefono = etTelefono.getText().toString();

        // Aquí se haría la llamada al ViewModel / DB para guardar los cambios
        Toast.makeText(getContext(), "Datos guardados!", Toast.LENGTH_SHORT).show();

        // Volvemos a modo lectura
        activarEdicion(false);
    }
}
