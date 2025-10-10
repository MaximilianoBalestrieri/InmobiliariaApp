package com.tec.inmobiliariaapp.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;


import com.tec.inmobiliariaapp.R;
import com.tec.inmobiliariaapp.model.UsuarioResponse;
import com.tec.inmobiliariaapp.viewModel.LoginViewModel;
public class LoginActivity extends AppCompatActivity {

    // 💡 CORRECCIÓN DE CLARIDAD: Renombramos la variable a etEmail
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 💡 CORRECCIÓN DE CLARIDAD: Asignamos a la variable etEmail
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Creamos el ViewModel con Application
        loginViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(LoginViewModel.class);

        // Observamos la respuesta del login
        loginViewModel.getLoginResponse().observe(this, new Observer<UsuarioResponse>() {
            @Override
            public void onChanged(UsuarioResponse response) {
                // La Activity solo verifica si se recibió ALGO exitoso.
                // El token ya está guardado en el Repository.
                if (response != null) {
                    // 💡 No necesitamos llamar a guardarToken(response)
                    Toast.makeText(LoginActivity.this, "¡Login exitoso!", Toast.LENGTH_SHORT).show();
                    irAMain();
                } else {
                    // 💡 Mantenemos el mensaje de error actualizado
                    Toast.makeText(LoginActivity.this, "Error de autenticación o token no recibido.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLogin.setOnClickListener(v -> {
            String user = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                loginViewModel.login(user, pass);
            }
        });
    }

    private void guardarToken(UsuarioResponse response) {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        prefs.edit().putString("token", response.getToken()).apply();
    }

    private void irAMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}