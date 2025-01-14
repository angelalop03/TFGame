package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

import java.util.Date;

@Table(name = "games")
public class Game {

    @Column(name = "id", type = "INTEGER", primaryKey = true, notNull = true)
    private int id;  // ID único para la base de datos

    @Column(name = "name", type = "TEXT", notNull = true)
    private String name;  // Nombre del juego

    @Column(name = "current_chapter", type = "INTEGER") // Relación con el capítulo actual (clave foránea)
    private int currentChapterId;

    @Column(name = "current_scene", type = "INTEGER") // Relación con la escena actual (clave foránea)
    private int currentSceneId;

    @Column(name = "player_id", type = "INTEGER", notNull = true) // Relación con el jugador (clave foránea)
    private int playerId;

    @Column(name = "start_date", type = "DATE", notNull = true)
    private Date startDate;  // Fecha de inicio de la partida

    @Column(name = "last_activity_date", type = "DATE", notNull = true)
    private Date lastActivityDate;  // Fecha de la última actividad

    @Column(name = "state", type = "TEXT", notNull = true, defaultValue = "'IN_PROGRESS'")
    private GameState state;  // Estado del juego (En curso, Completado, Pausado)

    @Column(name = "total_score", type = "INTEGER", defaultValue = "0")
    private int totalScore;  // Puntaje acumulado

    // Constructor principal
    public Game(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;
        this.startDate = new Date(); // Fecha de creación
        this.lastActivityDate = new Date();
        this.state = GameState.IN_PROGRESS;
        this.totalScore = 0;
    }

    // Constructor vacío
    public Game() {}

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

    public int getCurrentChapterId() {
        return currentChapterId;
    }

    public void setCurrentChapterId(int currentChapterId) {
        this.currentChapterId = currentChapterId;
    }

    public int getCurrentSceneId() {
        return currentSceneId;
    }

    public void setCurrentSceneId(int currentSceneId) {
        this.currentSceneId = currentSceneId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", currentChapterId=" + currentChapterId +
            ", currentSceneId=" + currentSceneId +
            ", playerId=" + playerId +
            ", startDate=" + startDate +
            ", lastActivityDate=" + lastActivityDate +
            ", state='" + state + '\'' +
            ", totalScore=" + totalScore +
            '}';
    }
}
