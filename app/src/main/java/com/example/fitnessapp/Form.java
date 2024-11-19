package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        findViewById(R.id.continueButton).setOnClickListener(view -> {
            Intent intent = new Intent(Form.this, MapActivity.class);
            intent.putExtra("userName", "John Doe");
            intent.putExtra("userGoal", "Perder peso");
            startActivity(intent);
        });
    }
}