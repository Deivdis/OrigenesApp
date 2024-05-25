package com.example.origenes;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    private EditText et2Correo, et2Contraseña;
    private Button BtnIngresar, Btn1Ccuenta;
    private SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        et2Correo = findViewById(R.id.et2Correo);
        et2Contraseña = findViewById(R.id.et2Contraseña);
        BtnIngresar = findViewById(R.id.BtnIngresar);
        Btn1Ccuenta = findViewById(R.id.Btn1Ccuenta); // Initialize the new button

        // Abrir la base de datos
        db = openOrCreateDatabase(OrigenesBD.DATABASE_NAME, MODE_PRIVATE, null);

        BtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = et2Correo.getText().toString();
                String contrasena = et2Contraseña.getText().toString();

                // Verificar si el usuario existe en la base de datos
                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM "+OrigenesBD.TABLA_USUARIOS+" WHERE "+OrigenesBD.COLUMNA_CORREO+" = ? AND "+OrigenesBD.COLUMNA_CONTRASENA +" = ?", new String[]{correo, contrasena});

                if (cursor.getCount() > 0) {
                    // Si el usuario existe, iniciar la siguiente actividad
                    Intent intent = new Intent(Login.this, Productos.class);
                    startActivity(intent);
                    finish();
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
        db.close();
    }
}