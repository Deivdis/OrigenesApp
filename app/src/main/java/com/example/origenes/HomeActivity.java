package com.example.origenes;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerSlider;
    private ProgressBar progressBarBanner;
    private RecyclerView recyclerViewPopular;
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
        recyclerViewPopular = findViewById(R.id.recyclerViewPopular);
        progressBarPopular = findViewById(R.id.progressBarPopular);

        // Configurar el RecyclerView
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de productos
        productosList = new ArrayList<>();

        // Crear algunos productos de ejemplo (puedes cambiar esto con tu lógica de obtener productos)
        recyclerViewPopular = findViewById(R.id.recyclerViewPopular);


        // Inicializar y configurar el adaptador
        productoAdapter = new ProductoAdapter(productosList, this);
        recyclerViewPopular.setAdapter(productoAdapter);

        // Ocultar la barra de progreso una vez que se hayan cargado los productos (simulado aquí)
        progressBarPopular.setVisibility(View.GONE);

        // Configuración del ViewPager y ProgressBar del banner
        databaseHelper = new OrigenesBD(this);
        List<String> imageUrls = databaseHelper.obtenerUrlsImagenesSlider();

        SliderAdapter sliderAdapter = new SliderAdapter(this, imageUrls);
        viewPagerSlider.setAdapter(sliderAdapter);

        // Oculta la ProgressBar después de configurar el adapter
        progressBarBanner.setVisibility(View.GONE);
    }
}
