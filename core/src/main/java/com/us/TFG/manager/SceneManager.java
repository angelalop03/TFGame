package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SceneManager {

    public static Scene getSceneById(int sceneId) {
        String query = "SELECT * FROM scenes WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        Scene scene = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sceneId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    scene = new Scene(
                        rs.getInt("id"),
                        rs.getInt("chapter_id"),
                        rs.getString("background_image"),
                        rs.getInt("scene_order")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scene;
    }

    public static Scene getNextSceneInChapter(int currentSceneId, int chapterId) {
        String query = "SELECT * FROM scenes WHERE chapter_id = ? AND scene_order = (SELECT scene_order + 1 FROM scenes WHERE id = ?)";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        Scene nextScene = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);       // Capítulo actual
            stmt.setInt(2, currentSceneId);  // ID de la escena actual

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nextScene = new Scene(
                        rs.getInt("id"),
                        rs.getInt("chapter_id"),
                        rs.getString("background_image"),
                        rs.getInt("scene_order")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextScene;
    }


    public static Scene getFirstSceneOfChapter(int gameId, int chapterId) {
        String query = "SELECT * FROM scenes WHERE chapter_id = ? ORDER BY scene_order ASC LIMIT 1";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        Scene firstScene = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    firstScene = new Scene(
                        rs.getInt("id"),
                        rs.getInt("chapter_id"),
                        rs.getString("background_image"),
                        rs.getInt("scene_order")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstScene;
    }


}
