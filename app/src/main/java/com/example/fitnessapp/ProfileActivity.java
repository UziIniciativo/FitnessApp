package com.example.fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.database.ProfileDAO;
import com.example.fitnessapp.models.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private ProfileDAO profileDAO;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar DAO y obtener username
        profileDAO = new ProfileDAO(this);
        username = getIntent().getStringExtra("username");
        Log.d("ProfileActivity", "Username recibido: " + username);

        if (username == null || username.isEmpty()) {
            Log.e("ProfileActivity", "Error: Usuario no especificado.");
            Toast.makeText(this, "Error: Usuario no especificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Referencias a los campos
        EditText nameEditText = findViewById(R.id.nameEditText);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        EditText heightEditText = findViewById(R.id.heightEditText);
        EditText weightEditText = findViewById(R.id.weightEditText);
        EditText ageEditText = findViewById(R.id.ageEditText);

        // Cargar datos del perfil
        loadUserProfile(nameEditText, genderRadioGroup, heightEditText, weightEditText, ageEditText);

        // Botón "Guardar Cambios"
        findViewById(R.id.saveProfileButton).setOnClickListener(view -> {
            // Validar y guardar datos actualizados
            String name = nameEditText.getText().toString().trim();
            String gender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();
            String heightStr = heightEditText.getText().toString().trim();
            String weightStr = weightEditText.getText().toString().trim();
            String ageStr = ageEditText.getText().toString().trim();

            if (name.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Convertir datos a sus tipos correspondientes
                double height = Double.parseDouble(heightStr);
                double weight = Double.parseDouble(weightStr);
                int age = Integer.parseInt(ageStr);

                // Guardar datos actualizados en la base de datos
                profileDAO.insertOrUpdateProfile(username, name, gender, height, weight, age);
                Toast.makeText(this, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce valores válidos.", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;
            } else if (id == R.id.navigation_progress) {
                // Acción para navegar a progreso
                Intent progressIntent = new Intent(ProfileActivity.this, StreakActivity.class);
                progressIntent.putExtra("username", username);
                startActivity(progressIntent);
                finish();
                return true;
            } else if (id == R.id.navigation_profile) {
                // Mantenerse en el perfil
                return true;
            }
            return false;
        });

        // Configurar cerrar sesión
        findViewById(R.id.logoutButton).setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void loadUserProfile(EditText nameEditText, RadioGroup genderRadioGroup,
                                 EditText heightEditText, EditText weightEditText, EditText ageEditText) {
        Profile profile = profileDAO.getProfile(username);

        if (profile != null) {
            nameEditText.setText(profile.getName());
            if (profile.getGender().equalsIgnoreCase("Masculino")) {
                genderRadioGroup.check(R.id.maleRadioButton);
            } else if (profile.getGender().equalsIgnoreCase("Femenino")) {
                genderRadioGroup.check(R.id.femaleRadioButton);
            }
            heightEditText.setText(String.valueOf(profile.getHeight()));
            weightEditText.setText(String.valueOf(profile.getWeight()));
            ageEditText.setText(String.valueOf(profile.getAge()));
        } else {
            Toast.makeText(this, "Perfil no encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

}