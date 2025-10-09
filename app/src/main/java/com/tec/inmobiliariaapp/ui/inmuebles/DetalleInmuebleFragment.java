package com.tec.inmobiliariaapp.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;

public class DetalleInmuebleFragment extends Fragment {

    private static final String ARG_INMUEBLE = "arg_inmueble";
    private Inmueble inmueble;

    // 🔹 Método para crear una nueva instancia del fragment con el inmueble como argumento
    public static DetalleInmuebleFragment newInstance(Inmueble inmueble) {
        DetalleInmuebleFragment fragment = new DetalleInmuebleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INMUEBLE, inmueble);
        fragment.setArguments(args);
        return fragment;
    }

    // 🔹 Recuperamos el inmueble desde los argumentos
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable(ARG_INMUEBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_inmueble, container, false);

        ImageView ivInmueble = view.findViewById(R.id.ivInmueble);
        TextView tvDireccion = view.findViewById(R.id.tvDireccion);
        TextView tvPrecio = view.findViewById(R.id.tvPrecio);
        TextView tvAmbientes = view.findViewById(R.id.tvAmbientes);
        TextView tvUso = view.findViewById(R.id.tvUso);
        TextView tvTipo = view.findViewById(R.id.tvTipo);
        TextView tvDisponible = view.findViewById(R.id.tvDisponible);

        if (inmueble != null) {
            ivInmueble.setImageResource(inmueble.getImagen());
            tvDireccion.setText("Dirección: " + inmueble.getDireccion());
            tvPrecio.setText("Precio: $" + inmueble.getPrecio());
            tvAmbientes.setText("Ambientes: " + inmueble.getAmbientes());
            tvUso.setText("Uso: " + inmueble.getUso());
            tvTipo.setText("Tipo: " + inmueble.getTipo());
            tvDisponible.setText("Disponible: " + (inmueble.isDisponible() ? "Sí" : "No"));
        }

        return view;
    }
}
