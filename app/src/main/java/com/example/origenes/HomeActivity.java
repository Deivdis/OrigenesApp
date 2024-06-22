package com.example.origenes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
    public static ProductoAdapter productoAdapter;
    public static boolean isIngresar = false;
    private CategoriaAdapter categoriaAdapter;
    private static List<Producto> productosList = new ArrayList<>();
    private static List<Categoria> categoriasList = new ArrayList<>();
    private OrigenesBD databaseHelper;
    private SharedPreferences sharedPref;
    private Handler sliderHandler;
    private List<Integer> imageResources;
    private TextView textViewVerTodos;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("prueba","cantidad de productos: "+productoAdapter.getItemCount());
            Log.e("lista", "productos: "+productosList.size());
            Log.e("lista categoria", "productos: "+categoriasList.size());
        }
    };

    public static final String ACTION_THEME_CHANGED = "android.intent.action.THEME_CHANGED";

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
        btnLogout.setOnClickListener(v -> {
            setSessionActive(false);
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        });

        // Configurar el OnClickListener para textViewVerTodos
        textViewVerTodos.setOnClickListener(v -> productoAdapter.setProductos(productosList));

        // Configurar el SearchView para buscar productos
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!productosList.isEmpty()) {
                    Log.d("HomeActivity", "Buscando productos con la query: " + query);
                    productoAdapter.filter(query);
                } else {
                    Log.w("HomeActivity", "La lista de productos está vacía al intentar buscar");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!productosList.isEmpty()) {
                    Log.d("HomeActivity", "Filtrando productos con el texto: " + newText);
                    productoAdapter.filter(newText);
                } else {
                    Log.w("HomeActivity", "La lista de productos está vacía al intentar filtrar");
                }
                return false;
            }
        });

        // Register the receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // Add more actions if needed
        registerReceiver(myReceiver, filter);

        ImageView cartImageView = findViewById(R.id.imageView6);
        cartImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, CarritoActivity.class);
            startActivity(intent);
        });
    }

    private void startAutoSlide() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPagerSlider.getCurrentItem();
                int nextItem = (currentItem + 1) % imageResources.size();
                viewPagerSlider.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 9000);
            }
        };
        sliderHandler.postDelayed(runnable, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(myReceiver);
    }

    private void setSessionActive(boolean active) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sessionActive", active);
        editor.apply();
    }

    private void obtenerProductos() {
        productosList.clear();  // Limpiar la lista antes de agregar nuevos elementos
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OrigenesBD.TABLA_PRODUCTOS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_NOMBRE));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_DESCRIPCION));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_PRECIO)); // Cambiado a double
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
        categoriasList.clear();  // Limpiar la lista antes de agregar nuevos elementos
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
