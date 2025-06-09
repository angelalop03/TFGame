package com.us.TFG.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.us.TFG.FireBase.PlayerFireBase;
import com.us.TFG.MyTfgGame;
import com.us.TFG.model.Authorities;
import com.us.TFG.model.Player;
import com.us.TFG.model.User;
import com.us.TFG.FireBase.UserFireBase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserManager {

    public static String findUserByPlayerId(int playerId) {
        // 🔁 Intentar buscar en Firebase primero
        String firebaseUsername = UserFireBase.getUsernameByPlayerIdFirebase(playerId);
        if (firebaseUsername != null) return firebaseUsername;

        // 🧱 Si falla Firebase, usar SQLite local
        String query = "SELECT u.username FROM users u JOIN players p ON u.id = p.user_id WHERE p.id = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return null;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                rs.close();
                stmt.close();
                return username;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static boolean registerUser(String username, String password, String firstname, String lastname, String email) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (usernameExists(username)) return false;

        String userSql = "INSERT INTO users (username, password, authority) VALUES (?, ?, ?)";
        String playerSql = "INSERT INTO players (firstname, lastname, email, image, user_id) VALUES (?, ?, ?, 'default_image.png', ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        try {
            connection.setAutoCommit(false);

            int userId = -1;
            try (PreparedStatement userStmt = connection.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, username);
                userStmt.setString(2, hashedPassword);
                userStmt.setInt(3, 2); // PLAYER
                userStmt.executeUpdate();

                ResultSet keys = userStmt.getGeneratedKeys();
                if (keys.next()) {
                    userId = keys.getInt(1);
                }
            }

            try (PreparedStatement playerStmt = connection.prepareStatement(playerSql)) {
                playerStmt.setString(1, firstname);
                playerStmt.setString(2, lastname);
                playerStmt.setString(3, email);
                playerStmt.setInt(4, userId);
                playerStmt.executeUpdate();
            }

            connection.commit();

            // 🔁 Guardar también en Firebase
            User user = new User(username, hashedPassword, Authorities.PLAYER);
            user.setId(userId);
            UserFireBase.registerUserFire(user);

            Player player = new Player(firstname,lastname,email, "default_image.png", userId);
            UserFireBase.registerPlayerFire(player);


            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }


    public static boolean usernameExists(String username) {
        // 1. Buscar en Firebase
        if (UserFireBase.usernameExistsInFirebase(username)) {
            return true;
        }

        // 2. Fallback a SQLite
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) return false;

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


    public static boolean updateProfile(String currentUsername, String newUsername,
                                        String firstname, String lastname,
                                        String email, String profileImage) {

        if (profileImage == null || profileImage.isEmpty()) {
            profileImage = PlayerManager.getCurrentProfileImage(currentUsername);
        }

        if (!newUsername.equals(currentUsername) && usernameExists(newUsername)) {
            return false;
        }

        String updateUserSql = "UPDATE users SET username = ? WHERE username = ?";
        String updatePlayerSql = "UPDATE players SET firstname = ?, lastname = ?, email = ?, image = ? WHERE user_id = (SELECT id FROM users WHERE username = ?)";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            connection.setAutoCommit(false);

            PreparedStatement userStmt = connection.prepareStatement(updateUserSql);
            userStmt.setString(1, newUsername);
            userStmt.setString(2, currentUsername);
            userStmt.executeUpdate();

            PreparedStatement playerStmt = connection.prepareStatement(updatePlayerSql);
            playerStmt.setString(1, firstname);
            playerStmt.setString(2, lastname);
            playerStmt.setString(3, email);
            playerStmt.setString(4, profileImage);
            playerStmt.setString(5, newUsername);
            playerStmt.executeUpdate();

            // Obtener ID del user y del player
            int userId = -1;
            int playerId = -1;

            try (PreparedStatement idStmt = connection.prepareStatement(
                "SELECT u.id as uid, p.id as pid FROM users u JOIN players p ON u.id = p.user_id WHERE u.username = ?")) {
                idStmt.setString(1, newUsername);
                ResultSet rs = idStmt.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("uid");
                    playerId = rs.getInt("pid");
                }
                rs.close();
            }

            connection.commit();

            // 🔁 Sincronizar con Firebase
            UserFireBase.updateUserAndPlayerFire(currentUsername, newUsername,
                firstname, lastname, email, profileImage, userId, playerId);

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
        // 🔍 1. Intentar en Firebase primero
        User user = UserFireBase.verifyLoginFire(username);

        if (user != null) {
            // Comparar contraseña usando BCrypt
            if (BCrypt.checkpw(password, user.getPassword())) {
                System.out.println("Login verificado desde Firebase");
                return true;
            } else {
                System.out.println("Contraseña incorrecta en Firebase");
                return false;
            }
        }

        // 🔁 2. Fallback: buscar en la base de datos local (SQLite)
        String sql = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement stmt = MyTfgGame.databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (BCrypt.checkpw(password, storedPassword)) {
                    System.out.println("Login verificado desde SQLite");
                    return true;
                } else {
                    System.out.println("Contraseña incorrecta en SQLite");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar login en SQLite: " + e.getMessage());
        }

        System.out.println("Usuario no encontrado en Firebase ni SQLite");
        return false;
    }






    public static void loadUserProfile(String currentUsername, TextField usernameField,
                                       TextField firstnameField, TextField lastnameField,
                                       TextField emailField, String currentProfileImage, Image profileImage) {

        // 1. Obtener username desde Firebase
        Map<String, String> userData = UserFireBase.getUserProfileFromFirebase(currentUsername);
        // 2. Obtener playerData desde Firebase
        PlayerManager.PlayerData playerData = PlayerFireBase.getPlayerDataByUsernameFire(currentUsername);

        if (!userData.isEmpty() && playerData != null) {
            // Setear campos
            usernameField.setText(userData.get("username"));
            firstnameField.setText(playerData.getFirstname());
            lastnameField.setText(playerData.getLastname());
            emailField.setText(playerData.getEmail());
            currentProfileImage = playerData.getImage();


            if (currentProfileImage != null && !currentProfileImage.isEmpty()) {
                profileImage.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal("assets/photos/players/" + currentProfileImage))));
            }
            return;
        }

        // 3. Fallback: cargar desde SQLite
        String query = "SELECT username, firstname, lastname, email, image FROM users u JOIN players p ON u.id = p.user_id WHERE username = ?";
        Connection connection = MyTfgGame.databaseManager.getConnection();

        if (connection == null) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                firstnameField.setText(rs.getString("firstname"));
                lastnameField.setText(rs.getString("lastname"));
                emailField.setText(rs.getString("email"));
                currentProfileImage = rs.getString("image");
                profileImage.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal("assets/photos/players/" + currentProfileImage))));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static Authorities getAuthority(String username) {
        try {
            // 1. Buscar en Firebase
            User user = UserFireBase.verifyLoginFire(username);
            if (user != null && user.getAuthority() != null) {
                return user.getAuthority();
            }

            // 2. Fallback: Buscar en SQLite
            String query = "SELECT authority FROM users WHERE username = ?";
            Connection connection = MyTfgGame.databaseManager.getConnection();

            if (connection != null) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, username);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int authorityValue = rs.getInt("authority");
                        return Authorities.fromValue(authorityValue);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener autoridad: " + e.getMessage());
        }

        // Si no se encontró en Firebase ni SQLite
        System.out.println("No se encontró el usuario ni su autoridad");
        return Authorities.PLAYER; // O puedes devolver null o lanzar excepción controlada si prefieres
    }




    public static boolean updatePassword(String currentUsername, String newPassword) {
        try {
            // Encriptar nueva contraseña
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            // Actualizar directamente en Firebase
            UserFireBase.updatePasswordFire(currentUsername, hashedPassword);

            System.out.println("✅ Contraseña actualizada correctamente en Firebase.");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar la contraseña en Firebase: " + e.getMessage());
            return false;
        }
    }





    public static boolean deleteUser(String username) {
        String deletePlayerSql = "DELETE FROM players WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        String deleteUserSql = "DELETE FROM users WHERE username = ?";

        Connection connection = MyTfgGame.databaseManager.getConnection();
        if (connection == null) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            connection.setAutoCommit(false);

            // Eliminar de la tabla de jugadores
            try (PreparedStatement playerStmt = connection.prepareStatement(deletePlayerSql)) {
                playerStmt.setString(1, username);
                playerStmt.executeUpdate();
            }

            // Eliminar de la tabla de usuarios
            try (PreparedStatement userStmt = connection.prepareStatement(deleteUserSql)) {
                userStmt.setString(1, username);
                userStmt.executeUpdate();
            }

            connection.commit();

            // 🔁 También eliminar en Firebase
            UserFireBase.deleteUserFromFirebase(username);

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


}

