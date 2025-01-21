package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.PlayerDecisionState;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

    public class PlayerDecisionStateManager {


        public static void updateDecisionState(int playerId, int gameId, int decisionId, boolean completed) {
            String query = "UPDATE player_decision_state SET completed = ? WHERE player_id = ? AND game_id = ? AND decision_id = ?";
            Connection connection = MyTfgGame.databaseManager.getConnection();

            if (connection == null) {
                throw new RuntimeException("No se pudo conectar a la base de datos.");
            }

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setBoolean(1, completed); // Marcar como completada o no
                stmt.setInt(2, playerId);      // ID del jugador
                stmt.setInt(3, gameId);        // ID del juego
                stmt.setInt(4, decisionId);    // ID de la decisión

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




    }


