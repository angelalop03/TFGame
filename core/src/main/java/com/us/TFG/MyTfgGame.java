package com.us.TFG;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.us.TFG.screens.LogoScreen;
import com.us.TFG.screens.Menu;
import com.us.TFG.screens.OpenScreen;
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

        this.setScreen(new LogoScreen());
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
