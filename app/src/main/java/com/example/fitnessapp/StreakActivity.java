package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.adapters.StreakCalendarAdapter;
import com.example.fitnessapp.database.UserDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class StreakActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        // Obtener el username del Intent
        username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("StreakActivity", "Error: Usuario no especificado.");
            finish();
            return;
        }

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.streakCalendarRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columnas para simular un calendario

        // Simular datos para las fechas
        List<String> dates = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            dates.add(String.valueOf(i)); // Días del mes
        }

        // Crear instancia de UserDAO
        UserDAO userDAO = new UserDAO(this);

        // Obtener la longitud real de la racha desde la base de datos
        int streakLength = userDAO.getUserStreak(username);

        // Configurar el adaptador con el contexto, las fechas y la longitud de la racha
        StreakCalendarAdapter adapter = new StreakCalendarAdapter(this, dates, streakLength);
        recyclerView.setAdapter(adapter);

        // Configurar BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_progress);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                // Navegar a MapActivity
                Intent homeIntent = new Intent(StreakActivity.this, MapActivity.class);
                homeIntent.putExtra("username", username);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (id == R.id.navigation_progress) {
                // Ya estamos en StreakActivity
                return true;
            } else if (id == R.id.navigation_profile) {
                // Navegar a ProfileActivity
                Intent profileIntent = new Intent(StreakActivity.this, ProfileActivity.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                finish();
                return true;
            }
            return false;
        });
    }
}


