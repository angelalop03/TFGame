package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.*;
import com.us.TFG.model.*;
import com.us.TFG.utils.*;

import java.util.List;

public class PlayerHistorialDetailScreen implements Screen {
    private String username;
    private int gameId;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private Stage stage;
    private SpriteBatch batch;
    private static final int DECISIONS_PER_PAGE = 3;
    private int currentPage = 0;
    private List<PlayerDecisionState> decisions;

    public PlayerHistorialDetailScreen(String username, int gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.FONDOBOOK2);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();
        topBar = new TopBar(username, stage, font);

        int playerId = PlayerManager.getPlayerData(username).getId();
        decisions = PlayerDecisionStateManager.getPositivePointDecisions(playerId, gameId);

        renderPage();
    }

    private void renderPage() {
        stage.clear();

        // TopBar y flecha de retroceso
        topBar = new TopBar(username, stage, font);

        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerHistorialScreen(username));
            }
        });
        stage.addActor(backArrowButton);

        Table decisionTable = new Table();
        decisionTable.setFillParent(true);
        decisionTable.top().padTop(150);

        if (decisions == null || decisions.isEmpty()) {
            Label noDataLabel = new Label("No hay decisiones disponibles", new Label.LabelStyle(font, Color.RED));
            decisionTable.add(noDataLabel).pad(10).row();
        } else {
            int start = currentPage * DECISIONS_PER_PAGE;
            int end = Math.min(start + DECISIONS_PER_PAGE, decisions.size());

            for (int i = start; i < end; i++) {
                PlayerDecisionState decisionState = decisions.get(i);

                try {
                    // Obtener toda la cadena de datos
                    Option option = OptionManager.getOptionById(decisionState.getOptionId());
                    Decision decision = DecisionManager.getDecisionById(option.getDecisionId());
                    Scene scene = SceneManager.getSceneById(decision.getSceneId());
                    Chapter chapter = ChapterManager.getChapterById(scene.getChapterId());

                    String chapterTitle = chapter.getName();
                    String optionText = option.getText();

                    String info = "Capítulo: " + chapterTitle + "\n" +
                        "Opción elegida: " + optionText;

                    Label label = new Label(info, new Label.LabelStyle(font, Color.WHITE));
                    label.setWrap(true);
                    decisionTable.add(label).width(700).pad(10).padLeft(200).row();

                } catch (Exception e) {
                    Label errorLabel = new Label("Error al obtener información de la decisión.", new Label.LabelStyle(font, Color.RED));
                    decisionTable.add(errorLabel).pad(10).row();
                    e.printStackTrace();
                }
            }

            // Detectar clic para cambiar de página si hay más de una
            if (decisions.size() > DECISIONS_PER_PAGE) {
                stage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if ((currentPage + 1) * DECISIONS_PER_PAGE < decisions.size()) {
                            currentPage++;
                        } else {
                            currentPage = 0;
                        }
                        renderPage();
                    }
                });
            }
        }

        stage.addActor(decisionTable);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        fondo.draw();
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
