package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.History;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    public static List<History> getHistories() {
        List<History> histories = new ArrayList<>();
        String query = "SELECT id, title,image_path,description FROM historys";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    History history = new History();
                    history.setId(rs.getInt("id"));
                    history.setTitle(rs.getString("title"));
                    history.setImagePath(rs.getString("image_path"));
                    history.setDescription(rs.getString("description"));


                    histories.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return histories;
    }
}
