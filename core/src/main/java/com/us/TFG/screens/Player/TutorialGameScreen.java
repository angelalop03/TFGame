package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.manager.*;
import com.us.TFG.model.*;
import com.us.TFG.screens.Player.Game.ChapterCatalogScreen;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.UIStyle;

import java.util.List;

public class TutorialGameScreen implements Screen {

    private Stage stage;
    private Scene currentScene;
    private Decision currentDecision;
    private CharacterGame currentCharacter;
    private int chapterId;
    private BitmapFont font;
    private Image background;
    private Image characterImage;
    private Label dialogueLabel;
    private Table optionsTable;
    private String username;
    private int psychologicalPoints = 0;  // Máximo 100
    private int relationshipPoints = 0;   // Máximo 100
    private ProgressBar psychologicalBar;
    private ProgressBar relationshipBar;


    public TutorialGameScreen(int chapterId) {
        this.chapterId = chapterId;
        this.currentScene = SceneManager.getFirstSceneOfChapter(chapterId);

        if (currentScene != null) {
            this.currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());

            if (currentDecision != null) {
                this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();

        if (currentScene == null) {
            System.err.println("No hay escenas disponibles para el capítulo: " + chapterId);
            return;
        }

        setupBackground();
        setupCharacterImage();
        setupDialogue();
        setupOptions();
        setupBars();
    }
    private void setupBars() {
        psychologicalBar = createBar(psychologicalPoints, 100);
        relationshipBar = createBar(relationshipPoints, 100);

        psychologicalBar.setPosition(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 250);
        relationshipBar.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 250);

        stage.addActor(psychologicalBar);
        stage.addActor(relationshipBar);
    }

    private ProgressBar createBar(int value, int max) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        Pixmap pixmap = new Pixmap(20, 200, Pixmap.Format.RGBA8888);

        // Fondo de la barra (gris oscuro)
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        style.background = new TextureRegionDrawable(new Texture(pixmap));

        pixmap.dispose();
        pixmap = new Pixmap(20, 200, Pixmap.Format.RGBA8888);

        // Parte de progreso de la barra (rosado)
        pixmap.setColor(new Color(1f, 0.4f, 0.7f, 1f));
        pixmap.fill();
        style.knobBefore = new TextureRegionDrawable(new Texture(pixmap));

        pixmap.dispose();

        ProgressBar bar = new ProgressBar(0, max, 1, true, style);
        bar.setValue(value);
        bar.setSize(20, 200);
        bar.setAnimateDuration(0.5f);  // Animación suave

        return bar;
    }

    private void updateBars() {
        psychologicalBar.setValue(psychologicalPoints);
        relationshipBar.setValue(relationshipPoints);
    }


    private void setupBackground() {
        Texture bgTexture = new Texture(Gdx.files.internal("assets/photos/Scenes/" + currentScene.getBackgroundImage()));
        background = new Image(new TextureRegionDrawable(bgTexture));
        background.setFillParent(true);
        stage.addActor(background);
    }

    private void setupCharacterImage() {
        if (currentCharacter != null) {
            Texture characterTexture = new Texture(Gdx.files.internal("assets/photos/Characters/" + currentCharacter.getImagePath()));
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(380, 600);
            characterImage.setPosition(180, 80);
            stage.addActor(characterImage);
        }
    }

    private void setupDialogue() {
        dialogueLabel = new Label(currentDecision != null ? currentDecision.getDialogue() : "No hay decisiones.", new Label.LabelStyle(font, Color.WHITE));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWidth(Gdx.graphics.getWidth() - 100);
        dialogueLabel.setPosition(50, Gdx.graphics.getHeight() - 150);
        stage.addActor(dialogueLabel);
    }

    private void setupOptions() {
        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);

        showOptions();
    }

    private void showOptions() {
        optionsTable.clear();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        List<Option> options = OptionManager.getOptionsByDecisionId(currentDecision.getId());

        for (Option option : options) {
            TextButton optionButton = new TextButton(option.getText(), buttonStyle);
            optionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleOptionSelection(option);
                }
            });
            optionsTable.add(optionButton).pad(10).fillX().row();
        }
    }

    private void handleOptionSelection(Option option) {
        if (option.getNextDecisionId() != null) {
            currentDecision = DecisionManager.getDecisionById(option.getNextDecisionId());
            currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            updateCharacterImage();
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();
        } else {
            goToNextSceneOrChapter();
        }
    }

    private void goToNextSceneOrChapter() {
        Scene nextScene = SceneManager.getNextSceneInChapter(currentScene.getId(), chapterId);

        if (nextScene != null) {
            currentScene = nextScene;
            currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());
            currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            updateCharacterImage();
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();
        } else {
            System.out.println("Capítulo terminado.");
            Render.app.setScreen(new PlayerMenuScreen(username));
        }
    }

    private void updateCharacterImage() {
        if (characterImage != null) {
            stage.getActors().removeValue(characterImage, true);
        }

        if (currentCharacter != null) {
            Texture characterTexture = new Texture(Gdx.files.internal("assets/photos/Characters/" + currentCharacter.getImagePath()));
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(320, 600);
            characterImage.setPosition(180, 80);
            stage.addActor(characterImage);
        }
    }

    @Override
    public void render(float delta) {
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
