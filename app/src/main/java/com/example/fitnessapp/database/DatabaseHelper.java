package com.example.fitnessapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_app.db";
    private static final int DATABASE_VERSION = 3; // Incrementa la versión al hacer cambios

    // Tabla de usuarios
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_LAST_LOGIN_DATE = "last_login_date";
    public static final String COLUMN_STREAK = "streak";

    // Crear tabla de usuarios
    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_LAST_LOGIN_DATE + " TEXT, " +
                    COLUMN_STREAK + " INTEGER DEFAULT 0);";


    // Tabla de perfiles de usuario
    public static final String TABLE_PROFILE = "user_profile";
    public static final String COLUMN_PROFILE_ID = "id";
    public static final String COLUMN_PROFILE_USERNAME = "username";
    public static final String COLUMN_PROFILE_NAME = "name";
    public static final String COLUMN_PROFILE_GENDER = "gender";
    public static final String COLUMN_PROFILE_HEIGHT = "height";
    public static final String COLUMN_PROFILE_WEIGHT = "weight";
    public static final String COLUMN_PROFILE_AGE = "age";

    // Crear tabla de perfiles de usuario
    private static final String TABLE_PROFILE_CREATE =
            "CREATE TABLE " + TABLE_PROFILE + " (" +
                    COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROFILE_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_NAME + " TEXT, " +
                    COLUMN_PROFILE_GENDER + " TEXT, " +
                    COLUMN_PROFILE_HEIGHT + " REAL, " +
                    COLUMN_PROFILE_WEIGHT + " REAL, " +
                    COLUMN_PROFILE_AGE + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_PROFILE_USERNAME + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USERNAME + ") ON DELETE CASCADE ON UPDATE CASCADE);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_PROFILE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas antiguas si existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Crear las tablas nuevamente
        onCreate(db);
    }

    // Método para borrar datos y reiniciar IDs
    public void clearTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName); // Borra todos los registros
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + tableName + "'"); // Reinicia AUTOINCREMENT
        db.close();
    }
}
