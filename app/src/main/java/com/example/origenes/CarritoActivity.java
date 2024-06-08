package com.example.origenes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private OrigenesBD db;
    private RecyclerView recyclerView;
    private CarritoAdapter carritoAdapter;
    private TextView totalTextView;
    private static final String TAG = "CarritoActivity"; // Agregado para logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        db = new OrigenesBD(this);
        recyclerView = findViewById(R.id.recyclerViewCarrito);
        totalTextView = findViewById(R.id.totalTextView);

        obtenerProductos();
    }

    private void obtenerProductos() {
        // Recuperar el ID del usuario de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        // Si no hay un ID de usuario, eso significa que no hay sesión activa
        if (currentUserId == -1) {
            // Aquí puedes manejar el caso de usuario no encontrado o no logueado
            Log.e(TAG, "No user ID found, user might not be logged in");
            return;
        }

        // Obtener los productos del carrito para el usuario actual
        List<Producto> productosEnCarrito = db.obtenerProductosDelCarrito(currentUserId);

        imprimirProductos(productosEnCarrito); // Imprime los productos para verificación
        carritoAdapter = new CarritoAdapter(productosEnCarrito);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carritoAdapter);

        calcularTotal(productosEnCarrito);
    }

    private void imprimirProductos(List<Producto> productosEnCarrito) {
        for (Producto producto : productosEnCarrito) {
            Log.d(TAG, "Producto ID: " + producto.getId());
            Log.d(TAG, "Nombre: " + producto.getNombre());
            Log.d(TAG, "Descripción: " + producto.getDescripcion());
            Log.d(TAG, "Precio: " + producto.getPrecio());
            Log.d(TAG, "Cantidad: " + producto.getCantidad());
            Log.d(TAG, "Image Resource ID: " + producto.getImageResourceId());
        }
    }

    private void calcularTotal(List<Producto> productosEnCarrito) {
        double total = 0;
        for (Producto producto : productosEnCarrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        totalTextView.setText(String.format("Total: $%.2f", total));
    }
}