package com.example.origenes;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private ListView listaCarrito;
    private Button buttonComprar;
    private ArrayList<String> productosEnCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        listaCarrito = findViewById(R.id.listaCarrito);
        buttonComprar = findViewById(R.id.buttonComprar);

        // Obtener los productos del Intent
        productosEnCarrito = getIntent().getStringArrayListExtra("productosEnCarrito");

        // Crear un adaptador para mostrar los productos en el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosEnCarrito);
        listaCarrito.setAdapter(adapter);

        // Configurar el botón de comprar
        buttonComprar.setOnClickListener(v -> {
            // Lógica para proceder a la compra
            Toast.makeText(CarritoActivity.this, "Procediendo a la compra...", Toast.LENGTH_SHORT).show();
        });
    }
}
