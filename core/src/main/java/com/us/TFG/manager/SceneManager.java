package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SceneManager {

    public static Scene getFirstSceneOfChapter(int gameId, int chapterId) {
        String query = "SELECT * FROM scenes WHERE chapter_id = ? ORDER BY scene_order ASC LIMIT 1";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        Scene scene = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, chapterId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                scene = new Scene(
                    rs.getInt("chapter_id"),
                    rs.getString("background_image"),
                    rs.getInt("scene_order")
                );
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scene;
    }


    private static Scene mapSceneFromResultSet(ResultSet rs) throws SQLException {
        Scene scene = new Scene();
        scene.setId(rs.getInt("id"));
        scene.setChapterId(rs.getInt("chapter_id"));
        scene.setBackgroundImage(rs.getString("background_image"));
        scene.setSceneOrder(rs.getInt("scene_order"));
        return scene;
    }
}
