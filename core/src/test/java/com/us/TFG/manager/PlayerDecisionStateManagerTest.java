package com.us.TFG.manager;

import com.us.TFG.MyTfgGame;
import com.us.TFG.model.PlayerDecisionState;
import com.us.TFG.utils.DatabaseManager;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerDecisionStateManagerTest {

    private static final String ORIGINAL_DB_PATH = "../game.db";
    private static final String TEST_DB_PATH = "game_test.db";

    @Before
    public void setup() {
        try {
            File original = new File(ORIGINAL_DB_PATH);
            File testCopy = new File(TEST_DB_PATH);

            if (testCopy.exists()) testCopy.delete();
            Files.copy(original.toPath(), testCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);

            MyTfgGame.databaseManager = new DatabaseManager();
            MyTfgGame.databaseManager.connect("jdbc:sqlite:" + TEST_DB_PATH);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo preparar la base de datos para test", e);
        }
    }

    @After
    public void cleanup() {
        try {
            if (MyTfgGame.databaseManager != null) {
                MyTfgGame.databaseManager.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new File(TEST_DB_PATH).delete();
    }


    @Test
    public void testGetPositivePointDecisionsReturnsEmptyWhenNone() {
        int playerId = 999; // un ID que no tenga datos
        int gameId = 999;

        List<PlayerDecisionState> results = PlayerDecisionStateManager.getPositivePointDecisions(playerId, gameId);
        assertNotNull("Debe devolver una lista, incluso si está vacía", results);
        assertEquals("No debe haber decisiones positivas para un jugador inexistente", 0, results.size());
    }
}
