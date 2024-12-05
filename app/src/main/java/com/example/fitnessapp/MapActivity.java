package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String username = getIntent().getStringExtra("username");
        Log.d("MapActivity", "Username recibido: " + username);

        if (username == null || username.isEmpty()) {
            Log.e("MapActivity", "Error: Usuario no especificado.");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                // Acción para navegar a la sección de Inicio
                return true;
            } else if (id == R.id.navigation_progress) {
                // Acción para navegar a la sección de Progreso
                Intent progressIntent = new Intent(MapActivity.this, StreakActivity.class);
                progressIntent.putExtra("username", username);
                startActivity(progressIntent);
                finish(); // Cerrar actividad actual
                return true;
            } else if (id == R.id.navigation_profile) {
                // Acción para navegar a la sección de Perfil
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                intent.putExtra("username", username); // Aquí se pasa el username al perfil
                startActivity(intent);
                return true;
            }
            return false;
        });

    }
}