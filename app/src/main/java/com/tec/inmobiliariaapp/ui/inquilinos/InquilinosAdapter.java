package com.tec.inmobiliariaapp.ui.inquilinos;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.Inquilino;

import java.util.List;

public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.InquilinoViewHolder> {
    private List<Inquilino> inquilinos;

    public InquilinosAdapter(List<Inquilino> inquilinos) {
        this.inquilinos = inquilinos;
    }

    @NonNull
    @Override
    public InquilinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inquilinos, parent, false);
        return new InquilinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InquilinoViewHolder holder, int position) {
        Inquilino inq = inquilinos.get(position);
        Log.d("InquilinosAdapter", "onBindViewHolder: " + inq.nombre + " " + inq.apellido);
        holder.tvNombreApellido.setText(inq.nombre + " " + inq.apellido);
        holder.tvDni.setText("DNI: " + inq.dni);
        holder.tvTelefono.setText("TelÃ©fono: " + inq.telefono);
        holder.tvEmail.setText("Email: " + inq.email);
        holder.tvInmueble.setText("Inmueble: " + (inq.direccionInmueble != null ? inq.direccionInmueble : "Sin direcciÃ³n")); // AsegÃºrate de que 'inq' tenga la propiedad 'direccionInmueble'
    }

    @Override
    public int getItemCount() {
        return inquilinos != null ? inquilinos.size() : 0;
    }

    public void setInquilinos(List<Inquilino> nuevos) {
        Log.d("InquilinosAdapter", "setInquilinos llamado. Cantidad: " + (nuevos != null ? nuevos.size() : 0));
        this.inquilinos = nuevos;
        notifyDataSetChanged();
    }

    static class InquilinoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreApellido, tvDni, tvTelefono, tvEmail, tvInmueble;
        public InquilinoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreApellido = itemView.findViewById(R.id.tvNombreApellido);
            tvDni = itemView.findViewById(R.id.tvDni);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvInmueble = itemView.findViewById(R.id.tvInmueble);
        }
    }
}
