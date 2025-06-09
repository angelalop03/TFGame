package com.us.TFG.FireBase;

import com.us.TFG.manager.NewsManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewsFireBase {


        private static final String PROJECT_ID = "tfgame-3ff2b";
        private static final String FIRESTORE_BASE_URL =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents";



    public static List<NewsManager.NewsData> getAllNewsFromFirebase() {
        List<NewsManager.NewsData> newsList = new ArrayList<>();

        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/news");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();
            String json = jsonBuilder.toString();

            int currentIndex = 0;

            while ((currentIndex = json.indexOf("\"fields\"", currentIndex)) != -1) {
                int fieldsStart = json.indexOf("{", currentIndex);
                int fieldsEnd = json.indexOf("}", fieldsStart);
                if (fieldsStart == -1 || fieldsEnd == -1) break;

                String fieldsContent = json.substring(fieldsStart, json.indexOf("}", fieldsEnd + 1) + 1); // abarcar varios niveles

                int id = extractInt(fieldsContent, "\"id\":{\"integerValue\":\"");
                String name = extractString(fieldsContent, "\"name\":{\"stringValue\":\"");
                String text = extractString(fieldsContent, "\"text\":{\"stringValue\":\"");

                if (id != -1 && name != null && text != null) {
                    newsList.add(new NewsManager.NewsData(id, name, text));
                }

                currentIndex = fieldsEnd;
            }

        } catch (Exception e) {
            System.err.println("❌ Error leyendo Firebase: " + e.getMessage());
        }

        return newsList;
    }


    public static void deleteNewsFromFirebase(int newsId) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/news/" + newsId);  // sin ".json", igual que en deleteUserFromFirebase
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("🗑️ Noticia con ID " + newsId + " eliminada de Firebase.");
            } else if (responseCode == 404) {
                System.out.println("⚠️ Noticia con ID " + newsId + " no encontrada en Firebase.");
            } else {
                System.out.println("❌ Error al eliminar noticia de Firebase. Código: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar noticia de Firebase: " + e.getMessage());
        }
    }


    public static boolean createNewsInFirebase(int id, String title, String text) {
        try {
            URL url = new URL(FIRESTORE_BASE_URL + "/news/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String json = "{\n" +
                "  \"fields\": {\n" +
                "    \"id\": { \"integerValue\": \"" + id + "\" },\n" +
                "    \"name\": { \"stringValue\": \"" + title + "\" },\n" +
                "    \"text\": { \"stringValue\": \"" + text + "\" }\n" +
                "  }\n" +
                "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("📡 Firebase respuesta al crear noticia: " + responseCode);
            return responseCode == 200;

        } catch (Exception e) {
            System.err.println("❌ Error al crear noticia en Firebase: " + e.getMessage());
            return false;
        }
    }

    private static int extractInt(String input, String key) {
        int index = input.indexOf(key);
        if (index == -1) return -1;

        int start = index + key.length();
        int end = input.indexOf("\"", start);
        if (end == -1) return -1;

        try {
            return Integer.parseInt(input.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String extractString(String input, String key) {
        int index = input.indexOf(key);
        if (index == -1) return null;

        int start = index + key.length();
        int end = input.indexOf("\"", start);
        if (end == -1) return null;

        return input.substring(start, end);
    }




}


