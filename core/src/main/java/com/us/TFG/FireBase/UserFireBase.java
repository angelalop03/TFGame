package com.us.TFG.FireBase;

import com.us.TFG.model.Authorities;
import com.us.TFG.model.Player;
import com.us.TFG.model.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserFireBase {

    private static final String PROJECT_ID = "tfgame-3ff2b";
    private static final String FIRESTORE_BASE_URL =
        "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents";

    public static void registerUserFire(User user) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/users/" + user.getUsername());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String json = "{\n" +
                "  \"fields\": {\n" +
                "    \"username\": { \"stringValue\": \"" + user.getUsername() + "\" },\n" +
                "    \"password\": { \"stringValue\": \"" + user.getPassword() + "\" },\n" +
                "    \"authority\": { \"stringValue\": \"" + user.getAuthority().toString() + "\" },\n" +
                "    \"id\": { \"integerValue\": \"" + user.getId() + "\" }\n" +
                "  }\n" +
                "}";

            System.out.println("JSON enviado:\n" + json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("User subido a Firebase. Código: " + responseCode);

        } catch (Exception e) {
            System.err.println("Error al subir usuario a Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void registerPlayerFire(Player player) {
        try {
            int newId = player.getUserId(); // Paso 1: buscar ID disponible
            player.setId(newId); // Paso 2: asignarlo al player

            URL url = new URL(FIRESTORE_BASE_URL + "/players/" + newId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String json = "{\n" +
                "  \"fields\": {\n" +
                "    \"firstname\": { \"stringValue\": \"" + player.getFirstname() + "\" },\n" +
                "    \"lastname\": { \"stringValue\": \"" + player.getLastname() + "\" },\n" +
                "    \"email\": { \"stringValue\": \"" + player.getEmail() + "\" },\n" +
                "    \"image\": { \"stringValue\": \"" + player.getImage() + "\" },\n" +
                "    \"userId\": { \"integerValue\": \"" + player.getUserId() + "\" }\n" +
                "  }\n" +
                "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("✅ Jugador creado con ID " + newId + ". Código: " + responseCode);

        } catch (Exception e) {
            System.err.println("❌ Error al subir jugador a Firebase: " + e.getMessage());
        }
    }


    public static User verifyLoginFire(String username) {
        try {
            String userPath = FIRESTORE_BASE_URL + "/users/" + username;

            HttpURLConnection conn = (HttpURLConnection) new URL(userPath).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                String json = response.toString();
                String password = extraerCampoFire(json, "password");
                String authorityString = extraerCampoFire(json, "authority");

                Authorities authority = Authorities.valueOf(authorityString);

                return new User(username, password, authority);
            } else {
                System.out.println("Firebase => Usuario no encontrado");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static boolean usernameExistsInFirebase(String username) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/users/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            System.err.println("Error al verificar en Firebase: " + e.getMessage());
            return false;
        }
    }

    public static Map<String, String> getUserProfileFromFirebase(String username) {
        Map<String, String> data = new HashMap<>();

        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/users/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String json = responseBuilder.toString();

                // Solo extraer el nombre de usuario
                data.put("username", extraerCampoFire(json, "username"));

            } else {
                System.out.println("⚠️ Firebase no encontró el usuario. Código: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Error leyendo desde Firebase: " + e.getMessage());
        }

        return data;
    }



    public static boolean updatePasswordFire(String username, String hashedPassword) {
        try {
            // Construir la URL con updateMask para evitar que se borren otros campos
            String patchUrl = FIRESTORE_BASE_URL + "/users/" + username + "?updateMask.fieldPaths=password";

            URL url = new URL(patchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST"); // usamos POST con override
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Estructura correcta del cuerpo JSON
            String json = "{\n" +
                "  \"fields\": {\n" +
                "    \"password\": { \"stringValue\": \"" + hashedPassword + "\" }\n" +
                "  }\n" +
                "}";

            // Enviar el cuerpo
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            // Leer respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Contraseña actualizada correctamente en Firebase");
                return true;
            } else {
                System.out.println("❌ Error al actualizar contraseña en Firebase: " + responseCode);

                // Leer mensaje de error detallado de Firebase
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    errorReader.close();
                    System.out.println("🔍 Firebase error response: " + errorResponse);
                }

                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error en updatePasswordFire: " + e.getMessage());
            return false;
        }
    }

    //-------

    public static void updateUserAndPlayerFire(String currentUsername, String newUsername,
                                               String firstname, String lastname,
                                               String email, String profileImage, int userId, int playerId) {
        try {
            if (!currentUsername.equals(newUsername)) {
                // 1. Obtener datos actuales del usuario
                URL currentUserUrl = new URL(FIRESTORE_BASE_URL + "/users/" + currentUsername);
                HttpURLConnection getConn = (HttpURLConnection) currentUserUrl.openConnection();
                getConn.setRequestMethod("GET");
                getConn.setRequestProperty("Content-Type", "application/json");

                if (getConn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getConn.getInputStream()));
                    StringBuilder currentJson = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        currentJson.append(line);
                    }
                    reader.close();

                    // 2. Crear nuevo documento con ID = newUsername
                    URL newUserUrl = new URL(FIRESTORE_BASE_URL + "/users/" + newUsername);
                    HttpURLConnection createConn = (HttpURLConnection) newUserUrl.openConnection();
                    createConn.setRequestMethod("POST");
                    createConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                    createConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    createConn.setDoOutput(true);

                    String newUserJson = "{\n" +
                        "  \"fields\": {\n" +
                        "    \"username\": { \"stringValue\": \"" + newUsername + "\" },\n" +
                        "    \"password\": { \"stringValue\": " + extraerCampoFire(currentJson.toString(), "password") + " },\n" +
                        "    \"authority\": { \"stringValue\": " + extraerCampoFire(currentJson.toString(), "authority") + " },\n" +
                        "    \"id\": { \"integerValue\": \"" + userId + "\" }\n" +
                        "  }\n" +
                        "}";

                    try (OutputStream os = createConn.getOutputStream()) {
                        os.write(newUserJson.getBytes(StandardCharsets.UTF_8));
                    }

                    // 3. Eliminar el antiguo documento
                    URL deleteUrl = new URL(FIRESTORE_BASE_URL + "/users/" + currentUsername);
                    HttpURLConnection deleteConn = (HttpURLConnection) deleteUrl.openConnection();
                    deleteConn.setRequestMethod("DELETE");
                    deleteConn.getResponseCode(); // Ejecuta

                } else {
                    System.out.println("⚠️ No se pudo obtener el usuario original desde Firebase");
                }

            } else {
                // Solo actualizar el campo username (aunque sea el mismo)
                URL userUrl = new URL(FIRESTORE_BASE_URL + "/users/" + currentUsername);
                HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();
                userConn.setRequestMethod("POST");
                userConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                userConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                userConn.setDoOutput(true);

                String userJson = "{\n" +
                    "  \"fields\": {\n" +
                    "    \"username\": { \"stringValue\": \"" + newUsername + "\" }\n" +
                    "  }\n" +
                    "}";

                try (OutputStream os = userConn.getOutputStream()) {
                    os.write(userJson.getBytes(StandardCharsets.UTF_8));
                }
            }

            // Actualizar player (igual en ambos casos)
            URL playerUrl = new URL(FIRESTORE_BASE_URL + "/players/" + playerId);
            HttpURLConnection playerConn = (HttpURLConnection) playerUrl.openConnection();
            playerConn.setRequestMethod("POST");
            playerConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            playerConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            playerConn.setDoOutput(true);

            String playerJson = "{\n" +
                "  \"fields\": {\n" +
                "    \"firstname\": { \"stringValue\": \"" + firstname + "\" },\n" +
                "    \"lastname\": { \"stringValue\": \"" + lastname + "\" },\n" +
                "    \"email\": { \"stringValue\": \"" + email + "\" },\n" +
                "    \"image\": { \"stringValue\": \"" + profileImage + "\" },\n" +
                "    \"userId\": { \"integerValue\": \"" + userId + "\" }\n" +
                "  }\n" +
                "}";

            try (OutputStream os = playerConn.getOutputStream()) {
                os.write(playerJson.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("✅ Perfil actualizado en Firebase");

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar perfil en Firebase: " + e.getMessage());
        }
    }



    public static void deleteUserFromFirebase(String username) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/users/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("🗑️ Usuario eliminado de Firebase");
            } else if (responseCode == 404) {
                System.out.println("⚠️ Usuario no encontrado en Firebase");
            } else {
                System.out.println("❌ Error al eliminar en Firebase: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar usuario en Firebase: " + e.getMessage());
        }
    }

    public static String getUsernameByPlayerIdFirebase(int playerId) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/players/" + playerId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String json = responseBuilder.toString();
                return extractFieldFromJson(json, "userId");
            }

        } catch (Exception e) {
            System.err.println("❌ Error en getUsernameByPlayerIdFirebase: " + e.getMessage());
        }

        return null;
    }


    // 🔍 Extrae campos simples del JSON de Firestore
    private static String extraerCampoFire(String json, String campo) {
        try {
            int index = json.indexOf("\"" + campo + "\":");
            if (index == -1) return null;
            int start = json.indexOf("\"stringValue\"", index);
            int quote1 = json.indexOf("\"", start + 14);
            int quote2 = json.indexOf("\"", quote1 + 1);
            return json.substring(quote1 + 1, quote2);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractFieldFromJson(String json, String fieldName) {
        // Primero busca como string
        String marker = "\"" + fieldName + "\": { \"stringValue\": \"";
        int start = json.indexOf(marker);
        if (start == -1) {
            // Luego busca como integer
            marker = "\"" + fieldName + "\": { \"integerValue\": \"";
            start = json.indexOf(marker);
        }

        if (start != -1) {
            int begin = start + marker.length();
            int end = json.indexOf("\"", begin);
            if (end != -1) {
                return json.substring(begin, end);
            }
        }

        return null;
    }



}
