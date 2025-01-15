package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name = "chapters")
public class Chapter {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id;  // ID único para la base de datos

    @Column(name = "name", type = "TEXT", notNull = true)
    private String name; // Nombre del capítulo

    @Column(name = "chapter_number", type = "TEXT", notNull = true)
    private String chapterNumber; // Número del capítulo (e.g., 6.1, 6.2)

    @Column(name = "image_path", type = "TEXT")
    private String imagePath; // Ruta de la imagen asociada al capítulo

    @Column(name = "required_relationship_state", type = "TEXT")
    private String requiredRelationshipState; // Estado de relación necesario para desbloquear el capítulo

    // Constructor principal
    public Chapter(String name, String chapterNumber, String imagePath, String requiredRelationshipState) {
        this.name = name;
        this.chapterNumber = chapterNumber;
        this.imagePath = imagePath;
        this.requiredRelationshipState = requiredRelationshipState;
    }

    // Constructor vacío
    public Chapter() {}

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

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRequiredRelationshipState() {
        return requiredRelationshipState;
    }

    public void setRequiredRelationshipState(String requiredRelationshipState) {
        this.requiredRelationshipState = requiredRelationshipState;
    }

    @Override
    public String toString() {
        return "Chapter{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", chapterNumber='" + chapterNumber + '\'' +
            ", imagePath='" + imagePath + '\'' +
            ", requiredRelationshipState='" + requiredRelationshipState + '\'' +
            '}';
    }
}
