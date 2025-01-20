package com.us.TFG.screens.Player.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.manager.*;
import com.us.TFG.model.Decision;
import com.us.TFG.model.Option;
import com.us.TFG.model.Scene;
import com.us.TFG.utils.Render;

import java.util.ArrayList;
import java.util.List;

public class SceneGameScreen implements Screen {

    private Stage stage;
    private Scene currentScene;
    private Decision currentDecision;
    private int chapterId;
    private int gameId;
    private String username;
    private BitmapFont font;
    private Image background;
    private Label dialogueLabel;
    private Table optionsTable;

    public SceneGameScreen(int gameId, int chapterId, String username) {
        this.gameId = gameId;
        this.chapterId = chapterId;
        this.username = username;
        this.currentScene = SceneManager.getFirstSceneOfChapter(gameId, chapterId); // Obtener la primera escena
        if (currentScene != null) {
            this.currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        if (currentScene == null) {
            System.err.println("No hay escenas disponibles para el capítulo: " + chapterId);
            Render.app.setScreen(new ChapterCatalogScreen(username, gameId));
            return;
        }

        // Configurar fondo de la escena
        Texture bgTexture = new Texture(Gdx.files.internal("assets/photos/Scenes/" + currentScene.getBackgroundImage()));
        background = new Image(new TextureRegionDrawable(bgTexture));
        background.setFillParent(true);
        stage.addActor(background);

        // Configurar fuente y diálogo inicial
        font = new BitmapFont();
        dialogueLabel = new Label(currentDecision != null ? currentDecision.getDialogue() : "No hay decisiones.", new Label.LabelStyle(font, null));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWidth(Gdx.graphics.getWidth() - 100);
        dialogueLabel.setPosition(50, Gdx.graphics.getHeight() - 150);
        stage.addActor(dialogueLabel);

        // Configurar tabla para opciones
        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);


    }






    @Override
    public void render(float delta) {
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
        font.dispose();

    }

}
