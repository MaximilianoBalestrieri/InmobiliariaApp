package com.tec.inmobiliariaapp.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.ui.LoginActivity;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        super(R.layout.fragment_logout);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mostramos el diálogo sobre el fragment
        view.post(() -> new AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas salir de la aplicación?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Si el usuario no quiere salir, volvemos al fragment anterior (Inicio)
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new com.tec.inmobiliariaapp.ui.inicio.InicioFragment())
                            .commit();
                })
                .show());
    }
}
