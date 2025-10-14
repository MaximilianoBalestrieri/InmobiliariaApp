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
import androidx.lifecycle.ViewModelProvider; // 💡 Nuevo import para ViewModelProvider

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Propietario; // 💡 Nuevo import para el modelo

public class PerfilFragment extends Fragment {

    private TextView tvCodigo;
    private EditText etDni, etNombre, etApellido, etEmail, etContrasena, etTelefono;
    private Button btnEditarGuardar;
    private boolean enEdicion = false;

    // 💡 Variables para MVVM
    private PerfilViewModel viewModel;
    private Propietario propietarioActual; // Almacena los datos del propietario que se están editando

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar Vistas
        tvCodigo = view.findViewById(R.id.tvCodigo);
        etDni = view.findViewById(R.id.etDni);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        etContrasena = view.findViewById(R.id.etContrasena);
        etTelefono = view.findViewById(R.id.etTelefono);
        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);

        // 2. Inicializar ViewModel (usando el ViewModelProvider del Fragment)
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // 3. Observar los datos del Propietario (Llamada GET a la API)
        viewModel.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                // Almacenamos el objeto recibido para futuras actualizaciones
                propietarioActual = propietario;
                mostrarDatos(propietario);
                // Inicialmente, no está en modo edición
                activarEdicion(false);
            } else {
                Toast.makeText(getContext(), "Error al cargar el perfil. Verifique su conexión.", Toast.LENGTH_LONG).show();
                // Si falla, al menos dejamos el botón disponible para reintentar o editar lo que haya
            }
        });

        // 4. Observar el resultado de la actualización (Llamada PUT a la API)
        viewModel.getActualizacionExitosa().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null) {
                if (exito) {
                    Toast.makeText(getContext(), "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    // Volvemos a modo lectura después del éxito
                    activarEdicion(false);
                } else {
                    Toast.makeText(getContext(), "Error al actualizar. El servidor rechazó los datos.", Toast.LENGTH_LONG).show();
                }
            }
            // Importante: No ponemos el observer en 'observeForever' para evitar fugas de memoria.
        });


        // 5. Listener del Botón Editar / Guardar
        btnEditarGuardar.setOnClickListener(v -> {
            if (propietarioActual == null) {
                // Si el objeto no se ha cargado, intentamos recargar (útil para errores de red iniciales)
                Toast.makeText(getContext(), "Cargando datos. Intente de nuevo.", Toast.LENGTH_SHORT).show();
                viewModel.cargarPropietario();
                return;
            }

            if (!enEdicion) {
                activarEdicion(true);
            } else {
                guardarDatos();
            }
        });
    }

    // ----------------------------------------------------
    // METODOS DE LA VISTA
    // ----------------------------------------------------

    private void mostrarDatos(Propietario p) {
        // Muestra los datos del objeto Propietario en las vistas
        tvCodigo.setText("Código: " + p.getIdPropietario());
        etDni.setText(p.getDni());
        etNombre.setText(p.getNombre());
        etApellido.setText(p.getApellido());
        etEmail.setText(p.getEmail());
        etContrasena.setText(p.getClave());
        etTelefono.setText(p.getTelefono());
    }

    private void activarEdicion(boolean editar) {
        enEdicion = editar;

        // 💡 Mejor práctica: El email (usuario) generalmente no se edita.
        // El ID (código) nunca se edita.
        etDni.setEnabled(editar);
        etNombre.setEnabled(editar);
        etApellido.setEnabled(editar);
        etEmail.setEnabled(false); // No editable
        etContrasena.setEnabled(editar);
        etTelefono.setEnabled(editar);

        btnEditarGuardar.setText(editar ? "Guardar" : "Editar");
    }

    private void guardarDatos() {
        // 1. Recopilar datos modificados y actualizar el objeto Propietario

        // ¡Cuidado! La clave (Clave) se envía. Si no la llenas, la API podría borrarla o fallar.
        String contrasena = etContrasena.getText().toString();

        // El resto de los campos son seguros de actualizar en el objeto actual
        propietarioActual.setDni(etDni.getText().toString());
        propietarioActual.setNombre(etNombre.getText().toString());
        propietarioActual.setApellido(etApellido.getText().toString());
        propietarioActual.setTelefono(etTelefono.getText().toString());

        // Si el usuario modificó el campo Contraseña, lo enviamos.
        // Si no lo modificó, el objeto 'propietarioActual' ya tiene la clave original (asumiendo que se cargó).
        propietarioActual.setClave(contrasena);

        // 2. Llamar al ViewModel para iniciar la actualización (PUT)
        viewModel.actualizarPropietario(propietarioActual);

        // NO desactivamos la edición aquí. La desactivamos en el observer si el API responde éxito.
        Toast.makeText(getContext(), "Enviando datos...", Toast.LENGTH_SHORT).show();
    }
}