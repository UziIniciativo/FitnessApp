package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitnessapp.database.UserDAO;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.registerButton).setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                UserDAO userDAO = new UserDAO(this);
                long result = userDAO.registerUser(username, password);

                if (result != -1) {
                    Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, Form.class);
                    intent.putExtra("username", username); // Pasar el username al formulario
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });


        // Botón "Iniciar sesión" en la pantalla de Crear Cuenta
        findViewById(R.id.loginText).setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}