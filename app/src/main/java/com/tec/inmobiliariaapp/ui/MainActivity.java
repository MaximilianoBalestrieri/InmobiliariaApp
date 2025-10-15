package com.tec.inmobiliariaapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.ui.contratos.ContratosFragment;
import com.tec.inmobiliariaapp.ui.inicio.InicioFragment;
import com.tec.inmobiliariaapp.ui.inmuebles.InmueblesFragment;
import com.tec.inmobiliariaapp.ui.inquilino.InquilinoFragment;
import com.tec.inmobiliariaapp.ui.login.LoginActivity;
import com.tec.inmobiliariaapp.ui.perfil.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Header con nombre del usuario
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvUsuarioLogueado);
        // 1. Leer SharedPreferences
        SharedPreferences prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        // 2. Obtener el email guardado con la clave "email"
        String emailUsuario = prefs.getString("email", "Usuario Invitado");

        // 3. Actualizar la vista
        if (tvNombre != null) {
            tvNombre.setText("Usuario: " + emailUsuario);
        }

        // Fragment inicial
        loadFragment(new InicioFragment(), "Inicio");

        // FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Aquí podés abrir el fragment para agregar inmueble
            getSupportFragmentManager().beginTransaction()
                   // .replace(R.id.fragment_container, new InmueblesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Listener del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragmentToLoad = null;
            String title = "";

            if (id == R.id.nav_inicio) {
                fragmentToLoad = new InicioFragment();
                title = "Inicio";
            } else if (id == R.id.nav_perfil) {
                fragmentToLoad = new PerfilFragment();
                title = "Perfil";
            } else if (id == R.id.nav_inmuebles) {
               fragmentToLoad = new InmueblesFragment();
                title = "Inmuebles";
            } else if (id == R.id.nav_contratos) {
                fragmentToLoad = new ContratosFragment();
                title = "Contratos";
            } else if (id == R.id.nav_inquilinos) {
                fragmentToLoad = new InquilinoFragment();
                title = "Inquilinos";
            } else if (id == R.id.nav_logout) {
                mostrarDialogoLogout();
                drawerLayout.closeDrawers();
                return true;
            }

            if (fragmentToLoad != null) {
                loadFragment(fragmentToLoad, title);
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void mostrarDialogoLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas salir de la aplicación?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
