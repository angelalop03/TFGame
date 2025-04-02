package com.us.TFG;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.us.TFG.screens.Admin.AdminMenuScreen;
import com.us.TFG.screens.General.LogoScreen;
import com.us.TFG.screens.Player.PlayerMenuScreen;
import com.us.TFG.utils.DatabaseManager;
import com.us.TFG.utils.RegistrationSystem;
import com.us.TFG.utils.Render;

public class MyTfgGame extends Game {

    private RegistrationSystem registrationSystem;
    public static DatabaseManager databaseManager; // Instancia global para la base de datos

    @Override
    public void create() {
        Render.app =this;
        Render.batch = new SpriteBatch();

        // Inicializar DatabaseManager solo una vez
        databaseManager = new DatabaseManager();

        //this.setScreen(new LogoScreen());
        //this.setScreen(new PlayerMenuScreen("Player1"));
        this.setScreen(new AdminMenuScreen("Admin1"));
    }

    @Override
    public void render() {
        super.render();

    }

    @Override
    public void dispose() {
        Render.batch.dispose();
        if (registrationSystem != null) {
            registrationSystem.close();
        }
    }
}
