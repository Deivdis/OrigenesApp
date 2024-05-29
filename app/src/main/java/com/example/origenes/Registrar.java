package com.example.origenes;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.regex.Pattern;

public class Registrar extends AppCompatActivity {

    EditText et2Nombre, et2Apellido, et2Telefono, et2Correo, et2Contraseña, et2ConfirmarContraseña;
    Button Btn2;
    ImageButton btnTogglePasswordVisibility, btnToggleConfirmPasswordVisibility;
    MetodosBaseDeDatos dbHelper;
    boolean isPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
        et2Contraseña = findViewById(R.id.et2Contraseña);
        et2ConfirmarContraseña = findViewById(R.id.et2ConfirmarContraseña);
        Btn2 = findViewById(R.id.Btn2);
        btnTogglePasswordVisibility = findViewById(R.id.btnTogglePasswordVisibility);
        btnToggleConfirmPasswordVisibility = findViewById(R.id.btnToggleConfirmPasswordVisibility);

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = et2Nombre.getText().toString();
                String apellido = et2Apellido.getText().toString();
                String telefono = et2Telefono.getText().toString();
                String correo = et2Correo.getText().toString();
                String clave = et2Contraseña.getText().toString();
                String confirmarClave = et2ConfirmarContraseña.getText().toString();

                if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()) {
                    Toast.makeText(Registrar.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!clave.equals(confirmarClave)) {
                    Toast.makeText(Registrar.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isEmailValid(correo)) {
                    Toast.makeText(Registrar.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPasswordStrong(clave)) {
                    Toast.makeText(Registrar.this, "Contraseña: mínimo 6 caracteres, con mayúscula, minúscula y número.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isPhoneNumberValid(telefono)) {
                    Toast.makeText(Registrar.this, "Número de teléfono no válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.VerificarUsuario(nombre)) {
                    Toast.makeText(Registrar.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                } else {
                    String hashedPassword = hashPassword(clave);
                    long id = dbHelper.agregarUsuario(nombre, apellido, telefono, correo, hashedPassword);
                    if (id != -1) {
                        Toast.makeText(Registrar.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        et2Nombre.setText("");
                        et2Apellido.setText("");
                        et2Telefono.setText("");
                        et2Correo.setText("");
                        et2Contraseña.setText("");
                        et2ConfirmarContraseña.setText("");

                        // Redirigir al LoginActivity después de un registro exitoso
                        Intent intent = new Intent(Registrar.this, Login.class);
                        startActivity(intent);
                        finish(); // Opcional: Finalizar la actividad actual
                    } else {
                        Toast.makeText(Registrar.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnTogglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(et2Contraseña);
                isPasswordVisible = !isPasswordVisible;
            }
        });

        btnToggleConfirmPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(et2ConfirmarContraseña);
                isConfirmPasswordVisible = !isConfirmPasswordVisible;
            }
        });
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isPasswordStrong(String password) {
        int length = password.length();
        return length >= 7 && length <= 15 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find();
    }

    private boolean isPhoneNumberValid(String phone) {
        return phone.length() == 10 && Pattern.matches("\\d{10}", phone);
    }

    private String hashPassword(String password) {
        // Implementa el hash de la contraseña aquí
        return password; // placeholder, reemplazar con el hash real
    }
}
