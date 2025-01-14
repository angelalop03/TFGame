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

public class AdminEditNewsScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Imagen fondo;
    private TopBar topBar;
    private TextArea titleField, contentField;
    private Label feedbackLabel;
    private SpriteBatch batch;
    private BitmapFont font;
    private NewsManager.NewsData news;
    private String username;

    public AdminEditNewsScreen(String username, NewsManager.NewsData news) {
        this.username = username;
        this.news = news;
    }

    @Override
    public void show() {
        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear y agregar la barra superior
        topBar = new TopBar(username, stage, font);

        // Crear flecha para volver
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new AdminNewsDetailScreen(username,news));
            }
        });

        // Estilos
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(1000, 40, Color.GRAY, 0.8f)));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.GRAY, 0.5f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.DARK_GRAY, 0.5f)));

        // Crear campos de texto y cargar datos actuales
        titleField = new TextArea(news.getName(), textFieldStyle); // Usa TextArea para manejar múltiples líneas
        titleField.setPrefRows(2); // Ajusta filas visibles

        contentField = new TextArea(news.getText(), textFieldStyle); // Usa TextArea para manejar texto largo
        contentField.setPrefRows(5);

        // Etiqueta para mostrar mensajes de feedback
        feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(true);

        // Botón para seleccionar imagen
        TextButton selectImageButton = new TextButton("Seleccionar Archivo", buttonStyle);

        selectImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción para seleccionar la imagen
                // Implementar la lógica de selección de archivo
            }
        });

        // Botón para guardar los cambios
        TextButton saveButton = new TextButton("Guardar", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Guardar cambios
                String newTitle = titleField.getText().trim();
                String newText = contentField.getText().trim();
                boolean success = NewsManager.updateNews(news.getId(), newTitle, newText, news.getImage());

                // Mostrar feedback
                if (success) {
                    feedbackLabel.setText("Noticia actualizada con éxito");
                    feedbackLabel.setColor(Color.GREEN);
                } else {
                    feedbackLabel.setText("Error al actualizar la noticia");
                    feedbackLabel.setColor(Color.RED);
                }
            }
        });

        // Crear tabla para los elementos
        Table table = new Table();
        table.setFillParent(true);
        table.row();
        table.add(new Label("Título", labelStyle)).left().padBottom(10);
        table.add(titleField).width(600).padBottom(15).center();
        table.row();
        table.add(new Label("Imagen", labelStyle)).left().padBottom(10);
        table.add(selectImageButton).width(300).padBottom(15).left();
        table.row();
        table.add(new Label("Contenido", labelStyle)).left().padBottom(10);
        table.add(contentField).width(600).height(300).padBottom(15).center(); // Amplía para texto largo
        table.row();
        table.add(saveButton).colspan(2).padTop(20).center();
        table.row();
        table.add(feedbackLabel).colspan(2).width(400).padTop(10).center();

        // ScrollPane para permitir desplazamiento
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFillParent(true);
        scrollPane.setFadeScrollBars(false);
        stage.addActor(scrollPane);
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
