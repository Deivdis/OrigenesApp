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
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerSlider;
    private ProgressBar progressBarBanner;
    private RecyclerView recyclerViewProducto;
    private ProgressBar progressBarPopular;
    private RecyclerView recyclerViewCategorias;
    private ProductoAdapter productoAdapter;
    private CategoriaAdapter categoriaAdapter;
    private List<Producto> productosList;
    private List<Categoria> categoriasList;
    private OrigenesBD databaseHelper;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        List<String> imageUrls = databaseHelper.obtenerUrlsImagenesSlider();
        SliderAdapter sliderAdapter = new SliderAdapter(this, imageUrls);
        viewPagerSlider.setAdapter(sliderAdapter);

        // Ocultar la ProgressBar después de configurar el adaptador
        progressBarBanner.setVisibility(View.GONE);

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
                String urlImagen = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_URL_IMAGEN));

                Producto producto = new Producto(id, nombre, descripcion, precio, categoriaId, urlImagen);
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
                Categoria categoria = new Categoria(id, nombre);
                categoriasList.add(categoria);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        categoriaAdapter.notifyDataSetChanged();
    }
}
