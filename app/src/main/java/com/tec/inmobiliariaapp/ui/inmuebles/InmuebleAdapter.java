package com.tec.inmobiliariaapp.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    private List<Inmueble> inmuebles;
    private OnItemClickListener listener;

    public InmuebleAdapter(List<Inmueble> inmuebles, OnItemClickListener listener) {
        this.inmuebles = inmuebles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);

        // Formateador de moneda para Argentina/pesos
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        // 1. Asignar datos principales
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("Precio: " + format.format(inmueble.getValor()));

        // 2. Asignar nuevos campos (Tipo | Uso)
        holder.tvTipoUso.setText(inmueble.getTipo() + " | " + inmueble.getUso());

        // 3. Lógica de Disponibilidad y Fondo Dinámico
        if (inmueble.isDisponible()) {
            holder.tvDisponibilidad.setText("DISPONIBLE");
            // Aplicar el fondo verde redondeado
            holder.tvDisponibilidad.setBackgroundResource(R.drawable.bg_disponible);
        } else {
            holder.tvDisponibilidad.setText("ALQUILADO");
            // Aplicar el fondo rojo redondeado
            holder.tvDisponibilidad.setBackgroundResource(R.drawable.bg_alquilado);
        }

        // 4. Lógica de Imagen (Usando URL de la API)
        String urlImagen = inmueble.getImagen();
        if (urlImagen != null && !urlImagen.isEmpty()) {
            // URL Base de la API + path de la imagen
            String fullUrl = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net" + urlImagen;

            Glide.with(holder.ivInmueble.getContext())
                    .load(fullUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.ivInmueble);

        } else {
            holder.ivInmueble.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(inmueble));
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }


    public void actualizarLista(List<Inmueble> nuevaLista) {
        this.inmuebles = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivInmueble;
        // Referencias a todos los TextViews del item_inmueble.xml
        TextView tvDireccion, tvPrecio, tvTipoUso, tvDisponibilidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivInmueble = itemView.findViewById(R.id.ivInmuebleItem);
            tvDireccion = itemView.findViewById(R.id.tvDireccionItem);
            tvPrecio = itemView.findViewById(R.id.tvPrecioItem);

            // Enlazamos los nuevos IDs de item_inmueble.xml
            tvTipoUso = itemView.findViewById(R.id.tvTipoUsoItem);
            tvDisponibilidad = itemView.findViewById(R.id.tvDisponibilidadItem);
        }
    }
}