package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.us.TFG.model.CharacterGame;


public class CharacterManager {

    public static CharacterGame getCharacterById(int characterId) {
        String query = "SELECT * FROM characters WHERE id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();
        CharacterGame character = null;

        if (connection == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, characterId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                character = new CharacterGame(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("image_path")
                );
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return character;
    }
}
