package com.us.TFG.FireBase;

import com.us.TFG.manager.PlayerManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerFireBase {

    private static final String PROJECT_ID = "tfgame-3ff2b";
    private static final String FIRESTORE_BASE_URL =
        "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents";

    public static String getProfileImageByUsername(String username) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/players.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();
            String json = responseBuilder.toString();

            // Buscar el campo con ese username como user_id
            int userIndex = json.indexOf("\"user_id\": { \"stringValue\": \"" + username + "\"");
            if (userIndex == -1) return null;

            // Buscar la imagen asociada
            int imageIndex = json.indexOf("\"image\": { \"stringValue\": \"", userIndex);
            if (imageIndex == -1) return null;

            int start = imageIndex + "\"image\": { \"stringValue\": \"".length();
            int end = json.indexOf("\"", start);

            if (end != -1) {
                return json.substring(start, end);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener imagen desde Firebase: " + e.getMessage());
        }

        return null;
    }

    public static String getUsernameByPlayerId(int playerId) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/players.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();
            String json = responseBuilder.toString();

            // Buscar player con ID correspondiente
            String searchKey = "\"id\": { \"integerValue\": \"" + playerId + "\"";
            int idIndex = json.indexOf(searchKey);
            if (idIndex == -1) return null;

            int userStart = json.indexOf("\"user_id\": { \"stringValue\": \"", idIndex);
            if (userStart == -1) return null;

            int start = userStart + "\"user_id\": { \"stringValue\": \"".length();
            int end = json.indexOf("\"", start);

            if (end != -1) {
                return json.substring(start, end);
            }

        } catch (Exception e) {
            System.err.println("Error obteniendo username desde Firebase: " + e.getMessage());
        }

        return null;
    }

    public static PlayerManager.PlayerData getPlayerDataByUsernameFire(String username) {
        try {
            // Paso 1: Obtener el user_id desde Firebase (/users/{username})
            String userUrl = FIRESTORE_BASE_URL + "/users/" + username;
            HttpURLConnection userConn = (HttpURLConnection) new URL(userUrl).openConnection();
            userConn.setRequestMethod("GET");

            if (userConn.getResponseCode() != 200) {
                System.out.println("Firebase => Usuario no encontrado");
                return null;
            }

            StringBuilder userResponse = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(userConn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    userResponse.append(line);
                }
            }

            String userJson = userResponse.toString();
            int userId = extraerCampoInt(userJson, "id");

            if (userId == -1) {
                System.out.println("Firebase => ID no encontrado en el usuario");
                return null;
            }

            // Paso 2: Obtener el player con ese ID
            String playerPath = FIRESTORE_BASE_URL + "/players/" + userId;
            HttpURLConnection conn = (HttpURLConnection) new URL(playerPath).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.out.println("Firebase => No se encontró el player con user_id: " + userId);
                return null;
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            String json = response.toString();

            // Extraer campos del documento del player
            int id = userId; // ya es el ID del documento
            String firstname = extraerCampoString(json, "firstname");
            String lastname = extraerCampoString(json, "lastname");
            String email = extraerCampoString(json, "email");
            String image = extraerCampoString(json, "image");

            return new PlayerManager.PlayerData(id, firstname, lastname, email, image);

        } catch (Exception e) {
            System.err.println("❌ Error al obtener player por user_id en Firebase: " + e.getMessage());
            return null;
        }
    }


    public static List<PlayerManager.PlayerData> getAllPlayersFromFirebase() {
        List<PlayerManager.PlayerData> players = new ArrayList<>();

        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/players.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();
            String json = responseBuilder.toString();

            // Dividir por cada entrada de jugador
            String[] entries = json.split("\\},\\s*\\{");  // Separar por objetos

            for (String entry : entries) {
                int id = extractInt(entry, "\"id\": { \"integerValue\": \"");
                String firstname = extractString(entry, "\"firstname\": { \"stringValue\": \"");
                String lastname = extractString(entry, "\"lastname\": { \"stringValue\": \"");
                String email = extractString(entry, "\"email\": { \"stringValue\": \"");
                String image = extractString(entry, "\"image\": { \"stringValue\": \"");

                if (firstname != null && lastname != null && email != null) {
                    players.add(new PlayerManager.PlayerData(id, firstname, lastname, email, image));
                }
            }

            return players;

        } catch (Exception e) {
            System.err.println("Error al obtener todos los jugadores desde Firebase: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    private static String extractString(String source, String key) {
        int start = source.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int end = source.indexOf("\"", start);
        if (end == -1) return null;
        return source.substring(start, end);
    }

    private static int extractInt(String source, String key) {
        try {
            int start = source.indexOf(key);
            if (start == -1) return -1;
            start += key.length();
            int end = source.indexOf("\"", start);
            if (end == -1) return -1;
            return Integer.parseInt(source.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private static String extraerCampoString(String json, String campo) {
        try {
            int index = json.indexOf("\"" + campo + "\":");
            if (index == -1) return null;

            int start = json.indexOf("\"stringValue\"", index);
            if (start == -1) return null;

            int quote1 = json.indexOf("\"", start + 14);
            int quote2 = json.indexOf("\"", quote1 + 1);
            return json.substring(quote1 + 1, quote2);

        } catch (Exception e) {
            return null;
        }
    }

    private static int extraerCampoInt(String json, String campo) {
        try {
            int index = json.indexOf("\"" + campo + "\":");
            if (index == -1) return -1;

            int start = json.indexOf("\"integerValue\"", index);
            if (start == -1) return -1;

            int quote1 = json.indexOf("\"", start + 15);
            int quote2 = json.indexOf("\"", quote1 + 1);
            return Integer.parseInt(json.substring(quote1 + 1, quote2));

        } catch (Exception e) {
            return -1;
        }
    }




}
