package com.example.origenes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class vistaProducto extends AppCompatActivity {

    TextView txtNombreProducto;
    TextView txtDescripcionProducto;
    TextView txtPrecioProducto;
    ImageView imgProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_producto);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imgProducto = findViewById(R.id.imgProducto);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtDescripcionProducto = findViewById(R.id.txtDescripcionProducto);
        txtPrecioProducto = findViewById(R.id.txtPrecioProducto);

        Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombreProducto");
        String descripcionProducto = intent.getStringExtra("descripcionProducto");
        String precioProducto = intent.getStringExtra("precioProducto");
        int imagenProducto = intent.getIntExtra("imagenProducto", -1); // -1 es el valor predeterminado en caso de que no se encuentre

        txtNombreProducto.setText(nombreProducto);
        txtDescripcionProducto.setText(descripcionProducto);
        txtPrecioProducto.setText(precioProducto);
        if (imagenProducto != -1) {
            imgProducto.setImageResource(imagenProducto);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}