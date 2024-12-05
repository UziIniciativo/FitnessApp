package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitnessapp.database.DatabaseHelper;
import com.example.fitnessapp.database.UserDAO;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        dbHelper.clearTable(DatabaseHelper.TABLE_USERS);
//        Toast.makeText(this, "Registros eliminados para depuración.", Toast.LENGTH_SHORT).show();


        findViewById(R.id.loginButton).setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                UserDAO userDAO = new UserDAO(this);

                if (userDAO.validateUser(username, password)) {
                    Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Username enviado a MapActivity: " + username);
                    userDAO.updateLastLoginAndStreak(username);
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                    Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });


        // Botón "Crear cuenta"
        findViewById(R.id.registerText).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}