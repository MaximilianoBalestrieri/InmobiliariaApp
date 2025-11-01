package com.tec.inmobiliariaapp.ui.login;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tec.inmobiliariaapp.databinding.ActivityLoginBinding;

// Implementamos SensorEventListener para detectar el movimiento
public class LoginActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityLoginBinding binding;
    private LoginActivityViewModel vm;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // Variables para la detección de agitación (Shake)
    private static final float SHAKE_THRESHOLD = 15f; // Sensibilidad de la agitación
    private static final int SHAKE_TIME_MS = 500; // Tiempo mínimo entre agitaciones
    private long mLastShakeTime = 0;

    // Constante para el número de teléfono
    private static final String NUMERO_INMOBILIARIA = "1169761345";

    // Launcher para manejar el resultado de la solicitud de permiso
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permiso concedido, intentamos la llamada de nuevo
                    vm.llamarInmobiliaria(NUMERO_INMOBILIARIA); // con el numero que se instanció antes.
                } else {
                    Toast.makeText(this, "Permiso de llamada denegado.", Toast.LENGTH_LONG).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        super.setContentView(binding.getRoot());

        // **Inicialización del sensor**
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // **Observadores del ViewModel**


        vm.getMMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        // **Observa la solicitud de permiso de llamada (activada desde el ViewModel)
        vm.getMSolicitarPermisoLlamada().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                solicitarPermisoLlamada();
            }
        });

        //  Observa la notificación de llamada iniciada
        vm.getMLlamadaIniciada().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean llamadaExitosa) {
                if (llamadaExitosa) {
                    Toast.makeText(LoginActivity.this, "Llamando a la inmobiliaria...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica del botón de login
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usu=binding.etEmail.getText().toString();
                String contra=binding.etPassword.getText().toString();
                vm.logueo(usu, contra);
            }
        });
    }

    // --- Métodos de ciclo de vida para registrar/desregistrar el sensor ---

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAccelerometer != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    // --- Implementación de SensorEventListener para la agitación ---

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Cálculo de la fuerza del movimiento, restando la gravedad (SensorManager.GRAVITY_EARTH)
            double acceleration = Math.sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH;

            // Detección de agitación y control de tiempo para evitar múltiples llamadas
            long currentTime = System.currentTimeMillis();
            if (acceleration > SHAKE_THRESHOLD && (currentTime - mLastShakeTime > SHAKE_TIME_MS)) {
                mLastShakeTime = currentTime;

                // Agitación detectada, intentar llamar
                vm.llamarInmobiliaria(NUMERO_INMOBILIARIA);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar para la detección de agitación
    }

    // --- Lógica de Permisos ---

    private void solicitarPermisoLlamada() {
        // Si el permiso no está concedido, lo solicitamos usando el launcher
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE);
        }
    }
}