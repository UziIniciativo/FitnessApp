package com.example.fitnessapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.fitnessapp.models.Profile;

public class ProfileDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ProfileDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insertar o actualizar perfil
    public boolean insertOrUpdateProfile(String username, String name, String gender, double height, double weight, int age) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PROFILE_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PROFILE_NAME, name);
        values.put(DatabaseHelper.COLUMN_PROFILE_GENDER, gender);
        values.put(DatabaseHelper.COLUMN_PROFILE_HEIGHT, height);
        values.put(DatabaseHelper.COLUMN_PROFILE_WEIGHT, weight);
        values.put(DatabaseHelper.COLUMN_PROFILE_AGE, age);

        Cursor cursor = null;
        try {
            // Verificar si ya existe un perfil para el usuario
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_PROFILE + " WHERE " + DatabaseHelper.COLUMN_PROFILE_USERNAME + " = ?";
            cursor = database.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()) {
                // Actualizar perfil existente
                database.update(DatabaseHelper.TABLE_PROFILE, values, DatabaseHelper.COLUMN_PROFILE_USERNAME + " = ?", new String[]{username});
            } else {
                // Insertar nuevo perfil
                database.insert(DatabaseHelper.TABLE_PROFILE, null, values);
            }
            return true; // Operación exitosa
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false; // Error en la operación
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Obtener perfil por nombre de usuario
    public Profile getProfile(String username) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PROFILE + " WHERE " + DatabaseHelper.COLUMN_PROFILE_USERNAME + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            Profile profile = new Profile(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_GENDER)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_HEIGHT)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_AGE))
            );
            cursor.close();
            return profile;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null; // Retorna null si no se encuentra el perfil
    }


    // Método para cerrar la base de datos
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}


