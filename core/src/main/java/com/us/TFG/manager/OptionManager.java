package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OptionManager {

    public static List<Option> getOptionsByDecisionId(int decisionId) {
        String query = "SELECT id, decision_id, text, points_m, points_r, next_decision_id, required_psychological_state " +
            "FROM options WHERE decision_id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        List<Option> optionsList = new ArrayList<>();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, decisionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Option option = new Option();
                    option.setId(rs.getInt("id"));
                    option.setDecisionId(rs.getInt("decision_id"));
                    option.setText(rs.getString("text"));
                    option.setPointsM(rs.getInt("points_m"));
                    option.setPointsR(rs.getInt("points_r"));
                    option.setNextDecisionId(rs.getObject("next_decision_id") != null ? rs.getInt("next_decision_id") : null);
                    option.setRequiredPsychologicalState(rs.getString("required_psychological_state"));

                    optionsList.add(option);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return optionsList;
    }
}
