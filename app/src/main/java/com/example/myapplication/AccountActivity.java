package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AccountActivity extends AppCompatActivity {
    public final static String ACCOUNT_RECORD = "ACCOUNT_RECORD";
    public final static Integer ACCOUNT_ACEPTAR = 100;
    public final static Integer ACCOUNT_CANCELAR = 200;

    private EditText edtUsername, edtPassword;
    private boolean isPasswordVisible = false;  // Variable para saber si la contraseña es visible o no
    private BroadcastReceiver keyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAceptar = findViewById(R.id.btnAceptar);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGenerarClave = findViewById(R.id.btnGenerarClave);
        Button btnMostrarContraseña = findViewById(R.id.btnMostrarContraseña);  // Botón de mostrar/ocultar contraseña

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        // Acción del botón Aceptar
        btnAceptar.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                saveAccountToFile(username, password);

                Intent data = new Intent();
                data.putExtra(ACCOUNT_RECORD, username);
                setResult(ACCOUNT_ACEPTAR, data);
                finish();
            } else {
                // Mostrar error si faltan campos
                edtUsername.setError("El usuario no puede estar vacío");
                edtPassword.setError("La contraseña no puede estar vacía");
            }
        });

        // Acción del botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            setResult(ACCOUNT_CANCELAR);
            finish();
        });

        // Acción del botón Generar Clave
        btnGenerarClave.setOnClickListener(v -> {
            // Crear una instancia del generador de claves
            generadorDeClaves generador = new generadorDeClaves();
            // Generar la clave
            String nuevaClave = generador.generarClave();
            // Asignar la clave generada al campo de contraseña
            edtPassword.setText(nuevaClave);
        });

                /*
        // Acción del botón Generar Clave
        btnGenerarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del generador de claves
                generadorDeClaves generador = new generadorDeClaves();
                // Generar la clave
                String nuevaClave = generador.generarClave();
                // Asignar la clave generada al campo de contraseña
                edtPassword.setText(nuevaClave);
            }
        });*/

        // Acción del botón Mostrar/Ocultar Contraseña
        btnMostrarContraseña.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Si la contraseña es visible, ocultarla
                edtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                btnMostrarContraseña.setText("Mostrar Contraseña");
            } else {
                // Si la contraseña está oculta, mostrarla
                edtPassword.setTransformationMethod(null);  // Mostrar texto sin transformación
                btnMostrarContraseña.setText("Ocultar Contraseña");
            }

            // Alternar el estado
            isPasswordVisible = !isPasswordVisible;
        });

        // Registrar el receptor local
        keyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && "com.example.myapplication.KEY_GENERATED".equals(intent.getAction())) {
                    String generatedKey = intent.getStringExtra("GENERATED_KEY");
                    if (generatedKey != null) {
                        edtPassword.setText(generatedKey);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(keyReceiver, new IntentFilter("com.example.myapplication.KEY_GENERATED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistrar el receptor local
        LocalBroadcastManager.getInstance(this).unregisterReceiver(keyReceiver);
    }

    /**
     * Guarda las credenciales en un archivo `logininfo.txt`.
     * Si el archivo no existe, lo crea.
     */
    private void saveAccountToFile(String username, String password) {
        File file = new File(getFilesDir(), "logininfo.txt"); // Ruta del archivo en almacenamiento interno

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Escribir las credenciales en formato "usuario,contraseña"
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
