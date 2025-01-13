package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

        // Añadir la barra superior al Stage
        stage.addActor(topBar.getTopBar());

        // Crear el contenedor principal
        Table mainTable = new Table();
        mainTable.setFillParent(true);


        // Crear el contenedor para las noticias
        Table newsTable = new Table();
        newsTable.top().padTop(100); // Asegurarse de que las noticias no estén demasiado pegadas al `TopBar`

        // Obtener las noticias desde NewsManager
        List<NewsManager.NewsData> newsList = NewsManager.getAllNews();

        // Crear un diseño vertical tipo feed con solo la imagen y el título
        for (NewsManager.NewsData news : newsList) {
            // Contenedor para cada noticia
            Table newsContainer = new Table();

            // Mostrar imagen de la noticia
            Texture newsImageTexture = new Texture(Gdx.files.internal("assets/photos/News/" + news.getImage()));
            Image newsImage = new Image(new TextureRegionDrawable(newsImageTexture));
            newsImage.setSize(1000, 300);

            // Mostrar título de la noticia
            Label newsTitleLabel = new Label(news.getName(), new Label.LabelStyle(font, Color.WHITE));
            newsTitleLabel.setWrap(true); // Habilitar ajuste automático
            newsTitleLabel.setAlignment(1); // Centrar el texto
            newsContainer.add(newsImage).center().padTop(5).size(1000, 300).row();
            newsContainer.add(newsTitleLabel).width(1000).center().pad(10).row(); // Establecer ancho fijo

            // Añadir listener para cambiar a la pantalla de detalle
            newsContainer.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Render.app.setScreen(new NewsDetailScreen(username, news));
                }
            });

            // Añadir el contenedor al Table principal
            newsTable.add(newsContainer).padTop(5).center().pad(10).row();
        }

        // Crear un ScrollPane para las noticias
        ScrollPane scrollPane = new ScrollPane(newsTable);
        scrollPane.setScrollingDisabled(true, false); // Permitir scroll solo vertical
        scrollPane.setFadeScrollBars(false);

        // Añadir el ScrollPane al contenedor principal debajo del TopBar
        mainTable.add(scrollPane).fill().expand();

        // Añadir el contenedor principal al stage
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
