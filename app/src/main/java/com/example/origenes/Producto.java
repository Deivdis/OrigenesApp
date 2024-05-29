package com.example.origenes;
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String precio;
    private int categoriaId;
    private int imageResourceId; // Nuevo campo para el ID del recurso de imagen

    public Producto(int id, String nombre, String descripcion, String precio, int categoriaId, int imageResourceId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.imageResourceId = imageResourceId;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
