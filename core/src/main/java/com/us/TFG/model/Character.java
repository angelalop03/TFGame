package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name = "characters")
public class Character {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id;

    @Column(name = "name", type = "TEXT", notNull = true) // Nombre del personaje
    private String name;

    @Column(name = "image_path", type = "TEXT") // Ruta de la imagen del personaje
    private String imagePath;

    // Constructor principal
    public Character(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    // Constructor vacío
    public Character() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Character{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", imagePath='" + imagePath + '\'' +
            '}';
    }
}
