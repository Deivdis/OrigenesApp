package com.example.origenes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class vistaProducto extends AppCompatActivity {

    private TextView txtNombreProducto;
    private TextView txtDescripcionProducto;
    private TextView txtPrecioProducto;
    private ImageView imgProducto;
    private OrigenesBD db;
    private ArrayList<String> productosEnCarrito;

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

        // Obtener datos del Intent
        Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombreProducto");
        String descripcionProducto = intent.getStringExtra("descripcionProducto");
        String precioProducto = intent.getStringExtra("precioProducto");
        int imagenProducto = intent.getIntExtra("imagenProducto", -1);

        // Configurar UI con los datos del producto
        txtNombreProducto.setText(nombreProducto);
        txtDescripcionProducto.setText(descripcionProducto);
        txtPrecioProducto.setText(precioProducto);
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
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(vistaProducto.this, HomeActivity.class);
            startActivity(backIntent);
        });

        // Inicializar lista de productos en el carrito
        productosEnCarrito = new ArrayList<>();

        // Configurar botón de agregar al carrito
        AppCompatButton buttonAddToCart = findViewById(R.id.button2);
        buttonAddToCart.setOnClickListener(v -> {
            agregarProductoAlCarrito(nombreProducto, precioProducto);
            abrirCarrito();
        });

        ImageView carritoImageView = findViewById(R.id.imageView6);
        carritoImageView.setOnClickListener(v -> {
            abrirCarrito();
        });
    }

    private void agregarProductoAlCarrito(String nombreProducto, String precioProducto) {
        // Lógica para agregar el producto al carrito
        String producto = nombreProducto + " - " + precioProducto;
        productosEnCarrito.add(producto);
    }

    private void abrirCarrito() {
        // Abrir la actividad del carrito con los productos agregados
        Intent carritoIntent = new Intent(vistaProducto.this, CarritoActivity.class);
        carritoIntent.putStringArrayListExtra("productosEnCarrito", productosEnCarrito);
        startActivity(carritoIntent);
    }
}
