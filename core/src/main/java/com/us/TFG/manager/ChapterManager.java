package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.PlayerChapterState;

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
    public static List<Chapter> getChaptersForPlayer(int playerId) {
        List<Chapter> unlockedChapters = new ArrayList<>();

        String query = "SELECT c.id, c.name, c.chapter_number, c.image_path, c.required_relationship_state " +
            "FROM chapters c " +
            "JOIN player_chapter_state pcs ON c.id = pcs.chapter_id " +
            "WHERE pcs.player_id = ? AND pcs.unlocked = true";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);

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
}
