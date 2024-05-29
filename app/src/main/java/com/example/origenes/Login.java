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

                // Verificar si el usuario existe en la base de datos
                Cursor cursor = db.rawQuery("SELECT " + OrigenesBD.COLUMNA_CONTRASENA + " FROM " + OrigenesBD.TABLA_USUARIOS + " WHERE " + OrigenesBD.COLUMNA_CORREO + " = ?", new String[]{correo});

                if (cursor.moveToFirst()) {
                    // Obtener la contraseña hash almacenada en la base de datos usando el índice de la columna directamente
                    String hashedPassword = cursor.getString(0);

                    if (hashedPassword.equals(contrasena)) { // Aquí asumimos que las contraseñas están almacenadas en texto plano
                        // Guardar el estado de sesión activa en SharedPreferences
                        setSessionActive(true);

                        // Si la contraseña es correcta, iniciar la siguiente actividad
                        goToHomeActivity();
                    } else {
                        // Si la contraseña es incorrecta, mostrar un mensaje de error
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si el usuario no existe, mostrar un mensaje de error
                    Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el cursor
                cursor.close();
            }
        });

        Btn1Ccuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de registro
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cerrar la base de datos
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

    private void goToHomeActivity() {
        Intent intent = new Intent(Login.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Si la contraseña es visible, establecer el tipo de entrada como texto sin ocultar
            et2Contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Si la contraseña no es visible, establecer el tipo de entrada como texto oculto
            et2Contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // Mover el cursor al final del texto para mantener la posición del cursor
        et2Contraseña.setSelection(et2Contraseña.getText().length());
    }
}
