package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.screens.Player.Game.ChargeGameScreen;
import com.us.TFG.screens.Player.Game.CreateGameScreen;
import com.us.TFG.utils.*;

public class PlayerMenuScreen implements Screen {

    private Stage stage;
    private SpriteBatch b;
    private Skin skin;
    private Imagen fondo;
    private TopBar topBar;
    private String username;

    public PlayerMenuScreen(String username) {
        this.username = username;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.MENU);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        b = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = UIStyle.getSkin();

        topBar = new TopBar(username, stage, UIStyle.getFont());

        TextButton newGameButton = new TextButton("Nueva Partida", UIStyle.getButtonStyle());
        TextButton loadGameButton = new TextButton("Cargar Partida", UIStyle.getButtonStyle());
        TextButton historialButton = new TextButton("Historial", UIStyle.getButtonStyle());
        TextButton newsForumButton = new TextButton("Foro de Noticias", UIStyle.getButtonStyle());
        TextButton instructionsButton = new TextButton("Instrucciones", UIStyle.getButtonStyle());

        newGameButton.setSize(300, 75);
        loadGameButton.setSize(300, 75);
        historialButton.setSize(300, 75);
        newsForumButton.setSize(300, 75);
        instructionsButton.setSize(300, 75);

        newGameButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 420);
        loadGameButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 340);
        historialButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 260);
        newsForumButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 180);
        instructionsButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 100);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new CreateGameScreen(username));
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ChargeGameScreen(username));
            }
        });

        newsForumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ForumScreen(username));
            }
        });

        stage.addActor(newGameButton);
        stage.addActor(loadGameButton);
        stage.addActor(historialButton);
        stage.addActor(newsForumButton);
        stage.addActor(instructionsButton);
    }

    @Override
    public void render(float delta) {
        b.begin();
        fondo.draw();
        b.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
