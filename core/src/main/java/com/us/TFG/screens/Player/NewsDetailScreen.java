package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.NewsManager;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class NewsDetailScreen implements Screen {

    private Stage stage;
    private SpriteBatch b;
    private Imagen fondo;
    private BitmapFont font;
    private BitmapFont titleFont;

    private NewsManager.NewsData news;
    private Skin skin;
    private TopBar topBar;
    private String username;



    public NewsDetailScreen(String username, NewsManager.NewsData news) {
        this.username= username;
        this.news = news;
    }

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        b = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);

        // Generar fuente para el título (más grande)
        FreeTypeFontGenerator.FreeTypeFontParameter titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParameter.size = 48; // Tamaño más grande para el título
        titleFont = generator.generateFont(titleParameter);

        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear el contenedor principal con scroll
        Table contentTable = new Table();
        contentTable.top(); // Asegurar que el contenido comienza desde arriba

        // Fondo grisáceo translúcido
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.8f));
        pixmap.fill();
        TextureRegionDrawable background = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        contentTable.setBackground(background);

        // Mostrar imagen de la noticia
        Texture newsImageTexture = new Texture(Gdx.files.internal("assets/photos/News/" + news.getImage()));
        Image newsImage = new Image(new TextureRegionDrawable(newsImageTexture));
        contentTable.add(newsImage).center().size(900, 300).padTop(25).row();

        // Mostrar título de la noticia
        Label newsTitleLabel = new Label(news.getName(), new Label.LabelStyle(titleFont, Color.DARK_GRAY));
        newsTitleLabel.setWrap(true); // Ajustar texto a múltiples líneas
        newsTitleLabel.setAlignment(1); // Centrar el texto
        contentTable.add(newsTitleLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(10).row();

        // Mostrar contenido de la noticia
        Label contentLabel = new Label(news.getText(), new Label.LabelStyle(font, Color.DARK_GRAY));
        contentLabel.setWrap(true); // Habilitar ajuste automático
        contentTable.add(contentLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(20).row();

        // ScrollPane para permitir desplazamiento
        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false); // Desactivar scroll horizontal
        scrollPane.setFadeScrollBars(false); // Barras de desplazamiento visibles

        // Crear el contenedor principal y agregar el ScrollPane
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(scrollPane).expand().fill().padTop(60); // Asegurar que no se solape con el TopBar

        // Añadir elementos al Stage
        stage.addActor(mainTable);
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
}
