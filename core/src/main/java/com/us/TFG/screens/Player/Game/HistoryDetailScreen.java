package com.us.TFG.screens.Player.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.us.TFG.manager.ChapterManager;
import com.us.TFG.manager.GameManager;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.Game;
import com.us.TFG.model.History;
import com.us.TFG.utils.*;

public class HistoryDetailScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private TextField gameNameField;
    private Label feedbackLabel;
    private String username;
    private History history;
    private final String PLACEHOLDER_TEXT = "Introduce un nombre";
    private ImageButton backArrowButton;
    private Table contentTable;

    public HistoryDetailScreen(String username, History history) {
        this.username = username;
        this.history = history;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.FONDODOORS);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();
        topBar = new TopBar(username, stage, font);

        // Retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new CreateGameScreen(username));
            }
        });

        // Tabla raíz
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Contenedor fijo
        contentTable = new Table();
        contentTable.pad(30);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1.0f, 0.41f, 0.71f, 0.6f));
        pixmap.fill();
        Texture backgroundTex = new Texture(pixmap);
        pixmap.dispose();

        contentTable.setBackground(new TextureRegionDrawable(backgroundTex));

        // Título
        Label historyTitle = new Label(history.getTitle(), UIStyle.getLabelStyle());

        // Descripción con scroll
        Label historyDescription = new Label(history.getDescription(), UIStyle.getLabelStyle());
        historyDescription.setWrap(true);

        Table descriptionTable = new Table();
        descriptionTable.add(historyDescription).expandX().left().top().pad(10);

        ScrollPane scrollPane = new ScrollPane(descriptionTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        // Campo de texto
        gameNameField = new TextField(PLACEHOLDER_TEXT, UIStyle.getTextFieldStyle());
        gameNameField.setMessageText(PLACEHOLDER_TEXT);
        gameNameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameNameField.getText().equals(PLACEHOLDER_TEXT)) {
                    gameNameField.setText("");
                }
            }
        });

        // Mensaje
        feedbackLabel = new Label("", UIStyle.getLabelStyle());
        feedbackLabel.setWrap(true);

        // Botón
        TextButton saveButton = new TextButton("Crear Juego", UIStyle.getButtonStyle());
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String gameName = gameNameField.getText().trim();
                if (gameName.isEmpty() || gameName.equals(PLACEHOLDER_TEXT)) {
                    feedbackLabel.setText("Por favor, introduce un nombre para el juego.");
                    feedbackLabel.setColor(Color.RED);
                } else {
                    if (!GameManager.doesGameExist(gameName, PlayerManager.getPlayerData(username).getId())) {
                        Game game = GameManager.createGame(gameName, PlayerManager.getPlayerData(username).getId(), history.getId());
                        feedbackLabel.setText("Juego creado con éxito.");
                        feedbackLabel.setColor(Color.GREEN);
                        Gdx.app.postRunnable(() -> {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Chapter introChapter = ChapterManager.getIntroChapterForHistory(history.getId());
                            Render.app.setScreen(new SceneGameScreen(game.getId(), introChapter.getId(), username));
                        });
                    } else {
                        feedbackLabel.setText("Ya existe una partida con ese nombre: " + gameName);
                        feedbackLabel.setColor(Color.RED);
                    }
                }
            }
        });

        // Armar contenido
        contentTable.add(historyTitle).padBottom(20);
        contentTable.row();
        contentTable.add(scrollPane).height(150).width(600).padBottom(20);
        contentTable.row();
        contentTable.add(gameNameField).width(600).padBottom(15);
        contentTable.row();
        contentTable.add(saveButton).width(300).padTop(15);
        contentTable.row();
        contentTable.add(feedbackLabel).width(500).padTop(10);

        // Añadir con tamaño fijo
        root.add(contentTable).width(700).height(500).center();

        // Añadir flecha por encima
        stage.addActor(backArrowButton);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        fondo.draw();
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        topBar.updateSize();

        if (backArrowButton != null) {
            backArrowButton.setPosition(10, height - backArrowButton.getHeight() - 50);
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
