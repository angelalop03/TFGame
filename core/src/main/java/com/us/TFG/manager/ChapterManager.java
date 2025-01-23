package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.Game;
import com.us.TFG.model.PlayerChapterState;
import com.us.TFG.model.RelationshipState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChapterManager {

    /**
     * Obtiene todos los capítulos desbloqueados para un jugador específico.
     *
     * @param playerId ID del jugador.
     * @return Lista de capítulos desbloqueados.
     */
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

    public static int unlockNextChapter(int gameId, int currentChapterId) {
        String nextChaptersQuery = " SELECT id, chapter_number, required_relationship_state FROM chapters WHERE chapter_number > ( SELECT chapter_number FROM chapters WHERE id = ?) ORDER BY chapter_number ASC";

        String insertUnlockQuery = "INSERT INTO player_chapter_state (player_id, chapter_id, game_id, played, unlocked) VALUES (?, ?, ?, false, true)";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        int nextChapterId = -1;

        try {
            // Obtener todos los capítulos posteriores al actual
            PreparedStatement nextChaptersStmt = connection.prepareStatement(nextChaptersQuery);
            nextChaptersStmt.setInt(1, currentChapterId);

            ResultSet rs = nextChaptersStmt.executeQuery();
            Game currentGame = GameManager.getGameById(gameId);
            RelationshipState currentRelationshipState = currentGame.getRelationshipState();

            while (rs.next()) {
                int candidateChapterId = rs.getInt("id");
                String requiredRelationshipState = rs.getString("required_relationship_state");

                // Verificar si el estado de la relación cumple con el requisito del capítulo
                if (requiredRelationshipState == null || currentRelationshipState.name().equals(requiredRelationshipState)) {
                    // Insertar el desbloqueo del capítulo en player_chapter_state
                    PreparedStatement insertUnlockStmt = connection.prepareStatement(insertUnlockQuery);
                    insertUnlockStmt.setInt(1, currentGame.getPlayerId());
                    insertUnlockStmt.setInt(2, candidateChapterId);
                    insertUnlockStmt.setInt(3, gameId);

                    int rowsInserted = insertUnlockStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Capítulo " + candidateChapterId + " desbloqueado para el juego " + gameId);
                    } else {
                        System.err.println("El capítulo " + candidateChapterId + " ya estaba desbloqueado.");
                    }
                    insertUnlockStmt.close();

                    nextChapterId = candidateChapterId;
                    break; // Salir del bucle una vez se desbloquea el primer capítulo válido
                } else {
                    System.out.println("El estado de la relación actual (" + currentRelationshipState + ") no cumple con el requisito para desbloquear el capítulo " + candidateChapterId + " (" + requiredRelationshipState + ").");
                }
            }

            rs.close();
            nextChaptersStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextChapterId;
    }


}
