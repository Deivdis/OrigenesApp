package com.example.origenes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class vistaProducto extends AppCompatActivity {

    private static final String TAG = "vistaProducto"; // Tag para los logs

    private TextView txtNombreProducto;
    private TextView txtDescripcionProducto;
    private TextView txtPrecioProducto;
    private ImageView imgProducto;
    private OrigenesBD db;
    private TextView cantidadTextView;
    private int cantidad = 1; // Cantidad inicial por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        // Configuración inicial
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicialización de componentes de UI
        imgProducto = findViewById(R.id.imgProducto);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtDescripcionProducto = findViewById(R.id.txtDescripcionProducto);
        txtPrecioProducto = findViewById(R.id.txtPrecioProducto);
        cantidadTextView = findViewById(R.id.textView142);
        ImageView imageViewIncrement = findViewById(R.id.imageView9);
        ImageView imageViewDecrement = findViewById(R.id.imageView82);
        AppCompatButton buttonAddToCart = findViewById(R.id.button2);
        ImageView backButton = findViewById(R.id.back);
        ImageView carritoImageView = findViewById(R.id.imageView6);

        // Inicialización de la base de datos
        db = new OrigenesBD(this);

        // Obtener datos del Intent
        Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombreProducto");
        String descripcionProducto = intent.getStringExtra("descripcionProducto");
        double precioProducto = intent.getDoubleExtra("precioProducto", 0.0);
        int imagenProducto = intent.getIntExtra("imagenProducto", -1);
        int idProducto = intent.getIntExtra("idProducto", -1);

        // Configurar UI con los datos del producto
        txtNombreProducto.setText(nombreProducto);
        txtDescripcionProducto.setText(descripcionProducto);
        txtPrecioProducto.setText(String.valueOf(precioProducto));
        if (imagenProducto != -1) {
            imgProducto.setImageResource(imagenProducto);
        }

        // Configurar ImageView para incrementar la cantidad
        imageViewIncrement.setOnClickListener(v -> {
            cantidad++;
            cantidadTextView.setText(String.valueOf(cantidad));
        });

        // Configurar ImageView para decrementar la cantidad
        imageViewDecrement.setOnClickListener(v -> {
            if (cantidad > 1) { // Evitar que la cantidad sea menor que 1
                cantidad--;
                cantidadTextView.setText(String.valueOf(cantidad));
            }
        });

        // Configurar botón de agregar al carrito
        buttonAddToCart.setOnClickListener(v -> {
            if (idProducto != -1) {
                agregarProductoAlCarrito(idProducto, cantidad);
            } else {
                Toast.makeText(vistaProducto.this, "Error: Producto no válido", Toast.LENGTH_SHORT).show();
            }
        });

        carritoImageView.setOnClickListener(v -> {
            abrirCarrito();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void agregarProductoAlCarrito(int productoId, int cantidad) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1); // Obtener el ID del usuario actual

        if (currentUserId != -1) {
            if (db.productoExisteEnCarrito(currentUserId, productoId)) {
                int cantidadActual = db.obtenerCantidadProductoEnCarrito(currentUserId, productoId);
                db.actualizarCantidadProductoEnCarrito(currentUserId, productoId, cantidadActual + cantidad);
            } else {
                db.agregarProductoAlCarrito(currentUserId, productoId, cantidad);
            }
            Toast.makeText(vistaProducto.this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(vistaProducto.this, "Usuario no identificado, por favor inicia sesión.", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirCarrito() {
        Intent carritoIntent = new Intent(vistaProducto.this, CarritoActivity.class);
        startActivity(carritoIntent);
    }
}