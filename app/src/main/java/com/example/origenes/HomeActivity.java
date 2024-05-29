package com.example.origenes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerSlider;
    private ProgressBar progressBarBanner;
    private ProgressBar progressBarOfficial;
    private RecyclerView recyclerViewProducto;
    private ProgressBar progressBarPopular;
    private RecyclerView recyclerViewCategorias;
    private ProductoAdapter productoAdapter;
    private CategoriaAdapter categoriaAdapter;
    private List<Producto> productosList;
    private List<Categoria> categoriasList;
    private OrigenesBD databaseHelper;
    private SharedPreferences sharedPref;
    private Handler sliderHandler;
    private List<Integer> imageResources; // Cambiado: lista de recursos de imágenes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPref = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        viewPagerSlider = findViewById(R.id.viewPagerSlider);
        progressBarBanner = findViewById(R.id.progressBarBanner);
        progressBarOfficial = findViewById(R.id.progressBarOfficial);
        recyclerViewProducto = findViewById(R.id.recyclerViewProducto);
        progressBarPopular = findViewById(R.id.progressBarPopular);
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);

        // Configurar el RecyclerView de productos
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar el RecyclerView de categorías
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Inicializar las listas de productos y categorías
        productosList = new ArrayList<>();
        categoriasList = new ArrayList<>();

        // Inicializar el adaptador de productos
        productoAdapter = new ProductoAdapter(productosList, this);
        recyclerViewProducto.setAdapter(productoAdapter);

        // Inicializar el adaptador de categorías
        categoriaAdapter = new CategoriaAdapter(categoriasList, this);
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Obtener los productos y categorías de la base de datos
        databaseHelper = new OrigenesBD(this);
        obtenerProductos();
        obtenerCategorias();
        // Ocultar la barra de progreso una vez que se hayan cargado los productos (simulado aquí)
        progressBarPopular.setVisibility(View.GONE);

        // Configuración del ViewPager y ProgressBar del banner
        imageResources = databaseHelper.obtenerImagenesSlider();
        SliderAdapter sliderAdapter = new SliderAdapter(this, imageResources);
        viewPagerSlider.setAdapter(sliderAdapter);

        // Ocultar la ProgressBar después de configurar el adaptador
        progressBarBanner.setVisibility(View.GONE);
        progressBarOfficial.setVisibility(View.GONE);

        // Iniciar el auto deslizamiento del slider
        sliderHandler = new Handler(Looper.getMainLooper());
        startAutoSlide();

        // Configurar el botón de cerrar sesión
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión: eliminar el indicador de sesión activa en SharedPreferences
                setSessionActive(false);

                // Redirigir al LoginActivity después de cerrar sesión
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
                finish(); // Opcional: Finalizar la actividad actual
            }
        });
    }

    private void startAutoSlide() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPagerSlider.getCurrentItem();
                int nextItem = (currentItem + 1) % imageResources.size();
                viewPagerSlider.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 5000); // Cambia la imagen cada 5 segundos
            }
        };
        sliderHandler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacksAndMessages(null);
    }

    private void setSessionActive(boolean active) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sessionActive", active);
        editor.apply();
    }

    private void obtenerProductos() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OrigenesBD.TABLA_PRODUCTOS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_NOMBRE));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_DESCRIPCION));
                String precio = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_PRECIO));
                int categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_CATEGORIA_ID));
                int imagenRecurso = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_IMAGEN_RECURSO));

                Producto producto = new Producto(id, nombre, descripcion, precio, categoriaId, imagenRecurso);
                productosList.add(producto);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        productoAdapter.notifyDataSetChanged();
    }

    private void obtenerCategorias() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OrigenesBD.TABLA_CATEGORIAS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_CATEGORIA_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_CATEGORIA_NOMBRE));
                int imagenRecurso = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_CATEGORIA_IMAGEN_RECURSO));

                Categoria categoria = new Categoria(id, nombre, imagenRecurso);
                categoriasList.add(categoria);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        categoriaAdapter.notifyDataSetChanged();
    }
}
