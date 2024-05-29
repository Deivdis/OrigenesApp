package com.example.origenes;

public class Categoria {
    private int id;
    private String nombre;
    private int imageResourceId; // Nuevo campo para el ID del recurso de imagen

    public Categoria(int id, String nombre, int imageResourceId) {
        this.id = id;
        this.nombre = nombre;
        this.imageResourceId = imageResourceId;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}