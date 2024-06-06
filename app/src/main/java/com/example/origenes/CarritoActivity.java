package com.example.origenes;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private ListView listaCarrito;
    private Button buttonComprar;
    private ArrayList<String> productosEnCarrito;
    private OrigenesBD databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        listaCarrito = findViewById(R.id.listaCarrito);
        buttonComprar = findViewById(R.id.buttonComprar);
        databaseHelper = new OrigenesBD(this);

        // Aquí debes obtener el ID del usuario actual, esto es un ejemplo
        int usuarioId = 1; // Reemplaza con el ID del usuario actual

        // Obtener los productos del carrito desde la base de datos
        List<Producto> productosCarrito = databaseHelper.obtenerProductosDelCarrito(usuarioId);

        // Convertir la lista de productos a una lista de strings para el adaptador
        productosEnCarrito = new ArrayList<>();
        for (Producto producto : productosCarrito) {
            productosEnCarrito.add(producto.getNombre() + " - " + producto.getDescripcion() + " - " + producto.getPrecio() + " x " + producto.getCantidad());
        }

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
