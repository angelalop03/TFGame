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
import com.us.TFG.manager.ChapterManager;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.Scene;
import com.us.TFG.screens.Player.PlayerMenuScreen;
import com.us.TFG.utils.*;

import java.util.List;

public class ChapterCatalogScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private Skin skin;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private String username;
    private int gameId;
    private int currentPage = 0;
    private static final int CHAPTERS_PER_PAGE = 5;
    private static final int MAX_UNLOCKED_CHAPTERS = 10;
    private List<Chapter> chapters;

    public ChapterCatalogScreen(String username, int gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.FONDOBOOK);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();
        topBar = new TopBar(username, stage, font);

        chapters = ChapterManager.getChaptersForPlayerandGame(PlayerManager.getPlayerData(username).getId(), gameId);

        chapters.removeIf(chapter -> "0".equals(chapter.getChapterNumber()));
        renderPage();

    }

    private void renderPage() {
        stage.clear();

        // Crear la flecha de retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ChargeGameScreen(username));
            }
        });

        // Crear flechas
        Texture flechaDTexture = new Texture(Gdx.files.internal("assets/photos/flechaD.png"));
        ImageButton flechaDButton = new ImageButton(new TextureRegionDrawable(flechaDTexture));
        flechaDButton.setSize(100, 100);
        flechaDButton.setPosition(1150, Gdx.graphics.getHeight() - 700);

        // Crear flechas
        Texture flechaTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton flechaButton = new ImageButton(new TextureRegionDrawable(flechaTexture));
        flechaButton.setSize(100, 100);
        flechaButton.setPosition(20, Gdx.graphics.getHeight() - 700);


        // Crear tabla de capítulos
        Table chapterTable = new Table();
        chapterTable.setFillParent(true);
        chapterTable.padTop(30).padLeft(150).padRight(150);

        // Estilos
        TextButton.TextButtonStyle unlockedButtonStyle = UIStyle.getCustomButtonStyle(300, 150, Color.PURPLE);
        TextButton.TextButtonStyle lockedButtonStyle = UIStyle.getCustomButtonStyle(300, 150, new Color(0.5f, 0.5f, 0.5f, 0.5f));
        lockedButtonStyle.fontColor = Color.DARK_GRAY;

        // Renderizar capítulos
        int start = currentPage * CHAPTERS_PER_PAGE;
        int end = Math.min(start + CHAPTERS_PER_PAGE, MAX_UNLOCKED_CHAPTERS);

        for (int i = start; i < end; i++) {
            Table chapterRow = new Table();

            if (i < chapters.size()) {
                Chapter chapter = chapters.get(i);

                // Imagen del capítulo desbloqueado
                Texture chapterImageTexture = new Texture(Gdx.files.internal("assets/photos/Chapters/"+chapter.getImagePath()));
                Image chapterImage = new Image(new TextureRegionDrawable(chapterImageTexture));
                chapterImage.setSize(100, 100);
                chapterRow.add(chapterImage).size(100, 100).padRight(20);

                // Botón del capítulo desbloqueado
                TextButton chapterButton = new TextButton(chapter.getChapterNumber() + "\n" + chapter.getName(), unlockedButtonStyle);
                chapterButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Capítulo seleccionado: " + chapter.getName());
                        Render.app.setScreen(new SceneGameScreen(gameId,chapter.getId(),username));

                    }
                });

                chapterRow.add(chapterButton).width(300).height(100);

            } else {
                // Imagen del capítulo bloqueado
                Texture defaultImageTexture = new Texture(Gdx.files.internal("assets/photos/Chapters/Default_Chapter.png"));
                Image defaultImage = new Image(new TextureRegionDrawable(defaultImageTexture));
                defaultImage.setSize(100, 100);
                chapterRow.add(defaultImage).size(100, 100).padRight(20);

                // Botón del capítulo bloqueado
                TextButton chapterButton = new TextButton("No desbloqueado", lockedButtonStyle);
                chapterRow.add(chapterButton).width(300).height(100);
            }

            chapterTable.add(chapterRow).padBottom(20);

            // Nueva fila después de dos columnas
            if ((i - start + 1) % 2 == 0) {
                chapterTable.row();
            }
        }

        // Detectar clic en cualquier parte de la pantalla para avanzar página
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((currentPage + 1) * CHAPTERS_PER_PAGE < MAX_UNLOCKED_CHAPTERS) {
                    currentPage++;
                } else {
                    currentPage = 0; // Reiniciar a la primera página si se alcanza el final
                }
                renderPage();
            }
        });

        stage.addActor(chapterTable);
        stage.addActor(backArrowButton);
        stage.addActor(flechaDButton);
        stage.addActor(flechaButton);


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
