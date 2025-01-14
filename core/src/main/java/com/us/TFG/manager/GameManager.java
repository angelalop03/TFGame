package com.us.TFG.manager;

import com.us.TFG.model.*;

import java.util.Date;

public class GameManager {

    // Crear una nueva partida
    public static Game createGame(String name, int playerId) {
        Game game = new Game(name, playerId);
        game.setStartDate(new Date());
        game.setLastActivityDate(new Date());
        game.setState(GameState.IN_PROGRESS);
        game.setTotalScore(0);

        // Simulación de persistencia (debería insertarse en la base de datos)
        System.out.println("Juego creado: " + game);

        return game;
    }

    // Avanzar a la siguiente escena
    public static void advanceScene(Game game) {
        int currentSceneId = game.getCurrentSceneId();
        int nextSceneId = findNextSceneId(currentSceneId);

        if (nextSceneId != -1) {
            game.setCurrentSceneId(nextSceneId);
            game.setLastActivityDate(new Date());
            System.out.println("Avanzado a la escena: " + nextSceneId);
        } else {
            System.out.println("No hay más escenas en el capítulo actual. Intentando avanzar al siguiente capítulo...");
            advanceChapter(game);
        }
    }

    // Avanzar al siguiente capítulo
    public static void advanceChapter(Game game) {
        int currentChapterId = game.getCurrentChapterId();
        int nextChapterId = findNextChapterId(currentChapterId);

        if (nextChapterId != -1) {
            game.setCurrentChapterId(nextChapterId);
            int firstSceneId = findFirstSceneIdInChapter(nextChapterId);
            if (firstSceneId != -1) {
                game.setCurrentSceneId(firstSceneId);
            }
            game.setLastActivityDate(new Date());
            System.out.println("Avanzado al capítulo: " + nextChapterId);
        } else {
            System.out.println("No hay más capítulos. Terminando el juego...");
            completeGame(game);
        }
    }

    // Guardar progreso
    public static void saveProgress(Game game) {
        // Simulación de persistencia (aquí se debería guardar en la base de datos)
        System.out.println("Progreso guardado para el jugador con ID: " + game.getPlayerId());
        System.out.println("Capítulo actual ID: " + game.getCurrentChapterId());
        System.out.println("Escena actual ID: " + game.getCurrentSceneId());
    }

    // Reanudar partida
    public static void resumeGame(Game game) {
        if (game.getState() == GameState.PAUSED) {
            game.setState(GameState.IN_PROGRESS);
            game.setLastActivityDate(new Date());
            System.out.println("Partida reanudada para el jugador con ID: " + game.getPlayerId());
        } else {
            System.out.println("La partida no está pausada y no puede ser reanudada.");
        }
    }

    // Terminar partida
    public static void completeGame(Game game) {
        game.setState(GameState.COMPLETED);
        game.setLastActivityDate(new Date());
        System.out.println("Juego completado: " + game.getName());
        System.out.println("Jugador con ID: " + game.getPlayerId());
    }

    // Métodos auxiliares para buscar IDs (simulación)
    private static int findNextSceneId(int currentSceneId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID de la siguiente escena o -1 si no hay más
        System.out.println("Buscando la siguiente escena después de la escena ID: " + currentSceneId);
        return currentSceneId + 1; // Ejemplo
    }

    private static int findNextChapterId(int currentChapterId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID del siguiente capítulo o -1 si no hay más
        System.out.println("Buscando el siguiente capítulo después del capítulo ID: " + currentChapterId);
        return currentChapterId + 1; // Ejemplo
    }

    private static int findFirstSceneIdInChapter(int chapterId) {
        // Simulación de búsqueda en base de datos
        // Devuelve el ID de la primera escena del capítulo o -1 si no existe
        System.out.println("Buscando la primera escena en el capítulo ID: " + chapterId);
        return chapterId * 100 + 1; // Ejemplo
    }
}
