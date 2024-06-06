package com.example.origenes;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Locale;

public class vistaProducto extends AppCompatActivity {

    private TextView txtNombreProducto;
    private TextView txtDescripcionProducto;
    private TextView txtPrecioProducto;
    private ImageView imgProducto;
    private TextView textViewQuantity;
    private ImageView imageViewIncrease, imageViewDecrease;
    private int quantity = 1;
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
        textViewQuantity = findViewById(R.id.textView142);
        imageViewIncrease = findViewById(R.id.imageView9);
        imageViewDecrease = findViewById(R.id.imageView82);

        // Inicializar la base de datos
        db = new OrigenesBD(this);

        // Obtener datos del Intent
        Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombreProducto");
        String descripcionProducto = intent.getStringExtra("descripcionProducto");
        double precioProducto = intent.getDoubleExtra("precioProducto", 0.0);
        int imagenProducto = intent.getIntExtra("imagenProducto", -1);
        int productoId = intent.getIntExtra("productoId", -1); // Asegúrate de que el ID del producto se pase también

        // Configurar UI con los datos del producto
        txtNombreProducto.setText(nombreProducto);
        txtDescripcionProducto.setText(descripcionProducto);
        txtPrecioProducto.setText(String.format(Locale.getDefault(), "%.2f", precioProducto));
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
            // Verificar que el productoId sea válido
            if (productoId == -1) {
                Toast.makeText(vistaProducto.this, "Error: Producto no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el ID del usuario y el producto
            int usuarioId = 1; // Reemplaza con el ID del usuario actual
            int cantidad = quantity;

            // Agregar el producto al carrito en la base de datos
            db.agregarProductoAlCarrito(usuarioId, productoId, cantidad);

            // Mostrar mensaje de confirmación
            Toast.makeText(vistaProducto.this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();

            // Abrir el carrito
            abrirCarrito();
        });

        ImageView carritoImageView = findViewById(R.id.imageView6);
        carritoImageView.setOnClickListener(v -> {
            abrirCarrito();
        });

        // Configurar los listeners para incrementar y disminuir la cantidad
        imageViewIncrease.setOnClickListener(v -> {
            quantity++;
            textViewQuantity.setText(String.valueOf(quantity));
        });

        imageViewDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textViewQuantity.setText(String.valueOf(quantity));
            }
        });
    }

    private void abrirCarrito() {
        // Abrir la actividad del carrito con los productos agregados
        Intent carritoIntent = new Intent(vistaProducto.this, CarritoActivity.class);
        carritoIntent.putStringArrayListExtra("productosEnCarrito", productosEnCarrito);
        startActivity(carritoIntent);
    }
}
