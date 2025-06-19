package com.us.TFG.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.us.TFG.model.*;
import com.us.TFG.model.CharacterGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    private Connection connection;

    public DatabaseManager() {
        try {
            // Conectar a la base de datos SQLite
            String dbPath = System.getenv("APPDATA") + "/BellasHeart/game.db";
            File dbFile = new File(dbPath);
            boolean dbExists = dbFile.exists();

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Conexión a la base de datos establecida.");

            // Lista de clases que tienen anotaciones @Table
            List<Class<?>> tableClasses = Arrays.asList(
                User.class,
                Player.class,
                News.class,
                Game.class,
                Chapter.class,
                PlayerChapterState.class,
                Scene.class,
                Option.class,
                Decision.class,
                CharacterGame.class,
                PlayerDecisionState.class,
                History.class
            );


            if (!dbExists) {
                logger.info("Base de datos no existe. Se ejecuta el SQL de inicialización.");
                executeManualSQL("data.sql");
            } else {
                logger.info("Base de datos ya existente. No se ejecuta SQL de inicialización.");
            }

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Método para resetear todas las tablas
    private void resetTables(List<Class<?>> tableClasses) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Primero eliminamos todas las tablas correspondientes a las clases
            for (Class<?> clazz : tableClasses) {
                if (clazz.isAnnotationPresent(com.us.TFG.annotations.Table.class)) {
                    com.us.TFG.annotations.Table tableAnnotation = clazz.getAnnotation(com.us.TFG.annotations.Table.class);
                    String tableName = tableAnnotation.name();
                    String dropSQL = "DROP TABLE IF EXISTS " + tableName;
                    stmt.execute(dropSQL);
                    System.out.println("Tabla '" + tableName + "' eliminada.");
                }
            }

            // Luego recreamos las tablas
            for (Class<?> clazz : tableClasses) {
                TableManager.createTable(connection, clazz);  // Volver a crear la tabla
            }
        }
    }
    private void executeManualSQL(String path) {
        try {
            BufferedReader reader;

            if (Gdx.files != null) {
                // En modo juego
                FileHandle fileHandle = Gdx.files.internal(path);
                reader = new BufferedReader(new InputStreamReader(fileHandle.read()));
            } else {
                // En modo test
                File file = new File("../assets/" + path);
                if (!file.exists()) {
                    System.err.println("Archivo SQL no encontrado: core/assets/" + path);
                    return;
                }
                reader = new BufferedReader(new FileReader(file));
            }


            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("--") || line.trim().isEmpty()) continue;

                sql.append(line);
                if (line.trim().endsWith(";")) {
                    try (PreparedStatement pstmt = connection.prepareStatement(sql.toString().trim())) {
                        pstmt.executeUpdate();
                        sql.setLength(0);
                    } catch (SQLException e) {
                        System.err.println("Error en la sentencia SQL: " + sql);
                        e.printStackTrace();
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error al ejecutar el archivo SQL manual: " + e.getMessage());
            e.printStackTrace();
        }
    }






    // Método para devolver la conexión
    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public void connect(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            System.out.println("Conectado a: " + url);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar a la base de datos: " + url, e);
        }
    }



}
