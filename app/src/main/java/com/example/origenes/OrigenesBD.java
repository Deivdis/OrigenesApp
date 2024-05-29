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
    private static final int DATABASE_VERSION = 4;

    public static final String TABLA_USUARIOS = "usuarios";
    public static final String TABLA_PRODUCTOS = "productos";
    public static final String TABLA_CATEGORIAS = "categorias";
    public static final String TABLA_SLIDER = "slider";

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
    public static final String COLUMNA_PRODUCTO_IMAGEN_RECURSO = "ImagenRecurso"; // Recurso de imagen

    // Columnas de la tabla Categorias
    public static final String COLUMNA_CATEGORIA_ID = "Id";
    public static final String COLUMNA_CATEGORIA_NOMBRE = "Nombre";
    public static final String COLUMNA_CATEGORIA_IMAGEN_RECURSO = "ImagenRecurso"; // Recurso de imagen

    // Columnas para el slider
    public static final String COLUMNA_SLIDER_ID = "Id";
    public static final String COLUMNA_SLIDER_IMAGEN_RECURSO = "ImagenRecurso"; // Recurso de imagen

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
            COLUMNA_CATEGORIA_IMAGEN_RECURSO + " INTEGER" + // Recurso de imagen
            ")";

    // Crear tabla Productos
    private static final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE " + TABLA_PRODUCTOS +
            "(" +
            COLUMNA_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Definir la columna ID_PRODUCTO
            COLUMNA_PRODUCTO_NOMBRE + " TEXT, " +
            COLUMNA_PRODUCTO_DESCRIPCION + " TEXT, " +
            COLUMNA_PRODUCTO_PRECIO + " TEXT, " +
            COLUMNA_PRODUCTO_CATEGORIA_ID + " INTEGER, " +
            COLUMNA_PRODUCTO_IMAGEN_RECURSO + " INTEGER, " + // Recurso de imagen
            "FOREIGN KEY(" + COLUMNA_PRODUCTO_CATEGORIA_ID + ") REFERENCES " + TABLA_CATEGORIAS + "(" + COLUMNA_CATEGORIA_ID + ")" +
            ")";

    // Crear tabla Slider
    private static final String CREAR_TABLA_SLIDER = "CREATE TABLE " + TABLA_SLIDER +
            "(" +
            COLUMNA_SLIDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_SLIDER_IMAGEN_RECURSO + " INTEGER" + // Recurso de imagen
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
        agregarProductos(db); // Agregar productos de ejemplo
        agregarImagenesSlider(db); // Agregar imágenes del slider de ejemplo
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
        // Insertar Categorías con recursos de imagen
        insertarCategoria(db, "Suplementos", R.drawable.suplementos);
        insertarCategoria(db, "Herbales", R.drawable.herbales);
        insertarCategoria(db, "Vitaminas", R.drawable.vitaminas);
        insertarCategoria(db, "Minerales", R.drawable.minerales);
        insertarCategoria(db, "Especiales", R.drawable.especiales);

        // Insertar productos suplementos
        insertarProducto(db, "Omega 3", "Suplemento de aceite de pescado.", "25.00", 1, R.drawable.omega3);
        insertarProducto(db, "Melatonina", "Suplemento para dormir.", "41.341", 1, R.drawable.melatonina);
        // Insertar productos herbales
        insertarProducto(db, "Té Verde", "Bebida de hojas de té verde.", "15.00", 2, R.drawable.te_verde);
        // Insertar productos vitaminas
        insertarProducto(db, "Vitamina C", "Suplemento de vitamina C.", "10.00", 3, R.drawable.vitamina_c);
        // Insertar productos minerales
        insertarProducto(db, "Calcio", "El calcio es la principal fuente de minerales para los huesos", "20.00", 4, R.drawable.calcio);
        // Insertar productos especiales
        insertarProducto(db, "Lisina", "Lisina es un aminoácido esencial que no produce", "38.691", 5, R.drawable.lisina);
        insertarProducto(db, "Carcato Activado", "El carbón activado para eliminar toxicinas no deseadas", "65.798", 5, R.drawable.carcatoactivado);

    }

    private void insertarCategoria(SQLiteDatabase db, String nombre, int imagenRecurso) {
        ContentValues values = new ContentValues();
        values.put(COLUMNA_CATEGORIA_NOMBRE, nombre);
        values.put(COLUMNA_CATEGORIA_IMAGEN_RECURSO, imagenRecurso);
        long categoriaId = db.insert(TABLA_CATEGORIAS, null, values);
        if (categoriaId == -1) {
            Log.e("OrigenesBD", "Error inserting category: " + nombre);
        }
    }

    private void insertarProducto(SQLiteDatabase db, String nombre, String descripcion, String precio, int categoriaId, int imagenRecurso) {
        ContentValues values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, nombre);
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, descripcion);
        values.put(COLUMNA_PRODUCTO_PRECIO, precio);
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, categoriaId);
        values.put(COLUMNA_PRODUCTO_IMAGEN_RECURSO, imagenRecurso);
        long productoId = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId == -1) {
            Log.e("OrigenesBD", "Error inserting product: " + nombre);
        }
    }

    public void agregarImagenesSlider(SQLiteDatabase db) {
        // Insertar imágenes del slider con recursos de imagen
        insertarImagenSlider(db, R.drawable.slider1);
        insertarImagenSlider(db, R.drawable.slider2);
        insertarImagenSlider(db, R.drawable.slider3);
    }

    private void insertarImagenSlider(SQLiteDatabase db, int imagenRecurso) {
        ContentValues values = new ContentValues();
        values.put(COLUMNA_SLIDER_IMAGEN_RECURSO, imagenRecurso);
        long sliderId = db.insert(TABLA_SLIDER, null, values);
        if (sliderId == -1) {
            Log.e("OrigenesBD", "Error inserting slider image: " + imagenRecurso);
        }
    }

    public List<Integer> obtenerImagenesSlider() {
        List<Integer> imagenes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_SLIDER_IMAGEN_RECURSO + " FROM " + TABLA_SLIDER, null);

        if (cursor.moveToFirst()) {
            do {
                imagenes.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagenes;
    }
}
