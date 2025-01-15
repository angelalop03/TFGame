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
        game.setLastActivityDate(new Date());
        game.setState(GameState.IN_PROGRESS);
        game.setTotalScore(0);

        String query = "INSERT INTO games (name, player_id, start_date, last_activity_date, state, total_score) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getName());
            stmt.setInt(2, game.getPlayerId());
            stmt.setDate(3, new java.sql.Date(game.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(game.getLastActivityDate().getTime()));
            stmt.setString(5, game.getState().toString());
            stmt.setInt(6, game.getTotalScore());

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




    // Avanzar a la siguiente escena
    public static void advanceScene(Game game) {
        int currentSceneId = game.getCurrentSceneId();
        int nextSceneId = findNextSceneId(currentSceneId);

        if (nextSceneId != -1) {
            game.setCurrentSceneId(nextSceneId);
            game.setLastActivityDate(new Date());
            System.out.println("Avanzado a la escena: " + nextSceneId);
        } else {
            System.out.println("No hay más escenas en el capítulo actual. Intentando avanzar al siguiente capítulo...");
            advanceChapter(game);
        }
    }

    // Avanzar al siguiente capítulo
    public static void advanceChapter(Game game) {
        int currentChapterId = game.getCurrentChapterId();
        int nextChapterId = findNextChapterId(currentChapterId);

        if (nextChapterId != -1) {
            game.setCurrentChapterId(nextChapterId);
            int firstSceneId = findFirstSceneIdInChapter(nextChapterId);
            if (firstSceneId != -1) {
                game.setCurrentSceneId(firstSceneId);
            }
            game.setLastActivityDate(new Date());
            System.out.println("Avanzado al capítulo: " + nextChapterId);
        } else {
            System.out.println("No hay más capítulos. Terminando el juego...");
            completeGame(game);
        }
    }

    // Guardar progreso
    public static void saveProgress(Game game) {
        // Simulación de persistencia (aquí se debería guardar en la base de datos)
        System.out.println("Progreso guardado para el jugador con ID: " + game.getPlayerId());
        System.out.println("Capítulo actual ID: " + game.getCurrentChapterId());
        System.out.println("Escena actual ID: " + game.getCurrentSceneId());
    }

    // Reanudar partida
    public static void resumeGame(Game game) {
        if (game.getState() == GameState.PAUSED) {
            game.setState(GameState.IN_PROGRESS);
            game.setLastActivityDate(new Date());
            System.out.println("Partida reanudada para el jugador con ID: " + game.getPlayerId());
        } else {
            System.out.println("La partida no está pausada y no puede ser reanudada.");
        }
    }

    // Terminar partida
    public static void completeGame(Game game) {
        game.setState(GameState.COMPLETED);
        game.setLastActivityDate(new Date());
        System.out.println("Juego completado: " + game.getName());
        System.out.println("Jugador con ID: " + game.getPlayerId());
    }

    // Métodos auxiliares para buscar IDs (simulación)
    private static int findNextSceneId(int currentSceneId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID de la siguiente escena o -1 si no hay más
        System.out.println("Buscando la siguiente escena después de la escena ID: " + currentSceneId);
        return currentSceneId + 1; // Ejemplo
    }

    private static int findNextChapterId(int currentChapterId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID del siguiente capítulo o -1 si no hay más
        System.out.println("Buscando el siguiente capítulo después del capítulo ID: " + currentChapterId);
        return currentChapterId + 1; // Ejemplo
    }

    private static int findFirstSceneIdInChapter(int chapterId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID de la primera escena del capítulo o -1 si no existe
        System.out.println("Buscando la primera escena en el capítulo ID: " + chapterId);
        return chapterId * 100 + 1; // Ejemplo
    }



    public static List<Game> getGamesForPlayer(int playerId) {
        List<Game> games = new ArrayList<>();

        String query = "SELECT id, name, player_id, start_date, last_activity_date, state, total_score, " +
            "current_chapter, current_scene " +
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
                    game.setLastActivityDate(rs.getDate("last_activity_date"));
                    game.setState(GameState.valueOf(rs.getString("state")));
                    game.setTotalScore(rs.getInt("total_score"));
                    game.setCurrentChapterId(rs.getInt("current_chapter"));
                    game.setCurrentSceneId(rs.getInt("current_scene"));

                    games.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }



    //RESTRICCIONES
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
