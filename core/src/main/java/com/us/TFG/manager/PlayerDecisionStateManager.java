package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


    public class PlayerDecisionStateManager {


        public static void updateDecisionState(int playerId, int gameId, int decisionId, boolean completed) {
            String query = "INSERT INTO player_decision_state (player_id, game_id, decision_id, completed) VALUES (?,?,?,?)";
            Connection connection = MyTfgGame.databaseManager.getConnection();

            if (connection == null) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, playerId);      // ID del jugador
                stmt.setInt(2, gameId);        // ID del juego
                stmt.setInt(3, decisionId);    // ID de la decisión
                stmt.setBoolean(4, completed); // Marcar como completada o no


                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Estado de la decisión actualizado correctamente.");
                } else {
                    System.out.println("No se encontró ninguna decisión para actualizar.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static boolean areAllDecisionsComplete(int playerId, int gameId, int sceneId) {
            String query = " SELECT COUNT(*) AS incomplete_count FROM decisions d LEFT JOIN player_decision_state" +
            " pds ON d.id = pds.decision_id AND pds.player_id = ? AND pds.game_id = ? WHERE d.scene_id = ?"+
            " AND (pds.completed IS NULL OR pds.completed = 0) ";

            Connection connection = MyTfgGame.databaseManager.getConnection();

            if (connection == null) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, playerId);
                stmt.setInt(2, gameId);
                stmt.setInt(3, sceneId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int incompleteCount = rs.getInt("incomplete_count");
                        return incompleteCount == 0; // Si no hay decisiones incompletas, todas están completas
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false; // Si ocurre un error o no se encuentran datos, devolver falso
        }





    }


