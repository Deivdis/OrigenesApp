package com.example.origenes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrigenesBD extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "origenes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLA_USUARIOS = "usuarios";
    public static final String TABLA_PRODUCTOS = "productos";
    public static final String TABLA_CATEGORIAS = "categorias";

    // Columnas de la tabla Usuarios
    public static final String COLUMNA_ID = "Id";
    public static final String COLUMNA_NOMBRE = "Nombre";
    public static final String COLUMNA_APELLIDO = "Apellido";
    public static final String COLUMNA_TELEFONO = "Telefono";
    public static final String COLUMNA_CORREO = "Correo";
    public static final String COLUMNA_CONTRASENA = "Contrasena";

    // Columnas de la tabla Productos
    public static final String COLUMNA_PRODUCTO_ID = "IdProducto"; // Renombrado para evitar conflictos con la tabla Usuarios
    public static final String COLUMNA_PRODUCTO_NOMBRE = "Nombre";
    public static final String COLUMNA_PRODUCTO_DESCRIPCION = "Descripcion";
    public static final String COLUMNA_PRODUCTO_PRECIO = "Precio";
    public static final String COLUMNA_PRODUCTO_CATEGORIA_ID = "CategoriaId";
    public static final String COLUMNA_PRODUCTO_URL_IMAGEN = "UrlImagen"; // Variable URL de imagen

    // Columnas de la tabla Categorias
    public static final String COLUMNA_CATEGORIA_ID = "Id";
    public static final String COLUMNA_CATEGORIA_NOMBRE = "Nombre";
    public static final String COLUMNA_CATEGORIA_URL_IMAGEN = "UrlImagen"; // Nueva columna en la definición de la tabla para las imagenes de las categorias

    // Columnas para el slider
    public static final String TABLA_SLIDER = "slider";
    public static final String COLUMNA_SLIDER_ID = "Id";
    public static final String COLUMNA_SLIDER_URL_IMAGEN = "UrlImagen";

    // Crear tabla Usuarios
    private static final String CREAR_TABLA_USUARIOS = "CREATE TABLE " + TABLA_USUARIOS +
            "(" +
            COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_NOMBRE + " TEXT, " +
            COLUMNA_APELLIDO + " TEXT, " +
            COLUMNA_TELEFONO + " TEXT, " +
            COLUMNA_CORREO + " TEXT UNIQUE, " +
            COLUMNA_CONTRASENA + " TEXT" +
            ")";

    // Crear tabla Categorias
    private static final String CREAR_TABLA_CATEGORIAS = "CREATE TABLE " + TABLA_CATEGORIAS +
            "(" +
            COLUMNA_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_CATEGORIA_NOMBRE + " TEXT UNIQUE, " +
            COLUMNA_CATEGORIA_URL_IMAGEN + " TEXT" + // Nueva columna en la definición de la tabla para las imagenes de las categorias
            ")";

    // Crear tabla Productos
    private static final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE " + TABLA_PRODUCTOS +
            "(" +
            COLUMNA_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Definir la columna ID_PRODUCTO
            COLUMNA_PRODUCTO_NOMBRE + " TEXT, " +
            COLUMNA_PRODUCTO_DESCRIPCION + " TEXT, " +
            COLUMNA_PRODUCTO_PRECIO + " TEXT, " +
            COLUMNA_PRODUCTO_CATEGORIA_ID + " INTEGER, " +
            COLUMNA_PRODUCTO_URL_IMAGEN + " TEXT, " + // Variable URL de imagen
            "FOREIGN KEY(" + COLUMNA_PRODUCTO_CATEGORIA_ID + ") REFERENCES " + TABLA_CATEGORIAS + "(" + COLUMNA_CATEGORIA_ID + ")" +
            ")";

    // Crear tabla Slider
    private static final String CREAR_TABLA_SLIDER = "CREATE TABLE " + TABLA_SLIDER +
            "(" +
            COLUMNA_SLIDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_SLIDER_URL_IMAGEN + " TEXT" +
            ")";

    public OrigenesBD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_CATEGORIAS);
        db.execSQL(CREAR_TABLA_PRODUCTOS);
        db.execSQL(CREAR_TABLA_SLIDER);
        // agregarCategorias(db); // Agregar imagenes del las categorias
        agregarProductos(db); // Agregar imagenes de los productos
        agregarImagenesSlider(db); // Agregar imagenes del slider
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SLIDER);
        onCreate(db);
    }

    // Método para insertar datos de ejemplo
    public void agregarProductos(SQLiteDatabase db) {
        // Insertar Categorías
        String[] categorias = {"Suplementos", "Herbales", "Vitaminas", "Minerales", "Te's"};
        ContentValues values;

        for (String categoria : categorias) {
            values = new ContentValues();
            values.put(COLUMNA_CATEGORIA_NOMBRE, categoria);
            long categoriaId = db.insert(TABLA_CATEGORIAS, null, values);

            if (categoriaId == -1) {
                Log.e("OrigenesBD", "Error inserting category: " + categoria);
            }
        }

        // Insertar productos
        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Omega 3");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Suplemento de aceite de pescado.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "25.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 1); // Suplementos
        values.put(COLUMNA_PRODUCTO_URL_IMAGEN, "https://www.madewithnestle.ca/sites/default/files/2023-12/Omega-3-D3.png");
        long productoId1 = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId1 == -1) {
            Log.e("OrigenesBD", "Error inserting product: Omega 3");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Té Verde");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Bebida de hojas de té verde.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "15.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 2); // Herbales
        values.put(COLUMNA_PRODUCTO_URL_IMAGEN, "https://naturesbounty.com/cdn/shop/products/003131.png?v=1667506648&width=550");
        long productoId2 = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId2 == -1) {
            Log.e("OrigenesBD", "Error inserting product: Té Verde");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Vitamina C");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Suplemento de vitamina C.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "10.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 3); // Vitaminas
        values.put(COLUMNA_PRODUCTO_URL_IMAGEN, "https://naturesbounty.com/cdn/shop/products/001707.png?v=1667506832");
        long productoId3 = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId3 == -1) {
            Log.e("OrigenesBD", "Error inserting product: Vitamina C");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Calcio");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "El calcio es la principal fuente de minerales para los huesos");
        values.put(COLUMNA_PRODUCTO_PRECIO, "20.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 4); // Minerales
        values.put(COLUMNA_PRODUCTO_URL_IMAGEN, "https://naturesbounty.com/cdn/shop/products/004290.png?v=1667506484&width=550");
        long productoId4 = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId4 == -1) {
            Log.e("OrigenesBD", "Error inserting product: Calcio");
        }
    }

    public void agregarImagenesSlider(SQLiteDatabase db) {
        String[] urlsImagenes = {
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHvQDLQnhJuGi2GsJMESGV6MDfMLtBf4lP71V-mBc1GZvqlac8NMqyrO3iXaRiphAab4A&usqp=CAU",
                "https://www.regiondigital.com/m/p/745x450/media/files/106569_medicinabio.jpg",
                "https://www.elaesi.edu.mx/wp-content/uploads/2021/07/medicina-naturista-alternativa.jpg"
        };

        ContentValues values;
        for (String url : urlsImagenes) {
            values = new ContentValues();
            values.put(COLUMNA_SLIDER_URL_IMAGEN, url);
            long sliderId = db.insert(TABLA_SLIDER, null, values);
            if (sliderId == -1) {
                Log.e("OrigenesBD", "Error inserting slider image: " + url);
            }
        }
    }

    public List<String> obtenerUrlsImagenesSlider() {
        List<String> urls = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_SLIDER_URL_IMAGEN + " FROM " + TABLA_SLIDER, null);

        if (((Cursor) cursor).moveToFirst()) {
            do {
                urls.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return urls;
    }
}
