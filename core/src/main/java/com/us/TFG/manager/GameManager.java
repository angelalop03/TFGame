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

    public static Game getGameById(int gameId) {
        String query = "SELECT * FROM games WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        Game game = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    game = new Game(
                        rs.getString("name"),
                        rs.getInt("player_id"),
                        rs.getInt("history_id")
                    );
                    game.setId(rs.getInt("id"));
                    game.setStartDate(rs.getDate("start_date"));
                    game.setState(GameState.valueOf(rs.getString("state")));
                    game.setPointsM(rs.getInt("points_m"));
                    game.setPointsR(rs.getInt("points_r"));
                    game.setCurrentChapterId(rs.getInt("current_chapter_id"));
                    game.setCurrentSceneId(rs.getInt("current_scene_id"));
                    game.setCurrentDecisionId(rs.getInt("current_decision_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    // Método para desbloquear el primer capítulo
    private static void unlockFirstChapter(int playerId, Game game, Integer historyId) {
        int firstChapterId = findFirstChapter(historyId);

        if (firstChapterId == -1) {
            System.err.println("No se encontró un capítulo inicial para la historia con ID: " + historyId);
            return;
        }

        game.setCurrentChapterId(firstChapterId);

        // Obtener la primera escena del capítulo
        int firstSceneId = getFirstSceneId(firstChapterId);
        updateCurrentScene(game.getId(), firstSceneId);

        // Obtener la primera decisión de la escena
        int firstDecisionId = getFirstDecisionId(firstSceneId);
        updateCurrentDecision(game.getId(), firstDecisionId);

        // Marcar el capítulo como desbloqueado en player_chapter_state
        unlockChapterForPlayer(playerId, game.getId(), firstChapterId);

        System.out.println("Primer capítulo desbloqueado y actualizado correctamente.");
    }

    // Método para actualizar player_chapter_state a unlocked = true
    private static void unlockChapterForPlayer(int playerId, int gameId, int chapterId) {
        String query = "INSERT INTO player_chapter_state (player_id, chapter_id, game_id, played, unlocked) " +
            "VALUES (?, ?, ?, ?, ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, chapterId);
            stmt.setInt(3, gameId);
            stmt.setBoolean(4, false); // No jugado aún
            stmt.setBoolean(5, true); // Desbloqueado

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("El capítulo con ID " + chapterId + " ha sido desbloqueado para el juego con ID " + gameId);
            } else {
                System.err.println("Error al actualizar el estado del capítulo en player_chapter_state.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Game createGame(String name, int playerId, int historyId) {
        // Crear el objeto Game
        Game game = new Game(name, playerId, historyId);
        game.setStartDate(new Date());
        game.setState(GameState.IN_PROGRESS);
        game.setPointsM(0);
        game.setPointsR(0);
        game.setCurrentChapterId(1); // Desbloquear el primer capítulo
        game.setCurrentSceneId(1); // Primera escena
        game.setCurrentDecisionId(1); // Primera Decision

        String query = "INSERT INTO games (name,history_id, current_chapter_id, current_scene_id, current_decision_id, player_id, start_date, state, points_m, points_r) " +
            "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getName());
            stmt.setInt(2, game.getHistoryId());
            stmt.setInt(3, game.getCurrentChapterId());
            stmt.setInt(4, game.getCurrentSceneId());
            stmt.setInt(5, game.getCurrentDecisionId());
            stmt.setInt(6, game.getPlayerId());
            stmt.setDate(7, new java.sql.Date(game.getStartDate().getTime()));
            stmt.setString(8, game.getState().toString());
            stmt.setInt(9, game.getPointsM());
            stmt.setInt(10, game.getPointsR());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        game.setId(generatedKeys.getInt(1)); // Asignar el ID generado al objeto Game
                        System.out.println("Juego creado y guardado en la base de datos: " + game);

                        // Desbloquear el primer capítulo
                        unlockFirstChapter(playerId, game, historyId);
                    } else {
                        throw new SQLException("No se pudo obtener el ID del juego insertado.");
                    }
                }
            } else {
                System.err.println("Error al guardar el juego en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    private static int findFirstChapter(int historyId) {
        String query = "SELECT id FROM chapters WHERE history_id = ? ORDER BY chapter_number ASC LIMIT 1";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, historyId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Retorna -1 si no se encuentra un capítulo
    }

    // Método para obtener la primera escena de un capítulo
    private static int getFirstSceneId(int chapterId) {
        String query = "SELECT id FROM scenes WHERE chapter_id = ? ORDER BY scene_order ASC LIMIT 1";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retornar -1 si no se encuentra
    }

    // Método para obtener la primera decisión de una escena
    private static int getFirstDecisionId(int sceneId) {
        String query = "SELECT id FROM decisions WHERE scene_id = ? ORDER BY decision_order ASC LIMIT 1";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sceneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retornar -1 si no se encuentra
    }

    public static void updateCurrentChapter(int gameId, int chapterId) {
        String query = "UPDATE games SET current_chapter_id = ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);
            stmt.setInt(2, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Game> getGamesForPlayer(int playerId) {
        List<Game> games = new ArrayList<>();

        String query = "SELECT id, name,history_id, player_id, start_date, state, points_m, points_r, current_chapter_id, current_scene_id, current_decision_id " +
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
                    Game game = new Game(
                        rs.getString("name"),
                        rs.getInt("player_id"),
                        rs.getInt("history_id")
                    );
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

    public static void updatePoints(int gameId, int pointsMDelta, int pointsRDelta) {
        String query = "UPDATE games SET points_m = points_m + ?, points_r = points_r + ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pointsMDelta);
            stmt.setInt(2, pointsRDelta);
            stmt.setInt(3, gameId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Puntos actualizados exitosamente para el juego con ID: " + gameId);
            } else {
                System.err.println("Error al actualizar puntos para el juego con ID: " + gameId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCurrentDecision(int gameId, int decisionId) {
        String query = "UPDATE games SET current_decision_id = ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, decisionId);
            stmt.setInt(2, gameId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Decisión actual actualizada para el juego con ID: " + gameId);
            } else {
                System.err.println("Error al actualizar la decisión actual para el juego con ID: " + gameId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCurrentScene(int gameId, int sceneId) {
        String query = "UPDATE games SET current_scene_id = ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sceneId);
            stmt.setInt(2, gameId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Decisión actual actualizada para el juego con ID: " + gameId);
            } else {
                System.err.println("Error al actualizar la decisión actual para el juego con ID: " + gameId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void markChapterAsPlayed(int gameId, int chapterId) {
        String query = "UPDATE player_chapter_state SET played = true WHERE game_id = ? AND chapter_id = ?";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            stmt.setInt(2, chapterId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("El capítulo con ID " + chapterId + " ha sido marcado como jugado para el juego con ID " + gameId);
            } else {
                System.err.println("No se pudo marcar el capítulo como jugado. Verifique que el estado del capítulo existe en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
