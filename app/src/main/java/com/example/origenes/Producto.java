package com.example.origenes;
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String precio;
    private int categoriaId;
    private String urlImagen; // Nuevo campo para la URL de la imagen

    public Producto(int id, String nombre, String descripcion, String precio, int categoriaId, String urlImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.urlImagen = urlImagen;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getPrecio() { return precio; }
    public int getCategoriaId() { return categoriaId; }
    public String getUrlImagen() { return urlImagen; } // Getter para la URL de la imagen
}
