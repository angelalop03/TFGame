package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Decision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DecisionManager {
    public static Decision getFirstDecisionBySceneId(int sceneId) {
        String query = "SELECT * FROM decisions WHERE scene_id = ? ORDER BY decision_order ASC LIMIT 1";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        Decision decision = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, sceneId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                decision = new Decision(
                    rs.getInt("id"),
                    rs.getInt("scene_id"),
                    rs.getInt("decision_order"),
                    rs.getInt("character_id"),
                    rs.getString("dialogue")
                );
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return decision;
    }


    private static Decision mapDecisionFromResultSet(ResultSet rs) throws SQLException {
        Decision decision = new Decision();
        decision.setId(rs.getInt("id"));
        decision.setSceneId(rs.getInt("scene_id"));
        decision.setDecisionOrder(rs.getInt("decision_order"));
        decision.setDialogue(rs.getString("dialogue"));
        decision.setCharacterId(rs.getInt("character_id"));
        return decision;
    }

    public static Decision getDecisionById(int decisionId) {
        String query = "SELECT id, scene_id, character_id, dialogue, decision_order FROM decisions WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, decisionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Decision decision = new Decision();
                    decision.setId(rs.getInt("id"));
                    decision.setSceneId(rs.getInt("scene_id"));
                    decision.setCharacterId(rs.getInt("character_id"));
                    decision.setDialogue(rs.getString("dialogue"));
                    decision.setDecisionOrder(rs.getInt("decision_order"));
                    return decision;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no se encuentra la decisión
    }
}


