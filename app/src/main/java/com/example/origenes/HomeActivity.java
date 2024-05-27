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

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerSlider;
    private ProgressBar progressBarBanner;
    private RecyclerView recyclerViewProducto;
    private ProgressBar progressBarPopular;
    private ProductoAdapter productoAdapter;
    private List<Producto> productosList;
    private OrigenesBD databaseHelper;

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

        viewPagerSlider = findViewById(R.id.viewPagerSlider);
        progressBarBanner = findViewById(R.id.progressBarBanner);
        recyclerViewProducto = findViewById(R.id.recyclerViewProducto);
        progressBarPopular = findViewById(R.id.progressBarPopular);

        // Configurar el RecyclerView
        recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this, 2));

        // Inicializar la lista de productos
        productosList = new ArrayList<>();

        // Obtener los productos de la base de datos
        databaseHelper = new OrigenesBD(this);
        productosList = obtenerProductos();

        // Inicializar y configurar el adaptador
        productoAdapter = new ProductoAdapter(productosList, this);
        recyclerViewProducto.setAdapter(productoAdapter);

        // Ocultar la barra de progreso una vez que se hayan cargado los productos (simulado aquí)
        progressBarPopular.setVisibility(View.GONE);

        // Configuración del ViewPager y ProgressBar del banner
        List<String> imageUrls = databaseHelper.obtenerUrlsImagenesSlider();
        SliderAdapter sliderAdapter = new SliderAdapter(this, imageUrls);
        viewPagerSlider.setAdapter(sliderAdapter);

        // Ocultar la ProgressBar después de configurar el adaptador
        progressBarBanner.setVisibility(View.GONE);
    }

    private List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
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
                productos.add(producto);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return productos;
    }
}