package com.example.origenes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Registrar extends AppCompatActivity {

    EditText et2Nombre, et2Apellido, et2Telefono, et2Correo, et2Contraseña;
    Button Btn2;
    MetodosBaseDeDatos dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MetodosBaseDeDatos(this);
        et2Nombre = findViewById(R.id.et2Nombre);
        et2Apellido = findViewById(R.id.et2Apellido);
        et2Telefono = findViewById(R.id.et2Telefono);
        et2Correo = findViewById(R.id.et2Correo);
        et2Contraseña = findViewById(R.id.et2contraseña);
        Btn2 = findViewById(R.id.Btn2);

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = et2Nombre.getText().toString();
                String apellido = et2Apellido.getText().toString();
                String telefono = et2Telefono.getText().toString();
                String correo = et2Correo.getText().toString();
                String clave = et2Contraseña.getText().toString();

                if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                    Toast.makeText(Registrar.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.VerificarUsuario(nombre)) {
                        Toast.makeText(Registrar.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = dbHelper.agregarUsuario(nombre, apellido, telefono, correo, clave);
                        if (id != -1) {
                            Toast.makeText(Registrar.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            et2Nombre.setText("");
                            et2Apellido.setText("");
                            et2Telefono.setText("");
                            et2Correo.setText("");
                            et2Contraseña.setText("");
                        } else {
                            Toast.makeText(Registrar.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}