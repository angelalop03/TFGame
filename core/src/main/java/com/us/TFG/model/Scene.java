package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name = "scenes")
public class Scene {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id; // ID único para la base de datos

    @Column(name = "name", type = "TEXT", notNull = true)
    private String name; // Nombre de la escena

    @Column(name = "image_url", type = "TEXT", notNull = true)
    private String imageUrl; // URL de la imagen asociada a la escena

    @Column(name = "description", type = "TEXT", notNull = true)
    private String description; // Descripción o texto de la escena

    @Column(name = "chapter_id", type = "INTEGER", notNull = true) // Relación con Chapter (clave foránea)
    private int chapterId;

    // Constructor principal
    public Scene(String name, String imageUrl, String description, int chapterId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.chapterId = chapterId;
    }

    // Constructor vacío
    public Scene() {}

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", chapterId=" + chapterId +
                '}';
    }
}
