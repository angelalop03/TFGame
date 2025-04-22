package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Decision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    // Método para obtener la primera decisión de una escena
    public static int getFirstDecisionId(int sceneId) {
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

        return null;
    }


    public static List<Decision> getDecisionsBySceneId(int sceneId) {
        List<Decision> decisions = new ArrayList<>();
        String query = "SELECT * FROM decisions WHERE scene_id = ? ORDER BY decision_order ASC";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sceneId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    decisions.add(new Decision(
                        rs.getInt("id"),
                        rs.getInt("scene_id"),
                        rs.getInt("decision_order"),
                        rs.getInt("character_id"),
                        rs.getString("dialogue")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return decisions;
    }

    public static int createDecision(Decision decision) {
        String query = "INSERT INTO decisions (scene_id, decision_order, character_id, dialogue) VALUES (?, ?, ?, ?)";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, decision.getSceneId());
            stmt.setInt(2, decision.getDecisionOrder());
            stmt.setInt(3, decision.getCharacterId());
            stmt.setString(4, decision.getDialogue());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La creación de la decisión falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La creación de la decisión falló, no se obtuvo el ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }




}


