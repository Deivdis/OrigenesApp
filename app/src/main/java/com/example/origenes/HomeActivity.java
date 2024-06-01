package com.example.origenes;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.stream.Collectors;
public class HomeActivity extends AppCompatActivity implements CategoriaAdapter.OnCategoriaClickListener {

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
    private List<Integer> imageResources;
    private TextView textViewVerTodos;

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
        textViewVerTodos = findViewById(R.id.textView8);

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
        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSessionActive(false);
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Configurar el OnClickListener para textViewVerTodos
        textViewVerTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productoAdapter.setProductos(productosList);
            }
        });

        // Configurar el SearchView para buscar productos
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productoAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productoAdapter.filter(newText);
                return false;
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
                sliderHandler.postDelayed(this, 5000);
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

    @Override
    public void onCategoriaClick(Categoria categoria) {
        List<Producto> filteredProductos = productosList.stream()
                .filter(producto -> producto.getCategoriaId() == categoria.getId())
                .collect(Collectors.toList());
        productoAdapter.setProductos(filteredProductos);
    }
}
