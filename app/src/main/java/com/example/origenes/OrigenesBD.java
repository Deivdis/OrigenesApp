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
    //colunmas carrito
    public static final String TABLA_CARRITO = "carrito";
    public static final String COLUMNA_CARRITO_ID = "Id";
    public static final String COLUMNA_CARRITO_PRODUCTO_ID = "ProductoId";
    public static final String COLUMNA_CARRITO_USUARIO_ID = "UsuarioId";
    public static final String COLUMNA_CARRITO_CANTIDAD = "Cantidad";

    // Crear tabla Carrito
    private static final String CREAR_TABLA_CARRITO = "CREATE TABLE " + TABLA_CARRITO +
            "(" +
            COLUMNA_CARRITO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_CARRITO_USUARIO_ID + " INTEGER, " +
            COLUMNA_CARRITO_PRODUCTO_ID + " INTEGER, " +
            COLUMNA_CARRITO_CANTIDAD + " INTEGER, " +
            "FOREIGN KEY(" + COLUMNA_CARRITO_PRODUCTO_ID + ") REFERENCES " + TABLA_PRODUCTOS + "(" + COLUMNA_PRODUCTO_ID + ")," +
            "FOREIGN KEY(" + COLUMNA_CARRITO_USUARIO_ID + ") REFERENCES " + TABLA_USUARIOS + "(" + COLUMNA_ID + ")" +
            ")";


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

    private static final String CREAR_INDICE_PRODUCTO_CATEGORIA_ID =
            "CREATE INDEX IF NOT EXISTS idx_producto_categoria_id ON " + TABLA_PRODUCTOS +
                    "(" + COLUMNA_PRODUCTO_CATEGORIA_ID + ")";

    private static final String CREAR_INDICE_CATEGORIA_ID =
            "CREATE INDEX IF NOT EXISTS idx_categoria_id ON " + TABLA_CATEGORIAS +
                    "(" + COLUMNA_CATEGORIA_ID + ")";

    public OrigenesBD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_CATEGORIAS);
        db.execSQL(CREAR_TABLA_PRODUCTOS);
        db.execSQL(CREAR_TABLA_SLIDER);
        db.execSQL(CREAR_TABLA_CARRITO);
        Log.d("OrigenesBD", "Database tables created successfully.");
        agregarProductos(db); // Agregar productos de ejemplo
        agregarImagenesSlider(db); // Agregar imágenes del slider de ejemplo
        Log.d("OrigenesBD", "Sample data inserted successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SLIDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CARRITO);
        onCreate(db);
        Log.d("OrigenesBD", "Database upgraded successfully.");
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
        insertarProducto(db, "Omega 3", "Suplemento de Omega-3 y Vitamina D3 que contribuye al bienestar cardiovascular, cerebral y ocular. Favorece la función inmunológica y el bienestar general del organismo. Formulado con ingredientes naturales para un equilibrio óptimo de nutrientes..", "85.999", 1, R.drawable.omega3);
        insertarProducto(db, "Melatonina", "Descubre el poder de la melatonina para transformar tus noches! Este aliado natural te ayuda a lograr un sueño profundo y reparador, para que despiertes renovado y lleno de energía. Ya sea que enfrentes insomnio ocasional, desfase horario, o simplemente busques mejorar tu calidad de descanso.", "41.999", 1, R.drawable.melatonina);
        // Insertar productos herbales
        insertarProducto(db, "Té Verde", "El Té Verde contiene flavonoides beneficiosos, que son fitoquímicos naturales. El extracto de Té Verde brinda un poderoso apoyo antioxidante y, además, ha sido utilizado tradicionalmente para respaldar la salud del corazón. Es una elección natural y un impulso en su bienestar diario.", "45.999", 2, R.drawable.te_verde);
        // Insertar productos vitaminas
        insertarProducto(db, "Vitamina C", "\n" +
                "La vitamina C desempeña un papel crucial en el apoyo a la función del sistema inmunológico.* Como antioxidante, ayuda a combatir los radicales libres en el cuerpo, contribuyendo así a su bienestar general.", "16.999", 3, R.drawable.vitamina_c);
        // Insertar productos minerales
        insertarProducto(db, "Calcio", "\n" +
                "El calcio es fundamental para huesos fuertes, contracción muscular y función nerviosa.* La vitamina D ayuda en la absorción del calcio y en el sistema inmunológico.* Una dieta equilibrada, ejercicio regular y una ingesta adecuada de calcio y vitamina D ayudan a mantener la salud de los huesos y el riesgo de osteoporosis.", "31.999", 4, R.drawable.calcio);
        // Insertar productos especiales
        insertarProducto(db, "Lisina", "La L-Lisina es un aminoácido esencial que el cuerpo no puede producir, por lo que debe obtenerse a partir de fuentes nutricionales. Se utiliza para mantener la salud y la integridad de la piel y también participa en la producción de colágeno y el mantenimiento de los tejidos.", "38.999", 5, R.drawable.lisina);
        insertarProducto(db, "Carcato Activado", "\n" +
                "El carbón activado ha sido utilizado durante más de 180 años para adsorber toxinas no deseadas en el cuerpo. Documentado por profesionales de la salud franceses y estadounidenses, Nature’s Bounty ofrece carbón activado con 520 mg por porción, ideal para su uso después de las comidas.", "65.999", 5, R.drawable.carcatoactivado);

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
    // Insertar producto en el carrito
    public void agregarProductoAlCarrito(int usuarioId, int productoId, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_CARRITO_USUARIO_ID, usuarioId);
        values.put(COLUMNA_CARRITO_PRODUCTO_ID, productoId);
        values.put(COLUMNA_CARRITO_CANTIDAD, cantidad);
        long carritoId = db.insert(TABLA_CARRITO, null, values);
        if (carritoId == -1) {
            Log.e("OrigenesBD", "Error inserting product into cart");
        }
        db.close();
    }

    // Obtener productos del carrito para un usuario específico
    public List<Producto> obtenerProductosDelCarrito(int usuarioId) {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p." + COLUMNA_PRODUCTO_ID + ", p." + COLUMNA_PRODUCTO_NOMBRE + ", p." + COLUMNA_PRODUCTO_DESCRIPCION + ", p." + COLUMNA_PRODUCTO_PRECIO + ", c." + COLUMNA_CARRITO_CANTIDAD +
                " FROM " + TABLA_CARRITO + " c JOIN " + TABLA_PRODUCTOS + " p ON c." + COLUMNA_CARRITO_PRODUCTO_ID + " = p." + COLUMNA_PRODUCTO_ID +
                " WHERE c." + COLUMNA_CARRITO_USUARIO_ID + " = ?"; // Añade esta línea
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)}); // Modifica esta línea para pasar el usuarioId
        if (cursor.moveToFirst()) {
            do {
                int idProducto = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                double precio = cursor.getDouble(3);
                int cantidad = cursor.getInt(4);
                productos.add(new Producto(idProducto, nombre, descripcion, precio, cantidad));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productos;
    }

    public boolean productoExisteEnCarrito(int usuarioId, int productoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLA_CARRITO +
                        " WHERE " + COLUMNA_CARRITO_PRODUCTO_ID + " = ? AND " + COLUMNA_CARRITO_USUARIO_ID + " = ?",
                new String[]{String.valueOf(productoId), String.valueOf(usuarioId)}
        );
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    // Actualizar la cantidad de un producto en el carrito
    public void actualizarCantidadProductoEnCarrito(int usuarioId, int productoId, int nuevaCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_CARRITO_CANTIDAD, nuevaCantidad);
        db.update(TABLA_CARRITO, values,
                COLUMNA_CARRITO_USUARIO_ID + " = ? AND " + COLUMNA_CARRITO_PRODUCTO_ID + " = ?",
                new String[]{String.valueOf(usuarioId), String.valueOf(productoId)}
        );
        db.close();
    }

    public int obtenerCantidadProductoEnCarrito(int usuarioId, int productoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMNA_CARRITO_CANTIDAD + " FROM " + TABLA_CARRITO +
                        " WHERE " + COLUMNA_CARRITO_PRODUCTO_ID + " = ? AND " + COLUMNA_CARRITO_USUARIO_ID + " = ?",
                new String[]{String.valueOf(productoId), String.valueOf(usuarioId)}
        );
        int cantidad = 0;
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }
        cursor.close();
        return cantidad;
    }

    // Eliminar un producto del carrito
    public void eliminarProductoDelCarrito(int usuarioId, int productoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_CARRITO,
                COLUMNA_CARRITO_USUARIO_ID + " = ? AND " + COLUMNA_CARRITO_PRODUCTO_ID + " = ?",
                new String[]{String.valueOf(usuarioId), String.valueOf(productoId)}
        );
        db.close();

    }
    public void vaciarCarrito(int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_CARRITO, COLUMNA_CARRITO_USUARIO_ID + " = ?", new String[]{String.valueOf(usuarioId)});
        db.close();
    }

}
