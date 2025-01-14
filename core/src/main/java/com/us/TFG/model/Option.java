package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name = "options")
public class Option {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id; // ID único para la base de datos

    @Column(name = "text", type = "TEXT", notNull = true)
    private String text; // Texto que describe la opción

    @Column(name = "points_impact", type = "INTEGER")
    private int pointsImpact; // Impacto en los puntos del jugador

    @Column(name = "relationship_impact", type = "INTEGER")
    private int relationshipImpact; // Impacto en las relaciones del jugador

    @Column(name = "next_scene_id", type = "INTEGER", notNull = true)
    private int nextSceneId; // ID de la siguiente escena (clave foránea)

    @Column(name = "scene_id", type = "INTEGER", notNull = true) // Relación con Scene (clave foránea)
    private int sceneId;

    // Constructor principal
    public Option(String text, int pointsImpact, int relationshipImpact, int nextSceneId, int sceneId) {
        this.text = text;
        this.pointsImpact = pointsImpact;
        this.relationshipImpact = relationshipImpact;
        this.nextSceneId = nextSceneId;
        this.sceneId = sceneId;
    }

    // Constructor vacío
    public Option() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPointsImpact() {
        return pointsImpact;
    }

    public void setPointsImpact(int pointsImpact) {
        this.pointsImpact = pointsImpact;
    }

    public int getRelationshipImpact() {
        return relationshipImpact;
    }

    public void setRelationshipImpact(int relationshipImpact) {
        this.relationshipImpact = relationshipImpact;
    }

    public int getNextSceneId() {
        return nextSceneId;
    }

    public void setNextSceneId(int nextSceneId) {
        this.nextSceneId = nextSceneId;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", pointsImpact=" + pointsImpact +
                ", relationshipImpact=" + relationshipImpact +
                ", nextSceneId=" + nextSceneId +
                ", sceneId=" + sceneId +
                '}';
    }
}
