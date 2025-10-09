package com.tec.inmobiliariaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inmueble;
import java.util.List;

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
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("Precio: $" + inmueble.getPrecio());
        holder.ivInmueble.setImageResource(inmueble.getImagen());
        // Si tenés Glide o Picasso podés cargar la imagen real:
        // Glide.with(holder.ivInmueble.getContext()).load(inmueble.getImagen()).into(holder.ivInmueble);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(inmueble));
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivInmueble;
        TextView tvDireccion, tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivInmueble = itemView.findViewById(R.id.ivInmuebleItem);
            tvDireccion = itemView.findViewById(R.id.tvDireccionItem);
            tvPrecio = itemView.findViewById(R.id.tvPrecioItem);
        }
    }
}
