package com.example.origenes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MetodosBaseDeDatos {

    private SQLiteDatabase db;

    private  OrigenesBD dbHelper;

    public MetodosBaseDeDatos(Context context) {
        dbHelper = new OrigenesBD(context);
    }

    public void abrir() {
        db = dbHelper.getWritableDatabase();
    }

    public void cerrar() {
        db.close();
    }

    public boolean VerificarUsuario(String nombre) {
        abrir(); // Abre la base de datos para operaciones de lectura
        Cursor cursor = db.query(
                OrigenesBD.TABLA_USUARIOS, // Nombre de la tabla
                null, // Columnas que quieres recuperar (null para todas)
                OrigenesBD.COLUMNA_NOMBRE + " = ?", // Clausula WHERE
                new String[]{nombre}, // Argumentos para la clausula WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );
        boolean existe = (cursor.getCount() > 0);
        cursor.close();
        cerrar(); // Cierra la base de datos
        return existe;
    }

    public long agregarUsuario(String nombre, String apellido, String telefono, String correo, String clave) {
        abrir(); // Abre la base de datos para operaciones de escritura
        ContentValues values = new ContentValues();
        values.put(OrigenesBD.COLUMNA_NOMBRE, nombre);
        values.put(OrigenesBD.COLUMNA_APELLIDO, apellido);
        values.put(OrigenesBD.COLUMNA_TELEFONO, telefono);
        values.put(OrigenesBD.COLUMNA_CORREO, correo);
        values.put(OrigenesBD.COLUMNA_CONTRASENA, clave);
        long id = db.insert(OrigenesBD.TABLA_USUARIOS, null, values);
        cerrar(); // Cierra la base de datos
        return id;
    }
}

