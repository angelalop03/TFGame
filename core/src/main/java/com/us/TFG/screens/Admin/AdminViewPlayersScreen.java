package com.us.TFG.screens.Admin;

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
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.screens.Player.PlayerPerfilScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

import java.util.List;

public class AdminViewPlayersScreen implements Screen {

    private String adminUsername;
    private Imagen fondo;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private TopBar topBar;

    public AdminViewPlayersScreen(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
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

        // Crear estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Crear y agregar la barra superior
        topBar = new TopBar(adminUsername, stage, font);

        // Crear el fondo gris translúcido para la tabla
        Pixmap pixmap = new Pixmap(1000, 500, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - 1000) / 2f, 100);
        stage.addActor(background);

        // Crear encabezados para la tabla
        Table headerTable = new Table();
        headerTable.setPosition((Gdx.graphics.getWidth() ) / 2f, 620);
        headerTable.add(new Label("Nombre", labelStyle)).width(300).pad(10).center();
        headerTable.add(new Label("Correo", labelStyle)).width(300).pad(10).center();
        headerTable.add(new Label("", labelStyle)).width(200).pad(10).center(); // Espacio para botones
        headerTable.row();

        // Crear la tabla de jugadores
        Table playersTable = createPlayersTable(labelStyle);

        // Crear el ScrollPane para los jugadores
        ScrollPane scrollPane = new ScrollPane(playersTable);
        scrollPane.setSize(1000, 500);
        scrollPane.setPosition((Gdx.graphics.getWidth() - 1000) / 2f, 100);
        scrollPane.setScrollingDisabled(true, false); // Solo permitir desplazamiento vertical
        scrollPane.setFadeScrollBars(true); // Mostrar siempre las barras de desplazamiento

        // Añadir encabezados y ScrollPane al stage
        stage.addActor(headerTable);
        stage.addActor(scrollPane);
    }

    private Table createPlayersTable(Label.LabelStyle labelStyle) {
        Table table = new Table();
        table.top(); // Alineación inicial de la tabla

        // Estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.DARK_GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.GRAY, 0.7f)));

        // Obtener la lista de jugadores desde PlayerManager
        List<PlayerManager.PlayerData> players = PlayerManager.getAllPlayers();
        for (PlayerManager.PlayerData player : players) {
            table.add(new Label(player.getFirstname() + " " + player.getLastname(), labelStyle)).width(300).pad(10).center();
            table.add(new Label(player.getEmail(), labelStyle)).width(300).pad(10).center();

            // Botón de Detalles
            TextButton detailsButton = new TextButton("Editar", buttonStyle);
            detailsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Render.app.setScreen(new PlayerPerfilScreen(PlayerManager.getUsername(player.getId()))); // Cambia a la pantalla del perfil
                }
            });
            table.add(detailsButton).width(150).pad(10).center();

            // Botón de Historial
            TextButton historyButton = new TextButton("Historial", buttonStyle);
            historyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Render.app.setScreen(new HistorialScreen(player.getUsername())); // Cambia a la pantalla del historial
                }
            });
            table.add(historyButton).width(150).pad(10).center();
            table.row();
        }

        return table;
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
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
}
