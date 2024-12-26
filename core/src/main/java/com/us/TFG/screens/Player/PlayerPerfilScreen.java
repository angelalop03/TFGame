package com.us.TFG.screens.Player;

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
import com.us.TFG.manager.PlayerManager.PlayerData;
import com.us.TFG.screens.LogOutScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class PlayerPerfilScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private String username;
    private PlayerData playerData;

    public PlayerPerfilScreen(String username) {
        this.username = username;
        this.playerData = PlayerManager.getPlayerData(username);

        if (playerData == null) {
            throw new RuntimeException("No se pudo cargar la información del jugador.");
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        batch = Render.batch;

        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);

        // Generar fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Crear estilos
        skin.add("Caveat", font);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.DARK_GRAY, 0.4f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.GRAY, 0.4f)));
        buttonStyle.font = font;

        skin.add("default", buttonStyle);

        // Crear y agregar la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear el cuadro gris translúcido
        Pixmap pixmap = new Pixmap(700, 650, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f)); // Gris claro con transparencia
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - 700) / 2, 40);
        stage.addActor(background);

        // Crear la tabla para mostrar la información del jugador
        Table table = new Table();
        table.setFillParent(true);

        Texture profileImage = new Texture(Gdx.files.internal("assets/photos/players/" + playerData.getImage()));
        Image playerImage = new Image(profileImage);
        table.add(playerImage).size(200, 200).padBottom(20);
        table.row();

        table.add(new Label("Nombre: " + playerData.getFirstname() + " " + playerData.getLastname(), skin)).padBottom(10);
        table.row();
        table.add(new Label("Email: " + playerData.getEmail(), skin)).padBottom(10);
        table.row();
        table.add(new Label("Puntos Mental: " + playerData.getPointsM(), skin)).padBottom(10);
        table.row();
        table.add(new Label("Puntos Relación: " + playerData.getPointsR(), skin)).padBottom(10);
        table.row();

        // Botón para editar perfil
        TextButton editProfileButton = new TextButton("Editar Perfil", buttonStyle);
        table.add(editProfileButton).size(200, 50).padBottom(10);
        table.row();

        // Botón para cerrar sesión
        TextButton logOutButton = new TextButton("Cerrar Sesión", buttonStyle);
        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new LogOutScreen(username));
            }
        });
        table.add(logOutButton).size(200, 50).padBottom(10);

        // Ajustar la posición de la tabla sobre el cuadro gris
        table.setPosition(0, -50);
        stage.addActor(table);
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
