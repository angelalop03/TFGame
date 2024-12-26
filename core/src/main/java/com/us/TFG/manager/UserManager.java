package com.us.TFG.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Authorities;
import com.us.TFG.model.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private static Label feedbackLabel;

    public static boolean registerUser(String username, String password, String firstname, String lastname, String email) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String userSql = "INSERT INTO users (username, password, authority" +
            ") VALUES (?, ?, ?)";
        String playerSql = "INSERT INTO players (firstname, lastname, email, image, pointsM, pointsR, user_id) VALUES (?, ?, ?, 'default_image.png', 0, 0, ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        try {
            connection.setAutoCommit(false);

            // Insertar usuario
            try (PreparedStatement userStmt = connection.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, username);
                userStmt.setString(2, hashedPassword); // Guardar contraseña encriptada
                userStmt.setInt(3, 2); // Establecer authorities a 2
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insertar jugador
                    try (PreparedStatement playerStmt = connection.prepareStatement(playerSql)) {
                        playerStmt.setString(1, firstname);
                        playerStmt.setString(2, lastname);
                        playerStmt.setString(3, email);
                        playerStmt.setInt(4, userId);
                        playerStmt.executeUpdate();
                    }
                }
            }
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }





    public static boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            showError("Error: No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;
            rs.close();
            stmt.close();
            return exists;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateProfile(String currentUsername, String newUsername, String password, String firstname,
                                        String lastname, String email, String profileImage) {

        // Comprobar si el nuevo nombre de usuario ya existe, excepto si es el mismo que el actual
        if (!newUsername.equals(currentUsername) && usernameExists(newUsername)) {
            showError("El nombre de usuario ya está en uso.");
            return false;
        }

        // Si no se proporciona una nueva imagen, se usa la imagen actual (o una predeterminada si es necesario)
        if (profileImage == null || profileImage.isEmpty()) {
            profileImage = PlayerManager.getCurrentProfileImage(currentUsername); // Obtener imagen actual si no se proporciona una nueva
        }

        String updateUserSql = "UPDATE users SET username = ?, password = ? WHERE username = ?";
        String updatePlayerSql = "UPDATE players SET firstname = ?, lastname = ?, email = ?, image = ? WHERE user_id = (SELECT id FROM users WHERE username = ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            showError("Error: No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            connection.setAutoCommit(false);

            PreparedStatement userStmt = connection.prepareStatement(updateUserSql);
            userStmt.setString(1, newUsername);
            userStmt.setString(2, password);
            userStmt.setString(3, currentUsername);
            userStmt.executeUpdate();

            PreparedStatement playerStmt = connection.prepareStatement(updatePlayerSql);
            playerStmt.setString(1, firstname);
            playerStmt.setString(2, lastname);
            playerStmt.setString(3, email);
            playerStmt.setString(4, profileImage); // Asignar la imagen
            playerStmt.setString(5, newUsername);
            playerStmt.executeUpdate();

            userStmt.close();
            playerStmt.close();

            connection.commit();
            currentUsername = newUsername;
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





    public static boolean verifyLogin(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement statement = MyTfgGame.databaseManager.getConnection().prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, storedPassword); // Comparar contraseñas
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar login: " + e.getMessage());
        }
        return false;
    }


    private static void showError(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setColor(Color.RED);
    }


    //Pasar esto a UserService
    public static void loadUserProfile(String currentUsername, TextField usernameField, TextField passwordField,
                                 TextField firstnameField, TextField lastnameField, TextField emailField, String currentProfileImage , Image profileImage) {
        String query = "SELECT username, password, firstname, lastname, email,image FROM users u JOIN players p ON u.id = p.user_id WHERE username = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            showError("Error: No se pudo conectar a la base de datos.");
            return;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                passwordField.setText(rs.getString("password"));
                firstnameField.setText(rs.getString("firstname"));
                lastnameField.setText(rs.getString("lastname"));
                emailField.setText(rs.getString("email"));
                currentProfileImage = rs.getString("image");
                profileImage.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("assets/photos/players/" + currentProfileImage))));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Authorities getAuthority(String username) {
        String query = "SELECT authority FROM users WHERE username = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            throw new RuntimeException("Error: No se pudo conectar a la base de datos.");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int authorityValue = rs.getInt("authority");
                rs.close();
                stmt.close();
                return Authorities.fromValue(authorityValue); // Convertir el valor entero al enum Authorities
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si no se encuentra el usuario o hay un error, lanzar una excepción o manejar de forma adecuada
        throw new RuntimeException("No se encontró el usuario o la autoridad no es válida.");
    }






}

