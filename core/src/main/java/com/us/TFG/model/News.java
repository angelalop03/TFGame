package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name="news")
public class News {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id;  // ID único para la base de datos

    @Column(name = "name", type = "TEXT", notNull = true)
    private String name;  // Nombre de la noticia

    @Column(name = "text", type = "TEXT", notNull = true)
    private String text;  // Contenido de la noticia

    @Column(name = "image", type = "TEXT")
    private String image;  // URL de la imagen asociada a la noticia


    public News(String name, String text, String image) {
        this.name = name;
        this.text = text;
        this.image = image;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
