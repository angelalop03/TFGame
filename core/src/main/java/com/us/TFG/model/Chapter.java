package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name = "chapters")
public class Chapter {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id;  // ID único para la base de datos

    @Column(name = "name", type = "TEXT", notNull = true)
    private String name; // Nombre del capítulo

    @Column(name = "chapter_number", type = "INTEGER", notNull = true)
    private int chapterNumber; // Número del capítulo

    @Column(name = "game_id", type = "INTEGER", notNull = true) // Relación con el juego (clave foránea)
    private int gameId;

    // Constructor principal
    public Chapter(String name, int chapterNumber, int gameId) {
        this.name = name;
        this.chapterNumber = chapterNumber;
        this.gameId = gameId;
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

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "Chapter{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", chapterNumber=" + chapterNumber +
            ", gameId=" + gameId +
            '}';
    }
}
