package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.Game;
import com.us.TFG.model.PlayerChapterState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChapterManager {


    public static List<Chapter> getChaptersForPlayerandGame(int playerId, int gameId) {
        List<Chapter> unlockedChapters = new ArrayList<>();

        String query = "SELECT c.id, c.name, c.chapter_number, c.image_path, c.required_relationship_state " +
            "FROM chapters c " +
            "JOIN player_chapter_state pcs ON c.id = pcs.chapter_id " +
            "WHERE pcs.player_id = ? AND pcs.game_id= ? AND pcs.unlocked = true";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2,gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Chapter chapter = new Chapter();
                    chapter.setId(rs.getInt("id"));
                    chapter.setName(rs.getString("name"));
                    chapter.setChapterNumber(rs.getString("chapter_number"));
                    chapter.setImagePath(rs.getString("image_path"));
                    chapter.setRequiredRelationshipState(rs.getString("required_relationship_state"));

                    unlockedChapters.add(chapter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unlockedChapters;
    }

    public static int findFirstChapter(int historyId) {
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

    // Método para desbloquear el primer capítulo
    public static void unlockFirstChapter(int playerId, Game game, Integer historyId) {
        int firstChapterId = findFirstChapter(historyId);

        if (firstChapterId == -1) {
            System.err.println("No se encontró un capítulo inicial para la historia con ID: " + historyId);
            return;
        }

        game.setCurrentChapterId(firstChapterId);

        // Obtener la primera escena del capítulo
        int firstSceneId = SceneManager.getFirstSceneId(firstChapterId);
        SceneManager.updateCurrentScene(game.getId(), firstSceneId);

        // Obtener la primera decisión de la escena
        int firstDecisionId = DecisionManager.getFirstDecisionId(firstSceneId);
        DecisionManager.updateCurrentDecision(game.getId(), firstDecisionId);

        // Marcar el capítulo como desbloqueado en player_chapter_state
        PlayerChapterStateManager.unlockChapterForPlayer(playerId, game.getId(), firstChapterId);

        System.out.println("Primer capítulo desbloqueado y actualizado correctamente.");
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
