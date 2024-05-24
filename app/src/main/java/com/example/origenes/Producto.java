package com.example.origenes;
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String precio;
    private int categoriaId;

    public Producto(int id, String nombre, String descripcion, String precio, int categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getPrecio() { return precio; }
    public int getCategoriaId() { return categoriaId; }
}
