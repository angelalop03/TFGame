package com.us.TFG.screens.Player.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.GameManager;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.screens.Player.PlayerMenuScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class CreateGameScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Skin skin;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private TextField gameNameField;
    private Label feedbackLabel;
    private String username;

    public CreateGameScreen(String username) {
        this.username = username;
    }

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.FONDODOORS);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear y agregar la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear la flecha de retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerMenuScreen(username));
            }
        });

        // Crear el contenedor principal centrado con el recuadro grisáceo
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Pixmap pixmap = new Pixmap(700, 500, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1.0f, 0.41f, 0.71f, 0.6f));
        pixmap.fill();
        TextureRegionDrawable background = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table contentTable = new Table();
        contentTable.setBackground(background);

        // Estilos
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(1000, 40, Color.GRAY, 0.8f)));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.LIGHT_GRAY, 0.4f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.GRAY, 0.4f)));
        buttonStyle.over = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.DARK_GRAY, 0.4f)));
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.font = font;

        // Campo para el nombre del juego
        gameNameField = new TextField("", textFieldStyle);

        // Etiqueta para mostrar mensajes de feedback
        feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(true);

        // Botón para guardar el juego
        TextButton saveButton = new TextButton("Crear Juego", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String gameName = gameNameField.getText().trim();
                if (gameName.isEmpty()) {
                    feedbackLabel.setText("Por favor, introduce un nombre para el juego.");
                    feedbackLabel.setColor(Color.RED);
                } else {
                    if (!GameManager.doesGameExist(gameName,PlayerManager.getPlayerData(username).getId())) {
                        GameManager.createGame(gameName, PlayerManager.getPlayerData(username).getId());
                        feedbackLabel.setText("Juego creado con éxito.");
                        feedbackLabel.setColor(Color.GREEN);

                        Gdx.app.postRunnable(() -> {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Que te lleve al catalogo de capitulos
                            Render.app.setScreen(new PlayerMenuScreen(username));
                        });
                    }else{
                        feedbackLabel.setText("Ya existe una partida con ese nombre" + gameName);
                        feedbackLabel.setColor(Color.RED);

                    }


                }
            }
        });

        // Agregar elementos al contentTable
        contentTable.add(new Label("Crear Partida", labelStyle)).colspan(2).padBottom(30).center();
        contentTable.row();
        contentTable.add(new Label("Nombre:", labelStyle)).left().padBottom(10);
        contentTable.add(gameNameField).width(600).padBottom(15).center();
        contentTable.row();
        contentTable.add(saveButton).colspan(2).padTop(20).center();
        contentTable.row();
        contentTable.add(feedbackLabel).colspan(2).width(600).padTop(10).center();

        // Agregar contentTable al mainTable
        mainTable.add(contentTable).center();
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
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
    }
}
