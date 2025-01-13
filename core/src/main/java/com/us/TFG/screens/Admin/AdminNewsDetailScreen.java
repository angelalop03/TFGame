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
import com.us.TFG.manager.NewsManager;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;

public class AdminNewsDetailScreen implements Screen {

    private Stage stage;
    private SpriteBatch b;
    private Imagen fondo;
    private BitmapFont font;
    private BitmapFont titleFont;

    private NewsManager.NewsData news;
    private Skin skin;
    private TopBar topBar;
    private String username;

    public AdminNewsDetailScreen(String username, NewsManager.NewsData news) {
        this.username = username;
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

        FreeTypeFontGenerator.FreeTypeFontParameter titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParameter.size = 48;
        titleFont = generator.generateFont(titleParameter);

        generator.dispose();

        skin.add("Caveat", font);

        // Crear la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear la flecha de retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100); // Tamaño de la flecha
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150); // Posición en la esquina superior izquierda
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new AdminForumScreen(username)); // Volver a AdminForumScreen
            }
        });

        // Crear contenedor principal
        Table contentTable = new Table();
        contentTable.top();

        // Fondo grisáceo
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.8f));
        pixmap.fill();
        contentTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        // Botones "Editar" y "Eliminar"
        Table buttonTable = new Table();
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();

        TextButton editButton = new TextButton("Editar", buttonStyle);
        TextButton deleteButton = new TextButton("Eliminar", buttonStyle);

        // Listeners para los botones
        editButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción para editar la noticia
                //Render.app.setScreen(new AdminEditNewsScreen(username, news));
            }
        });

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showDeleteDialog();
            }
        });

        buttonTable.add(editButton).pad(10).width(200).height(50);
        buttonTable.add(deleteButton).pad(10).width(200).height(50);
        contentTable.add(buttonTable).padTop(20).center().row();

        // Mostrar imagen
        Texture newsImageTexture = new Texture(Gdx.files.internal("assets/photos/News/" + news.getImage()));
        Image newsImage = new Image(new TextureRegionDrawable(newsImageTexture));
        float maxImageWidth = 800;
        float aspectRatio = (float) newsImageTexture.getHeight() / newsImageTexture.getWidth();
        contentTable.add(newsImage).center().size(maxImageWidth, maxImageWidth * aspectRatio).padTop(5).row();

        // Mostrar título
        Label newsTitleLabel = new Label(news.getName(), new Label.LabelStyle(titleFont, Color.DARK_GRAY));
        newsTitleLabel.setWrap(true);
        newsTitleLabel.setAlignment(1);
        contentTable.add(newsTitleLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(10).row();

        // Mostrar contenido
        Label contentLabel = new Label(news.getText(), new Label.LabelStyle(font, Color.DARK_GRAY));
        contentLabel.setWrap(true);
        contentTable.add(contentLabel).width(Gdx.graphics.getWidth() * 0.9f).center().pad(20).row();

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(scrollPane).expand().fill().padTop(60);

        stage.addActor(mainTable);
        stage.addActor(backArrowButton);
    }

    private void showDeleteDialog() {
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 300, Color.RED, 1f)));

        Dialog deleteDialog = new Dialog("", dialogStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        deleteDialog.text(new Label("¿Estás seguro de eliminar esta noticia?", labelStyle)).pad(10);

        TextButton.TextButtonStyle buttonStyle = createButtonStyle();

        TextButton confirmButton = new TextButton("Confirmar", buttonStyle);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (NewsManager.deleteNews(news.getId())) {
                    deleteDialog.hide();
                    deleteDialog.remove();
                    Render.app.setScreen(new AdminForumScreen(username));
                } else {
                    deleteDialog.text(new Label("Error al eliminar la noticia.", labelStyle)).pad(10);
                }
            }
        });

        TextButton cancelButton = new TextButton("Cancelar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteDialog.hide();
                deleteDialog.remove();
            }
        });

        deleteDialog.getButtonTable().add(confirmButton).pad(10);
        deleteDialog.getButtonTable().add(cancelButton).pad(10);

        deleteDialog.show(stage);
    }

    private TextButton.TextButtonStyle createButtonStyle() {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        Pixmap buttonPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        buttonPixmap.setColor(Color.DARK_GRAY);
        buttonPixmap.fill();
        buttonStyle.up = new TextureRegionDrawable(new Texture(buttonPixmap));
        buttonPixmap.dispose();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        return buttonStyle;
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        Color transparentColor = new Color(color.r, color.g, color.b, transparency);
        pixmap.setColor(transparentColor);
        pixmap.fill();
        return pixmap;
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
