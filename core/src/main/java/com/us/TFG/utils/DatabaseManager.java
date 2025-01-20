package com.us.TFG.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.us.TFG.model.*;
import com.us.TFG.model.Character;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager() {
        try {
            // Conectar a la base de datos SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
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
                Character.class,
                PlayerDecisionState.class
            );

            // Resetear todas las tablas al iniciar el juego
            resetTables(tableClasses);


            // Ejecutar el archivo SQL manual
            executeManualSQL("data.sql");

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
    // Método para ejecutar el archivo SQL manual
    private void executeManualSQL(String path) {
        try {
            // Obtener el archivo SQL usando Gdx.files.internal o desde el sistema de archivos
            FileHandle fileHandle = Gdx.files.internal(path);  // Si estás usando LibGDX
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()));

            StringBuilder sql = new StringBuilder();
            String line;

            // Leer línea por línea el archivo SQL
            while ((line = reader.readLine()) != null) {
                // Ignorar los comentarios y líneas vacías
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }

                sql.append(line);
                // Ejecutar cuando se encuentre un punto y coma ';'
                if (line.trim().endsWith(";")) {
                    try (PreparedStatement pstmt = connection.prepareStatement(sql.toString().trim())) {
                        pstmt.executeUpdate();  // Ejecutar la sentencia SQL
                        sql.setLength(0);  // Limpiar el buffer después de ejecutar la sentencia
                    } catch (SQLException e) {
                        System.err.println("Error en la sentencia SQL: " + sql);
                        e.printStackTrace();  // Mostrar detalles del error
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error al ejecutar el archivo SQL manual: " + e.getMessage());
            e.printStackTrace();  // Mostrar detalles del error
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


}
