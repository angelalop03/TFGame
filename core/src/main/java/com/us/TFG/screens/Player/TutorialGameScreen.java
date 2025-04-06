package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.us.TFG.utils.Render;
import com.us.TFG.utils.UIStyle;

import java.util.List;


public class TutorialGameScreen implements Screen {

    private Stage stage;
    private Scene currentScene;
    private Decision currentDecision;
    private CharacterGame currentCharacter;
    private BitmapFont font;
    private Image background;
    private Image characterImage;
    private Label dialogueLabel;
    private Table optionsTable;

    public TutorialGameScreen(int chapterId) {
        // Cargar la primera escena del tutorial (ID fijo o predefinido)
        this.currentScene = SceneManager.getFirstSceneOfChapter(chapterId);
        this.currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());

        if (currentDecision != null) {
            this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = UIStyle.getFont();

        Texture bgTexture = new Texture(Gdx.files.internal("assets/photos/Scenes/" + currentScene.getBackgroundImage()));
        background = new Image(new TextureRegionDrawable(bgTexture));
        background.setFillParent(true);
        stage.addActor(background);

        if (currentCharacter != null) {
            Texture characterTexture = new Texture(Gdx.files.internal("assets/photos/Characters/" + currentCharacter.getImagePath()));
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(380, 600);
            characterImage.setPosition(180, 0);
            stage.addActor(characterImage);
        }

        dialogueLabel = new Label(currentDecision != null ? currentDecision.getDialogue() : "No hay decisiones.", new Label.LabelStyle(font, Color.WHITE));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWidth(Gdx.graphics.getWidth() - 100);
        dialogueLabel.setPosition(50, Gdx.graphics.getHeight() - 150);
        stage.addActor(dialogueLabel);

        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);

        showOptions();
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

}
