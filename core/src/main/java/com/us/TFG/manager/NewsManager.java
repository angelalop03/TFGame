package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsManager {

    // Método para obtener todas las noticias de la base de datos
    public static List<NewsData> getAllNews() {
        String query = "SELECT id, name, text, image FROM news";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        List<NewsData> newsList = new ArrayList<>();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                newsList.add(new NewsData(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("text"),
                    rs.getString("image")
                ));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newsList;
    }

    // Método para eliminar una noticia por su ID
    public static boolean deleteNews(int newsId) {
        String query = "DELETE FROM news WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, newsId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0; // Retorna true si se eliminó una fila
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Retorna false si ocurrió un error
    }


    // Clase auxiliar para encapsular los datos de una noticia
    public static class NewsData {
        private final int id;
        private final String name;
        private final String text;
        private final String image;

        public NewsData(int id, String name, String text, String image) {
            this.id = id;
            this.name = name;
            this.text = text;
            this.image = image;
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

        public String getImage() {
            return image;
        }
    }
}
