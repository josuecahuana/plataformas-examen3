package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AccountActivity.ACCOUNT_ACEPTAR) {
                        Intent data = result.getData();
                        if (data != null) {
                            String accountJson = data.getStringExtra(AccountActivity.ACCOUNT_RECORD);
                            Toast.makeText(this, "Cuenta creada: " + accountJson, Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == AccountActivity.ACCOUNT_CANCELAR) {
                        Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        // Referencias a los campos de texto y botones
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnAddAccount = findViewById(R.id.btnAddAccount);

        // Acción al presionar el botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (checkLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                    // Navegar a otra actividad si es necesario
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción al presionar el botón de crear cuenta
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AccountActivity.class);
                // ActivityResultLauncher<Intent> activityResultLauncher;
                activityResultLauncher.launch(intent);
            }
        });
    }

    /**
     * Método para verificar el login con el archivo logininfo.txt
     */
    private boolean checkLogin(String username, String password) {
        File file = new File(getFilesDir(), "logininfo.txt"); // Ubicación del archivo en el almacenamiento interno

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Separar usuario y contraseña
                if (parts.length == 2) {
                    String storedUsername = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true; // Credenciales coinciden
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer el archivo de login", Toast.LENGTH_SHORT).show();
        }

        return false; // No se encontraron coincidencias
    }
}
