package com.us.TFG.screens.Admin.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.HistoryManager;
import com.us.TFG.manager.SceneManager;
import com.us.TFG.model.Chapter;
import com.us.TFG.model.Scene;
import com.us.TFG.utils.*;

import java.util.*;
import java.util.List;

public class AdminVisualStoryEditorScreen implements Screen {
    private String username;
    private Chapter chapter;
    private Stage stage;
    private Group canvas;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private SpriteBatch batch;
    private Imagen fondo;
    private TopBar topBar;

    private Map<Actor, List<Actor>> connections = new HashMap<>();

    public AdminVisualStoryEditorScreen(String username, Chapter chapter) {
        this.username = username;
        this.chapter = chapter;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.FONDODOORS);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();
        topBar = new TopBar(username, stage, font);
        shapeRenderer = new ShapeRenderer();

        canvas = new Group();
        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);

        List<Scene> scenes = SceneManager.getScenesByChapterId(chapter.getId());
        float offsetX = 100;
        float offsetY = 400;
        for (int i = 0; i < scenes.size(); i++) {
            Scene scene = scenes.get(i);
            createSceneNode(scene.getId(), offsetX + i * 250, offsetY - i * 100);
        }

        TextButton addSceneBtn = new TextButton("+ Añadir Escena", UIStyle.getButtonStyle());
        addSceneBtn.setPosition(30, 30);
        addSceneBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNewSceneDialog();
            }
        });
        stage.addActor(addSceneBtn);

        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new AdminEditChapterScreen(username, chapter));
            }
        });
        stage.addActor(backArrowButton);
    }

    private void createSceneNode(int sceneId, float x, float y) {
        Table node = new Table();
        node.setBackground(UIStyle.getBoxBackground());
        node.setPosition(x, y);

        Label titleLabel = new Label("Escena " + sceneId, UIStyle.getLabelStyle());
        node.add(titleLabel).pad(10);

        node.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                node.moveBy(x - node.getWidth() / 2, y - node.getHeight() / 2);
            }
        });

        node.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) {
                    showEditDialog(titleLabel);
                }
            }
        });

        canvas.addActor(node);

        if (canvas.getChildren().size > 1) {
            Actor previous = canvas.getChildren().get(canvas.getChildren().size - 2);
            connections.computeIfAbsent(previous, k -> new ArrayList<>()).add(node);
        }
    }

    private void showNewSceneDialog() {
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(
            new Texture(UIStyle.createRoundedPixmap(500, 250, new Color(0.3f, 0.2f, 0.5f, 1f), 1f))
        );

        Dialog dialog = new Dialog("Nueva Escena", dialogStyle);

        Label feedback = new Label("", UIStyle.getLabelStyle());
        feedback.setWrap(true);
        feedback.setAlignment(Align.center);

        dialog.getContentTable().add(new Label("¿Deseas crear una nueva escena?", UIStyle.getLabelStyle()))
            .colspan(2).pad(20).row();

        TextButton createBtn = new TextButton("Crear", UIStyle.getButtonStyle());
        createBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Scene nueva = new Scene();
                nueva.setChapterId(chapter.getId());
                nueva.setSceneOrder(canvas.getChildren().size + 1);

                int newId = SceneManager.createScene(nueva);

                if (newId > 0) {
                    x = 100 + canvas.getChildren().size * 180;
                    y = 300;
                    createSceneNode(newId, x, y);
                    dialog.hide();
                    dialog.remove();
                } else {
                    UIStyle.showError(feedback, "Error al crear la escena.");
                }
            }
        });

        TextButton cancelBtn = new TextButton("Cancelar", UIStyle.getButtonStyle());
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.getButtonTable().add(createBtn).pad(10);
        dialog.getButtonTable().add(cancelBtn).pad(10);
        dialog.getContentTable().add(feedback).colspan(2).width(400).padTop(10).row();

        dialog.show(stage);
    }

    private void showEditDialog(Label label) {
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(
            new Texture(UIStyle.createRoundedPixmap(500, 300, new Color(0.3f, 0.2f, 0.5f, 1f), 1f))
        );

        Dialog dialog = new Dialog("Editar Escena", dialogStyle);

        TextField input = new TextField(label.getText().toString(), UIStyle.getTextFieldStyle());

        Label feedback = new Label("", UIStyle.getLabelStyle());
        feedback.setWrap(true);

        dialog.getContentTable().add(new Label("Nuevo Título:", UIStyle.getLabelStyle())).left().pad(5);
        dialog.getContentTable().add(input).width(350).pad(5).row();

        TextButton saveBtn = new TextButton("Guardar", UIStyle.getButtonStyle());
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String newText = input.getText().trim();
                if (newText.isEmpty()) {
                    UIStyle.showError(feedback, "El título no puede estar vacío.");
                    return;
                }

                label.setText(newText);
                feedback.setText("Título actualizado.");
                feedback.setColor(Color.GREEN);

                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        dialog.hide();
                        dialog.remove();
                    }
                }, 1.2f);
            }
        });

        TextButton cancelBtn = new TextButton("Cancelar", UIStyle.getButtonStyle());
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.getButtonTable().add(saveBtn).pad(10);
        dialog.getButtonTable().add(cancelBtn).pad(10);
        dialog.getContentTable().add(feedback).colspan(2).width(400).padTop(10).row();

        dialog.show(stage);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        fondo.draw();
        batch.end();

        stage.act(delta);
        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        for (Map.Entry<Actor, List<Actor>> entry : connections.entrySet()) {
            Actor from = entry.getKey();
            for (Actor to : entry.getValue()) {
                float startX = from.getX() + from.getWidth() / 2;
                float startY = from.getY() + from.getHeight() / 2;
                float endX = to.getX() + to.getWidth() / 2;
                float endY = to.getY() + to.getHeight() / 2;
                shapeRenderer.line(startX, startY, endX, endY);
            }
        }
        shapeRenderer.end();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); shapeRenderer.dispose(); }
}
