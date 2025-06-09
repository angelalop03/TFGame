package com.us.TFG.manager;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.us.TFG.FireBase.NewsFireBase;
import com.us.TFG.FireBase.UserFireBase;
import com.us.TFG.MyTfgGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsManager {

    public static List<NewsData> getAllNews() {
        List<NewsData> newsList = new ArrayList<>();

        // 🔹 Cargar desde SQLite
        String query = "SELECT id, name, text FROM news";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    newsList.add(new NewsData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text")
                    ));
                }

                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("❌ No se pudo conectar a SQLite.");
        }

        // 🔹 Añadir desde Firebase
        List<NewsData> firebaseNews = NewsFireBase.getAllNewsFromFirebase();
        newsList.addAll(firebaseNews);

        return newsList;
    }


    public static boolean deleteNews(int newsId) {
        String deleteSql = "DELETE FROM news WHERE id = ?";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            System.out.println("❌ Error: No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
                stmt.setInt(1, newsId);
                stmt.executeUpdate();
            }

            connection.commit();

            // 🔁 Eliminar también en Firebase
            NewsFireBase.deleteNewsFromFirebase(newsId);

            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNews(int newsId, String newTitle, String newText) {
        String query = "UPDATE news SET name = ?, text = ? WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newTitle);
            stmt.setString(2, newText);
            stmt.setInt(3, newsId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Retorna false si ocurrió un error
    }

    public static boolean createNews(String title, String text) {
        String query = "INSERT INTO news (name, text) VALUES (?, ?)";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        int generatedId = -1;

        try {
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, title);
            stmt.setString(2, text);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                }
                keys.close();
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        boolean firebaseSuccess = false;
        if (generatedId != -1) {
            firebaseSuccess = NewsFireBase.createNewsInFirebase(generatedId, title, text);
        }

        return generatedId != -1 && firebaseSuccess;
    }


    // Clase auxiliar para encapsular los datos de una noticia
    public static class NewsData {
        private final int id;
        private final String name;
        private final String text;

        public NewsData(int id, String name, String text) {
            this.id = id;
            this.name = name;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }

    }
}
