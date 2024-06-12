package com.example.origenes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText et2Correo, et2Contraseña;
    private Button BtnIngresar, Btn1Ccuenta;
    private SQLiteDatabase db;
    private SharedPreferences sharedPref;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        et2Correo = findViewById(R.id.et2Correo);
        et2Contraseña = findViewById(R.id.et2Contraseña);
        BtnIngresar = findViewById(R.id.BtnIngresar);
        Btn1Ccuenta = findViewById(R.id.Btn1Ccuenta);
        ImageButton btnTogglePasswordVisibility = findViewById(R.id.btnTogglePasswordVisibility);

        // Obtener la instancia de SharedPreferences
        sharedPref = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Abrir la base de datos
        db = openOrCreateDatabase(OrigenesBD.DATABASE_NAME, MODE_PRIVATE, null);

        // Verificar si el usuario ya ha iniciado sesión previamente
        if (isSessionActive()) {
            // Si la sesión está activa, dirigir al usuario directamente a la actividad principal
            goToHomeActivity();
        }

        btnTogglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar la visibilidad de la contraseña al hacer clic en el botón
                isPasswordVisible = !isPasswordVisible;
                togglePasswordVisibility();
            }
        });

        BtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = et2Correo.getText().toString().trim();
                String contrasena = et2Contraseña.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = null;
                try {
                    cursor = db.rawQuery("SELECT " + OrigenesBD.COLUMNA_ID + ", " + OrigenesBD.COLUMNA_CONTRASENA + " FROM " + OrigenesBD.TABLA_USUARIOS + " WHERE " + OrigenesBD.COLUMNA_CORREO + " = ?", new String[]{correo});
                    if (cursor.moveToFirst()) {
                        int userId = cursor.getInt(cursor.getColumnIndex(OrigenesBD.COLUMNA_ID));
                        String hashedPassword = cursor.getString(cursor.getColumnIndex(OrigenesBD.COLUMNA_CONTRASENA));

                        if (hashedPassword.equals(contrasena)) {
                            setSessionActive(true);
                            saveUserId(userId);
                            goToHomeActivity();
                        } else {
                            Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        });

        Btn1Ccuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private void setSessionActive(boolean active) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sessionActive", active);
        editor.apply();
    }

    private boolean isSessionActive() {
        return sharedPref.getBoolean("sessionActive", false);
    }

    private void saveUserId(int userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(Login.this, HomeActivity.class);
        HomeActivity.isIngresar = true;
        startActivity(intent);
        finish();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            et2Contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            et2Contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        et2Contraseña.setSelection(et2Contraseña.getText().length());
    }
}