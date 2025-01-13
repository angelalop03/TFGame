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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.NewsManager;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

import java.util.List;

public class ForumScreen implements Screen {

    private Stage stage;
    private SpriteBatch b;
    private Skin skin;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private String username;

    public ForumScreen(String username) {
        this.username = username;
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

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear el contenedor para mostrar las noticias
        Table newsTable = new Table();
        newsTable.setFillParent(true);
        newsTable.top().padTop(100);


        // Obtener las noticias desde NewsManager
        List<NewsManager.NewsData> newsList = NewsManager.getAllNews();

        // Crear un diseño vertical tipo feed
        for (NewsManager.NewsData news : newsList) {
            // Mostrar imagen de la noticia
            Texture newsImageTexture = new Texture(Gdx.files.internal(news.getImage()));
            Image newsImage = new Image(new TextureRegionDrawable(newsImageTexture));
            newsImage.setSize(300, 300); // Tamaño de la imagen
            newsTable.add(newsImage).center().pad(10).row();

            // Mostrar título de la noticia
            Label newsTitleLabel = new Label(news.getName(), new Label.LabelStyle(font, Color.WHITE));
            newsTitleLabel.setFontScale(1.2f);
            newsTable.add(newsTitleLabel).center().pad(5).row();

            // Mostrar contenido de la noticia
            Label newsContentLabel = new Label(news.getText(), new Label.LabelStyle(font, Color.LIGHT_GRAY));
            newsContentLabel.setWrap(true);
            newsTable.add(newsContentLabel).width(Gdx.graphics.getWidth() * 0.8f).pad(10).row();
        }

        // Crear un ScrollPane para hacer que el feed sea desplazable
        ScrollPane scrollPane = new ScrollPane(newsTable);
        scrollPane.setFillParent(true);

        // Añadir el ScrollPane al stage
        stage.addActor(scrollPane);
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
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        Color transparentColor = new Color(color.r, color.g, color.b, transparency);
        pixmap.setColor(transparentColor);
        pixmap.fillRectangle(0, 0, width, height);
        return pixmap;
    }
}
