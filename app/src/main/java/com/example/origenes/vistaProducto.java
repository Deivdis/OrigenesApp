package com.example.origenes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class vistaProducto extends AppCompatActivity {

    private static final String TAG = "vistaProducto"; // Tag para los logs

    private TextView txtNombreProducto;
    private TextView txtDescripcionProducto;
    private TextView txtPrecioProducto;
    private ImageView imgProducto;
    private OrigenesBD db;
    private ArrayList<Producto> productosEnCarrito;

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

//        // Verificar que los datos se están recibiendo correctamente
//        Log.d(TAG, "ID Producto: " + idProducto);
//        Log.d(TAG, "Nombre Producto: " + nombreProducto);
//        Log.d(TAG, "Descripción Producto: " + descripcionProducto);
//        Log.d(TAG, "Precio Producto: " + precioProducto);
//        Log.d(TAG, "Imagen Producto: " + imagenProducto);

        // Configurar UI con los datos del producto
        txtNombreProducto.setText(nombreProducto);
        txtDescripcionProducto.setText(descripcionProducto);
        txtPrecioProducto.setText(String.valueOf(precioProducto));
        if (imagenProducto != -1) {
            imgProducto.setImageResource(imagenProducto);
        }

        // Ajustar márgenes para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar botón de retroceso
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(vistaProducto.this, HomeActivity.class);
            startActivity(backIntent);
        });

        // Inicializar lista de productos en el carrito
        productosEnCarrito = new ArrayList<>();

        // Configurar botón de agregar al carrito
        buttonAddToCart.setOnClickListener(v -> {
            if (idProducto != -1) {
                agregarProductoAlCarrito(idProducto, 1); // Asume cantidad 1 por defecto
                Toast.makeText(vistaProducto.this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(vistaProducto.this, "Error: Producto no válido", Toast.LENGTH_SHORT).show();
            }
        });

        carritoImageView.setOnClickListener(v -> {
            abrirCarrito();
        });
    }

    private void agregarProductoAlCarrito(int productoId, int cantidad) {
        // Lógica para agregar el producto al carrito en la base de datos
        db.agregarProductoAlCarrito(productoId, cantidad);
    }

    private void abrirCarrito() {
        // Abrir la actividad del carrito
        Intent carritoIntent = new Intent(vistaProducto.this, CarritoActivity.class);
        startActivity(carritoIntent);
    }
}