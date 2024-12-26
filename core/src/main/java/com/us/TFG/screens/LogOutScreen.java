package com.us.TFG.screens;

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
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class LogOutScreen implements Screen {
    private String username;
    private Stage stage;
    private Skin skin;
    private Imagen fondo;
    private SpriteBatch batch;
    private TopBar topBar;
    private BitmapFont font;




    public LogOutScreen(String username){

        this.username =username;
    }
    @Override
    public void show() {

        //Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Generar fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();
        skin.add("Caveat", font);

        // Crear el cuadro grisáceo transparente
        Pixmap pixmap = new Pixmap(700, 250, Pixmap.Format.RGBA8888); // Tamaño del cuadro
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));  // Gris claro con 60% de transparencia
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, 240); // Centramos el fondo en la pantalla
        stage.addActor(background);

        // Crear estilo para Label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Crear y agregar la barra superior

        topBar = new TopBar(username, stage, font);

        // Crear estilo para los botones usando Pixmap
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.GRAY, 0.4f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.LIGHT_GRAY, 0.4f)));
        buttonStyle.over = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.DARK_GRAY, 0.4f)));
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.font = font;

        // Botón de cerrar sesión
        TextButton logOutButton = new TextButton("Cerrar Sesión", buttonStyle);
        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new OpenScreen());
            }
        });


        // Botón para volver a la pantalla anterior
        TextButton backButton = new TextButton("Cancelar", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PerfilScreen(username));
            }
        });


        // Crear una tabla para organizar los elementos en la pantalla
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("¿Estas seguro que quieres cerrar sesión? ", labelStyle)).colspan(2).padBottom(-9);
        table.row();
        table.add(logOutButton).width(200).padBottom(5);
        table.row();
        table.add(backButton).width(200).padBottom(5);
        table.row();

        stage.addActor(table);

    }

    @Override
    public void render(float v) {
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

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fillRectangle(0, 20, width, height - 40);
        return pixmap;
    }
}
