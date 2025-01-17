package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameManager {

    public static Game createGame(String name, int playerId) {
        // Crear el objeto Game
        Game game = new Game(name, playerId);
        game.setStartDate(new Date());
        game.setState(GameState.IN_PROGRESS);
        game.setPointsM(0);
        game.setPointsR(0);
        game.setCurrentChapterId(0); // Inicialmente, ningún capítulo
        game.setCurrentSceneId(0); // Inicialmente, ninguna escena
        game.setCurrentDecisionId(0); // Inicialmente, ninguna decisión

        String query = "INSERT INTO games (name, current_chapter_id, current_scene_id, current_decision_id, player_id, start_date, state, points_m, points_r) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getName());
            stmt.setInt(2, game.getCurrentChapterId());
            stmt.setInt(3, game.getCurrentSceneId());
            stmt.setInt(4, game.getCurrentDecisionId());
            stmt.setInt(5, game.getPlayerId());
            stmt.setDate(6, new java.sql.Date(game.getStartDate().getTime()));
            stmt.setString(7, game.getState().toString());
            stmt.setInt(8, game.getPointsM());
            stmt.setInt(9, game.getPointsR());


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Juego creado y guardado en la base de datos: " + game);
            } else {
                System.err.println("Error al guardar el juego en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    public static List<Game> getGamesForPlayer(int playerId) {
        List<Game> games = new ArrayList<>();

        String query = "SELECT id, name, player_id, start_date, state, points_m, points_r, current_chapter_id, current_scene_id, current_decision_id " +
            "FROM games " +
            "WHERE player_id = ?";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Game game = new Game(rs.getString("name"), rs.getInt("player_id"));
                    game.setId(rs.getInt("id"));
                    game.setStartDate(rs.getDate("start_date"));
                    game.setState(GameState.valueOf(rs.getString("state")));
                    game.setPointsM(rs.getInt("points_m"));
                    game.setPointsR(rs.getInt("points_r"));
                    game.setCurrentChapterId(rs.getInt("current_chapter_id"));
                    game.setCurrentSceneId(rs.getInt("current_scene_id"));
                    game.setCurrentDecisionId(rs.getInt("current_decision_id"));

                    games.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    // Método auxiliar para verificar si ya existe un juego con el mismo nombre para el jugador
    public static boolean doesGameExist(String name, int playerId) {
        String query = "SELECT 1 FROM games WHERE name = ? AND player_id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, playerId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay resultados, el juego ya existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
