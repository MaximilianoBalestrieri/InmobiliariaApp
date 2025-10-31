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
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.ui.contratos.ContratosFragment;
import com.tec.inmobiliariaapp.ui.inicio.InicioFragment;
import com.tec.inmobiliariaapp.ui.inmuebles.InmueblesFragment;
import com.tec.inmobiliariaapp.ui.inquilinos.InquilinosFragment;
import com.tec.inmobiliariaapp.ui.login.LoginActivity;
import com.tec.inmobiliariaapp.ui.perfil.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int CONTAINER_ID = R.id.fragment_container;

    // Al usar navegación manual con un contenedor de fragments,
    // la variable `pendingFragment` ya no es necesaria con el nuevo enfoque.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // ... Configuración de Toolbar, Toggle y Header (sin cambios) ...
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvUsuarioLogueado);
        SharedPreferences prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String emailUsuario = prefs.getString("email", "Usuario Invitado");

        if (tvNombre != null) {
            tvNombre.setText("Usuario: " + emailUsuario);
        }
        // ... Fin de configuración ...

        // Fragment inicial
        if (savedInstanceState == null) {
            loadFragment(new InicioFragment(), "Inicio");
        }

        // FloatingActionButton (sin cambios)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // tryLoadFragment(new AlgúnFragmento(), "Título");
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
                fragmentToLoad = new InquilinosFragment();
                title = "Inquilinos";
            } else if (id == R.id.nav_logout) {
                mostrarDialogoLogout();
                drawerLayout.closeDrawers();
                return true;
            }

            if (fragmentToLoad != null) {
                // LLAMADA CLAVE: Usamos el método unificado para la carga segura
                loadFragmentWithCleanup(fragmentToLoad, title);
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Método público para cargar un fragmento con Bundle (llamado desde ContratosFragment)
    public void navigateToFragment(Fragment fragment, String title, Bundle args) {
        // 1. Asignar los argumentos al fragmento
        fragment.setArguments(args);
        // 2. Usar tu método de carga existente
        loadFragment(fragment, title);
        // 3. Opcional: Cerrar el Drawer si está abierto
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
        }
    }

    private void loadFragmentWithCleanup(Fragment nextFragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 1. Obtener el fragmento actualmente visible
        Fragment currentFragment = fragmentManager.findFragmentById(CONTAINER_ID);

        // 2. Si el fragmento actual es el mapa, necesitamos REMOVERLO explícitamente.
        // Un simple 'replace' a veces no es suficiente para que el mapa libere la GPU.
        if (currentFragment instanceof InicioFragment) {
            // Remover el InicioFragment (que contiene el mapa)
            fragmentManager.beginTransaction()
                    .remove(currentFragment)
                    .commitNow(); // commitNow() garantiza que el removal se complete antes de la siguiente carga

            // El mapa interno del SupportMapFragment se destruirá en este proceso.
        }

        // 3. Cargar el nuevo fragmento
        loadFragment(nextFragment, title);
    }

    // Método de carga real
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(CONTAINER_ID, fragment)
                .commitAllowingStateLoss(); // commitAllowingStateLoss es válido en este contexto

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