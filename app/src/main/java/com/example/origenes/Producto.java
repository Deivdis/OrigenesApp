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
        this.precio = String.valueOf(precio);
        this.categoriaId = categoriaId;
        this.imageResourceId = imageResourceId;
    }

    public Producto(int idProducto, String nombre, String descripcion, double precio, int cantidad) {
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
