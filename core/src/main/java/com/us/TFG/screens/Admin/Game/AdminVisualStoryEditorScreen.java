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
import com.us.TFG.manager.ChapterManager;
import com.us.TFG.manager.DecisionManager;
import com.us.TFG.model.Decision;
import com.us.TFG.model.Scene;
import com.us.TFG.utils.*;

import java.util.*;
import java.util.List;

public class AdminVisualStoryEditorScreen implements Screen {
    private String username;
    private Scene scene;
    private Stage stage;
    private Group canvas;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private SpriteBatch batch;
    private Imagen fondo;
    private TopBar topBar;

    private Map<Actor, List<Actor>> connections = new HashMap<>();

    public AdminVisualStoryEditorScreen(String username, Scene scene) {
        this.username = username;
        this.scene = scene;
    }

    @Override
    public void show() {
        fondo = new Imagen(Resources.FONDOBOOK2);
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

        List<Decision> decisions = DecisionManager.getDecisionsBySceneId(scene.getId());
        float offsetX = 100;
        float offsetY = 400;
        for (int i = 0; i < decisions.size(); i++) {
            Decision decision = decisions.get(i);
            createDecisionNode(decision.getId(), offsetX + i * 250, offsetY - i * 100);
        }

        TextButton addDecisionBtn = new TextButton("+ Añadir Decisión", UIStyle.getButtonStyle());
        addDecisionBtn.setPosition(30, 30);
        addDecisionBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Implementar diálogo de nueva decisión
            }
        });
        stage.addActor(addDecisionBtn);

        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new AdminEditChapterScreen(username, ChapterManager.getChapterById(scene.getChapterId())));
            }
        });
        stage.addActor(backArrowButton);
    }

    private void createDecisionNode(int decisionId, float x, float y) {
        Table node = new Table();
        node.setSize(200, 100);
        node.setBackground(new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(200, 100, new Color(0.3f, 0.3f, 0.3f, 0.7f), 0.7f))));
        node.setPosition(x, y);

        Label titleLabel = new Label("Decisión " + decisionId, UIStyle.getLabelStyle());
        node.add(titleLabel).expand().center();

        node.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                node.moveBy(x - node.getWidth() / 2, y - node.getHeight() / 2);
            }
        });

        canvas.addActor(node);

        if (canvas.getChildren().size > 1) {
            Actor previous = canvas.getChildren().get(canvas.getChildren().size - 2);
            connections.computeIfAbsent(previous, k -> new ArrayList<>()).add(node);
        }
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
