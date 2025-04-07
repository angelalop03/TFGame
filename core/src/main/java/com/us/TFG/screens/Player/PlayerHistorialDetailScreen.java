package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.PlayerDecisionStateManager;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.model.PlayerDecisionState;
import com.us.TFG.utils.*;

import java.util.List;

public class PlayerHistorialDetailScreen implements Screen {
    private String username;
    private int gameId;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private Stage stage;
    private SpriteBatch batch;

    public PlayerHistorialDetailScreen(String username, int gameId) {
        this.username = username;
        this.gameId = gameId;
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

        // Crear la flecha de retroceso
        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerHistorialScreen(username));
            }
        });
        int playerId = PlayerManager.getPlayerData(username).getId();
        List<PlayerDecisionState> decisions = PlayerDecisionStateManager.getPositivePointDecisions(playerId, gameId);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(150);

        if (decisions.isEmpty()) {
            Label noDataLabel = new Label("No hay datos disponibles", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.RED));
            table.add(noDataLabel).pad(10).row();
        } else {
            for (PlayerDecisionState decision : decisions) {
                Label decisionLabel = new Label(decision.toString(), new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE));
                table.add(decisionLabel).pad(10).row();
            }
        }

        stage.addActor(table);
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
    public void dispose() {}
}
