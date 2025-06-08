package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.NewsManager;
import com.us.TFG.utils.*;

public class NewsDetailScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private Imagen fondo;
    private NewsManager.NewsData news;
    private Skin skin;
    private TopBar topBar;
    private String username;

    public NewsDetailScreen(String username, NewsManager.NewsData news) {
        this.username = username;
        this.news = news;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.NEWS);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UIStyle.getSkin();

        topBar = new TopBar(username, stage, UIStyle.getFont());


        // Crear la flecha de retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100); // Tamaño de la flecha
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150); // Posición en la esquina superior izquierda
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ForumScreen(username)); // Volver a ForumScreen
            }
        });

        // Crear contenedor principal con scroll
        Table contentTable = new Table();
        contentTable.top();
        contentTable.setBackground(UIStyle.getBackgroundDrawable());


        // Mostrar título de la noticia
        Label newsTitleLabel = new Label(news.getName(), UIStyle.getTitleLabelStyle());
        newsTitleLabel.setWrap(true);
        newsTitleLabel.setAlignment(1);
        contentTable.add(newsTitleLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(10).row();

        // Mostrar contenido de la noticia
        Label contentLabel = new Label(news.getText(), UIStyle.getLabelStyle());
        contentLabel.setWrap(true);
        contentTable.add(contentLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(20).row();

        // ScrollPane para desplazamiento
        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        // Crear el contenedor principal y agregar el ScrollPane
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(scrollPane).expand().fill().padTop(60);

        // Añadir elementos al Stage
        stage.addActor(mainTable);
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
}
