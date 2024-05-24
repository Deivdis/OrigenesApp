package com.example.origenes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Productos extends AppCompatActivity {

    private OrigenesBD origenesBD;
    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private List<Producto> productosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        origenesBD = new OrigenesBD(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productosList = obtenerProductos();
        productoAdapter = new ProductoAdapter(productosList);
        recyclerView.setAdapter(productoAdapter);
    }

    private List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = origenesBD.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OrigenesBD.TABLA_PRODUCTOS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_NOMBRE));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_DESCRIPCION));
                String precio = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_PRECIO));
                int categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_CATEGORIA_ID));

                Producto producto = new Producto(id, nombre, descripcion, precio, categoriaId);
                productos.add(producto);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return productos;
    }
}
