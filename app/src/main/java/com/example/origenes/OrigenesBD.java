package com.example.origenes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
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
    public static final String COLUMNA_PRODUCTO_ID = "Id";
    public static final String COLUMNA_PRODUCTO_NOMBRE = "Nombre";
    public static final String COLUMNA_PRODUCTO_DESCRIPCION = "Descripcion";
    public static final String COLUMNA_PRODUCTO_PRECIO = "Precio";
    public static final String COLUMNA_PRODUCTO_CATEGORIA_ID = "CategoriaId";

    // Columnas de la tabla Categorias
    public static final String COLUMNA_CATEGORIA_ID = "Id";
    public static final String COLUMNA_CATEGORIA_NOMBRE = "Nombre";

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
            COLUMNA_CATEGORIA_NOMBRE + " TEXT UNIQUE" +
            ")";

    // Crear tabla Productos
    private static final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE " + TABLA_PRODUCTOS +
            "(" +
            COLUMNA_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_PRODUCTO_NOMBRE + " TEXT, " +
            COLUMNA_PRODUCTO_DESCRIPCION + " TEXT, " +
            COLUMNA_PRODUCTO_PRECIO + " TEXT, " +
            COLUMNA_PRODUCTO_CATEGORIA_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMNA_PRODUCTO_CATEGORIA_ID + ") REFERENCES " + TABLA_CATEGORIAS + "(" + COLUMNA_CATEGORIA_ID + ")" +
            ")";

    public OrigenesBD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_CATEGORIAS);
        db.execSQL(CREAR_TABLA_PRODUCTOS);
        agregarProductos(db); // Llamar al método de inserción de datos de ejemplo al crear la base de datos
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIAS);
        onCreate(db);
    }

    // Método para insertar datos de ejemplo
    public void agregarProductos(SQLiteDatabase db) {
        // Insertar Categorías
        String[] categorias = {"Suplementos", "Herbales", "Vitaminas", "Sistema Nervioso"};
        ContentValues values;

        for (String categoria : categorias) {
            values = new ContentValues();
            values.put(COLUMNA_CATEGORIA_NOMBRE, categoria);
            long categoriaId = db.insert(TABLA_CATEGORIAS, null, values);

            if (categoriaId == -1) {
                Log.e("OrigenesBD", "Error inserting category: " + categoria);
            }
        }

        // Insertar Productos
        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Omega 3");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Suplemento de aceite de pescado.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "25.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 1); // Suplementos
        long productoId = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId == -1) {
            Log.e("OrigenesBD", "Error inserting product: Omega 3");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Té Verde");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Bebida de hojas de té verde.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "15.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 2); // Herbales
        productoId = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId == -1) {
            Log.e("OrigenesBD", "Error inserting product: Té Verde");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Vitamina C");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Suplemento de vitamina C.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "10.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 3); // Vitaminas
        productoId = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId == -1) {
            Log.e("OrigenesBD", "Error inserting product: Vitamina C");
        }

        values = new ContentValues();
        values.put(COLUMNA_PRODUCTO_NOMBRE, "Ginkgo Biloba");
        values.put(COLUMNA_PRODUCTO_DESCRIPCION, "Suplemento para mejorar la función cognitiva.");
        values.put(COLUMNA_PRODUCTO_PRECIO, "20.00");
        values.put(COLUMNA_PRODUCTO_CATEGORIA_ID, 4); // Sistema Nervioso
        productoId = db.insert(TABLA_PRODUCTOS, null, values);
        if (productoId == -1) {
            Log.e("OrigenesBD", "Error inserting product: Ginkgo Biloba");
        }
    }
}
