package com.example.fitnessapp;

import android.os.Bundle;

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

        String userName = getIntent().getStringExtra("userName");
        String userGoal = getIntent().getStringExtra("userGoal");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                // Acción para navegar a la sección de Inicio
                return true;
            } else if (id == R.id.navigation_progress) {
                // Acción para navegar a la sección de Progreso
                return true;
            } else if (id == R.id.navigation_profile) {
                // Acción para navegar a la sección de Perfil
                return true;
            }
            return false;
        });

    }
}