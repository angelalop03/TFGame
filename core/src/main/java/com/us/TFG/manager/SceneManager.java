package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    // Método para obtener la primera escena de un capítulo
    public static int getFirstSceneId(int chapterId) {
        String query = "SELECT id FROM scenes WHERE chapter_id = ? ORDER BY scene_order ASC LIMIT 1";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);
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

    public static void updateCurrentScene(int gameId, int sceneId) {
        String query = "UPDATE games SET current_scene_id = ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sceneId);
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


    public static Scene getFirstSceneOfChapter( int chapterId) {
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

    public static List<Scene> getScenesByChapterId(int chapterId) {
        List<Scene> scenes = new ArrayList<>();
        String query = "SELECT * FROM scenes WHERE chapter_id = ? ORDER BY scene_order ASC";

        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chapterId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Scene scene = new Scene(
                        rs.getInt("id"),
                        rs.getInt("chapter_id"),
                        rs.getString("background_image"),
                        rs.getInt("scene_order")
                    );
                    scenes.add(scene);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scenes;
    }



}
