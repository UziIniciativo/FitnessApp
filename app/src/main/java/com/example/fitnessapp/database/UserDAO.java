package com.example.fitnessapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class UserDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Registrar un usuario nuevo
    public long registerUser(String username, String password) {
        if (userExists(username)) {
            return -1; // Usuario ya existe
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        try {
            return database.insert(DatabaseHelper.TABLE_USERS, null, values);
        } catch (SQLiteException e) {
            Log.e("UserDAO", "Error al registrar usuario", e);
            return -1;
        }
    }

    // Validar usuario y contraseña
    public boolean validateUser(String username, String password) {
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                    " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                    DatabaseHelper.COLUMN_PASSWORD + " = ?";
            String[] args = {username, password};
            cursor = database.rawQuery(query, args);
            return cursor.moveToFirst();
        } catch (SQLiteException e) {
            Log.e("UserDAO", "Error al validar usuario", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Verificar si un usuario ya existe
    public boolean userExists(String username) {
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                    " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?";
            cursor = database.rawQuery(query, new String[]{username});
            return cursor.moveToFirst();
        } catch (SQLiteException e) {
            Log.e("UserDAO", "Error al verificar existencia de usuario", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Actualizar última fecha de inicio de sesión y racha
    public void updateLastLoginAndStreak(String username) {
        SQLiteDatabase db = this.database;

        // Fecha actual
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Consultar última fecha y racha
        String query = "SELECT " + DatabaseHelper.COLUMN_LAST_LOGIN_DATE + ", " +
                DatabaseHelper.COLUMN_STREAK + " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            // Validar existencia de columnas
            String lastLoginDate = null;
            int streak = 0;

            int lastLoginIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_LOGIN_DATE);
            if (lastLoginIndex != -1) {
                lastLoginDate = cursor.getString(lastLoginIndex);
            } else {
                Log.e("UserDAO", "La columna 'last_login_date' no existe.");
            }

            int streakIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_STREAK);
            if (streakIndex != -1) {
                streak = cursor.getInt(streakIndex);
            } else {
                Log.e("UserDAO", "La columna 'streak' no existe.");
            }

            // Calcular nueva racha
            if (lastLoginDate != null) {
                LocalDate lastLogin = LocalDate.parse(lastLoginDate);
                LocalDate today = LocalDate.parse(currentDate);

                if (lastLogin.plusDays(1).equals(today)) {
                    streak++; // Incrementar racha
                } else if (!lastLogin.equals(today)) {
                    streak = 1; // Reiniciar racha
                }
            }

            // Actualizar base de datos
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_LAST_LOGIN_DATE, currentDate);
            values.put(DatabaseHelper.COLUMN_STREAK, streak);

            db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{username});
        } else {
            Log.e("UserDAO", "Usuario no encontrado al actualizar racha.");
        }

        cursor.close();
    }

    // Obtener racha del usuario
    public int getUserStreak(String username) {
        Cursor cursor = null;
        int streak = 0;

        try {
            String query = "SELECT " + DatabaseHelper.COLUMN_STREAK + " FROM " + DatabaseHelper.TABLE_USERS +
                    " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?";
            cursor = database.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()) {
                int streakIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_STREAK);
                if (streakIndex != -1) {
                    streak = cursor.getInt(streakIndex);
                } else {
                    Log.e("UserDAO", "La columna 'streak' no existe.");
                }
            }
        } catch (SQLiteException e) {
            Log.e("UserDAO", "Error al obtener la racha del usuario", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return streak;
    }

    // Cerrar la base de datos
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}

