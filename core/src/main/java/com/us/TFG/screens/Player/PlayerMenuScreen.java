package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.screens.Player.Game.ChargeGameScreen;
import com.us.TFG.screens.Player.Game.CreateGameScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class PlayerMenuScreen implements Screen {

    private Stage stage;
    private SpriteBatch b;
    private Skin skin;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private String username;

    //Para cuando haga los users
     public PlayerMenuScreen(String username) {
        this.username = username;
    }

    @Override
    public void show() {

        //Cargar el fondo
        fondo = new Imagen(Resources.MENU);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        b = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear estilo para los botones usando Pixmap
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.LIGHT_GRAY, 0.4f)));
        style.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.GRAY, 0.4f)));
        style.over = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.DARK_GRAY, 0.4f)));
        style.fontColor = Color.WHITE;
        style.font = font;

        // Crear y agregar la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear los botones de texto
        TextButton newGameButton = new TextButton("Nueva Partida", style);
        TextButton loadGameButton = new TextButton("Cargar Partida", style);
        TextButton historialButton = new TextButton("Historial", style);
        TextButton newsForumButton = new TextButton("Foro de Noticias", style);
        TextButton instructionsButton = new TextButton("Instrucciones", style);

        // Ajustar el tamaño de los botones
        newGameButton.setSize(300, 75);
        loadGameButton.setSize(300, 75);
        historialButton.setSize(300,75);
        newsForumButton.setSize(300, 75);
        newsForumButton.setSize(300, 75);
        instructionsButton.setSize(300, 75);

        // Posicionar los botones
        newGameButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 420);
        loadGameButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 340);
        historialButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 260);
        newsForumButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 180);
        instructionsButton.setPosition(Gdx.graphics.getWidth() / 2f - 175, 100);

        //Boton para Crear Partida
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new CreateGameScreen(username));
            }
        });

        //Boton para Cargar Partida
        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ChargeGameScreen(username));
            }
        });

        //Boton para Foro
        newsForumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ForumScreen(username));
            }
        });

        // Añadir los botones al stage
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
        font.dispose();
    }

    // Boton trasnparente
    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);

        // Establecer el color con la transparencia
        Color transparentColor = new Color(color.r, color.g, color.b, transparency);
        pixmap.setColor(transparentColor);

        pixmap.fillRectangle(0, 20, width, height - 2 * 20);  // Centro vertical


        return pixmap;
    }


}
