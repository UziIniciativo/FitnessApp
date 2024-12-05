package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.fitnessapp.database.ProfileDAO;

public class Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Inicializar ProfileDAO
        ProfileDAO profileDAO = new ProfileDAO(this);

        // Obtener el nombre de usuario desde el intent
        String username = getIntent().getStringExtra("username");


        // Referencias a los campos
        EditText nameEditText = findViewById(R.id.nameEditText);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        EditText heightEditText = findViewById(R.id.heightEditText);
        EditText weightEditText = findViewById(R.id.weightEditText);
        EditText ageEditText = findViewById(R.id.ageEditText);

        // Botón "Continuar"
        findViewById(R.id.continueButton).setOnClickListener(view -> {
            // Validar datos del formulario
            String name = nameEditText.getText().toString().trim();
            String gender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();
            String heightStr = heightEditText.getText().toString().trim();
            String weightStr = weightEditText.getText().toString().trim();
            String ageStr = ageEditText.getText().toString().trim();

            if (name.isEmpty() || gender.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir datos a sus tipos correspondientes
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            int age = Integer.parseInt(ageStr);

            // Guardar datos del perfil en la base de datos
            profileDAO.insertOrUpdateProfile(username, name, gender, height, weight, age);
            Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_SHORT).show();

            // Redirigir al mapa de progreso
            Intent intent = new Intent(Form.this, MapActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}