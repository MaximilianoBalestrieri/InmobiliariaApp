package com.tec.inmobiliariaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.tec.inmobiliariaapp.databinding.ActivityMainBinding;
import com.tec.inmobiliariaapp.ui.LoginActivity;
import com.tec.inmobiliariaapp.ui.contratos.ContratosFragment;
import com.tec.inmobiliariaapp.ui.inicio.InicioFragment;
import com.tec.inmobiliariaapp.ui.inmuebles.InmueblesFragment;
import com.tec.inmobiliariaapp.ui.inquilino.InquilinoFragment;
import com.tec.inmobiliariaapp.ui.perfil.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);

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
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        if (nombreUsuario != null) {
            tvNombre.setText("Usuario: " + nombreUsuario);
        }

        // Fragment inicial
        loadFragment(new InicioFragment(), "Inicio");

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

        // FAB para agregar inmueble
        fab.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new InmueblesFragment())
                    .addToBackStack(null)
                    .commit();
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