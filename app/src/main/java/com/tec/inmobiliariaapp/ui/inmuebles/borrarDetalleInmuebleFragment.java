package com.tec.inmobiliariaapp.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;
import com.bumptech.glide.Glide; // 💡 Importar Glide

import java.text.NumberFormat;
import java.util.Locale;

public class borrarDetalleInmuebleFragment extends Fragment {

    private static final String ARG_INMUEBLE = "arg_inmueble";
    private Inmueble inmueble;

    // 🔹 Método para crear una nueva instancia del fragment con el inmueble como argumento
    // El modelo Inmueble debe implementar Serializable
    public static borrarDetalleInmuebleFragment newInstance(Inmueble inmueble) {
        borrarDetalleInmuebleFragment fragment = new borrarDetalleInmuebleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INMUEBLE, inmueble);
        fragment.setArguments(args);
        return fragment;
    }

    // 🔹 Recuperamos el inmueble desde los argumentos
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable(ARG_INMUEBLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_inmueble, container, false);

        ImageView ivInmueble = view.findViewById(R.id.ivInmueble); // Asegúrate de que este ID exista
        TextView tvDireccion = view.findViewById(R.id.etDireccion);
        TextView tvPrecio = view.findViewById(R.id.etPrecio);
        TextView tvAmbientes = view.findViewById(R.id.etAmbientes);
        TextView tvUso = view.findViewById(R.id.etUso);
        TextView tvTipo = view.findViewById(R.id.etTipo);
        TextView tvDisponible = view.findViewById(R.id.tvDisponible);
        TextView tvSuperficie = view.findViewById(R.id.etSuperficie); // 💡 Agregamos Superficie

        if (inmueble != null) {

            // 💡 CORRECCIÓN 1: Carga de Imagen por URL usando Glide
            String urlImagen = inmueble.getImagen(); // El modelo ya tiene getImagen() que devuelve la URL
            if (urlImagen != null && !urlImagen.isEmpty()) {
                Glide.with(this) // Usamos 'this' para el fragment context
                        .load(urlImagen)
                        .placeholder(R.drawable.casa1) // 💡 Placeholder (usa un drawable que tengas)
                        .error(R.drawable.error_image) // 💡 Error image (si tienes)
                        .into(ivInmueble);
            } else {
                ivInmueble.setImageResource(R.drawable.casa1); // Imagen por defecto si no hay URL
            }

            // 💡 CORRECCIÓN 2: Formato de Precio
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            String precioFormateado = format.format(inmueble.getValor());

            tvDireccion.setText("Dirección: " + inmueble.getDireccion());
            tvPrecio.setText("Precio: " + precioFormateado);
            tvAmbientes.setText("Ambientes: " + inmueble.getAmbientes());
            tvUso.setText("Uso: " + inmueble.getUso());
            tvTipo.setText("Tipo: " + inmueble.getTipo());
            tvDisponible.setText("Disponible: " + (inmueble.isDisponible() ? "Sí" : "No"));
            tvSuperficie.setText("Superficie: " + inmueble.getSuperficie() + " m²"); // 💡 Mostrar Superficie
        }

        return view;
    }
}