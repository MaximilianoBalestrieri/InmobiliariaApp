package com.tec.inmobiliariaapp.ui.contratos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Contrato;

import java.util.List;

public class ContratosAdapter extends RecyclerView.Adapter<ContratosAdapter.ViewHolder> {

    // 1. Interfaz para comunicar el clic al Fragmento
    public interface OnContratoClickListener {
        void onPagosClick(int idContrato);
    }

    private List<Contrato> contratos;
    private final OnContratoClickListener clickListener; // Variable para guardar la referencia del Fragmento/Listener

    // 2. Constructor: inicializa el adapter con la lista de contratos y el listener
    public ContratosAdapter(List<Contrato> contratos, OnContratoClickListener clickListener) {
        this.contratos = contratos;
        this.clickListener = clickListener;
    }

    // Actualiza la lista y notifica los cambios
    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
        notifyDataSetChanged();
    }

    // Crea el ViewHolder inflando el layout correspondiente
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrato, parent, false);
        return new ViewHolder(view);
    }

    // Vincula los datos del contrato con los elementos de la vista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Contrato contrato = contratos.get(position);

            // Muestro los datos del inmueble
            holder.tvDireccion.setText("Direccion: " + contrato.inmueble.getDireccion());
            holder.tvUso.setText("Uso: " + contrato.inmueble.getUso());
            holder.tvTipo.setText("Tipo: " + contrato.inmueble.getTipo());
            holder.tvAmbientes.setText("Ambientes: " + contrato.inmueble.getAmbientes());
            holder.tvValor.setText("Valor: $ " + contrato.inmueble.getValor());

            // Muestro los datos del contrato
            holder.tvContratoVigente.setText("Contrato Vigente: " + (contrato.estado ? "Sí" : "No"));
            holder.tvFechaInicio.setText("Inicio: " + contrato.fechaInicio);
            holder.tvFechaFin.setText("Fin: " + contrato.fechaFinalizacion);
            holder.tvInquilino.setText("Inquilino: " + contrato.inquilino.nombre + " " + contrato.inquilino.apellido);

            // Lógica de Imagen con Glide
            String urlImagen = contrato.inmueble.getImagen();
            if (urlImagen != null && !urlImagen.isEmpty()) {
                String fullUrl = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/" + urlImagen;
                Glide.with(holder.ivImagen.getContext())
                        .load(fullUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.ivImagen);
            } else {
                holder.ivImagen.setImageResource(R.drawable.ic_launcher_background);
            }

            // 3. Navegación: Llama al método del listener para que el Fragmento se encargue de navegar.
            holder.btnPagos.setOnClickListener(v -> {
                // Notificamos al Fragmento qué ID de contrato debe usar para navegar
                clickListener.onPagosClick(contrato.idContrato);
            });

        } catch (Exception e) {
            Log.e("ContratosAdapter", "Error al mostrar contrato: " + e.getMessage());
        }
    }

    // Devuelve la cantidad de contratos
    @Override
    public int getItemCount() {
        return contratos != null ? contratos.size() : 0;
    }

    // Define el ViewHolder con los elementos de la vista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvDireccion, tvUso, tvTipo, tvAmbientes, tvValor, tvDisponible, tvContratoVigente, tvFechaInicio, tvFechaFin, tvInquilino;
        Button btnPagos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvUso = itemView.findViewById(R.id.tvUso);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvAmbientes = itemView.findViewById(R.id.tvAmbientes);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvContratoVigente = itemView.findViewById(R.id.tvContratoVigente);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicio);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);
            tvInquilino = itemView.findViewById(R.id.tvInquilino);
            btnPagos = itemView.findViewById(R.id.btnPagos);
            // tvDisponible no está en el constructor de ViewHolder, si estaba en el layout debe ser tvDisponible = itemView.findViewById(R.id.tvDisponible);
        }
    }
}