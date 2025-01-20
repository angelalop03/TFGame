package com.us.TFG.screens.Player.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.manager.*;
import com.us.TFG.model.Decision;
import com.us.TFG.model.Option;
import com.us.TFG.model.Scene;
import com.us.TFG.utils.Render;
import com.us.TFG.model.CharacterGame;
import java.util.List;


public class SceneGameScreen implements Screen {

    private Stage stage;
    private Scene currentScene;
    private Decision currentDecision;
    private CharacterGame currentCharacter;
    private int chapterId;
    private int gameId;
    private String username;
    private BitmapFont font;
    private Image background;
    private Image characterImage;
    private Image speechBubble;
    private Label dialogueLabel;
    private Table optionsTable;
    private Skin skin;


    public SceneGameScreen(int gameId, int chapterId, String username) {
        this.gameId = gameId;
        this.chapterId = chapterId;
        this.username = username;
        this.currentScene = SceneManager.getFirstSceneOfChapter(gameId, chapterId); // Obtener la primera escena
        if (currentScene != null) {
            this.currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());
            if (currentDecision !=null){
                this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());

            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        if (currentScene == null) {
            System.err.println("No hay escenas disponibles para el capítulo: " + chapterId);
            Render.app.setScreen(new ChapterCatalogScreen(username, gameId));
            return;
        }

        // Configurar fondo de la escena
        Texture bgTexture = new Texture(Gdx.files.internal("assets/photos/Scenes/" + currentScene.getBackgroundImage()));
        background = new Image(new TextureRegionDrawable(bgTexture));
        background.setFillParent(true);
        stage.addActor(background);

        // Configurar imagen del personaje
        if (currentCharacter != null) {
            Texture characterTexture = new Texture(Gdx.files.internal("assets/photos/Characters/" + currentCharacter.getImagePath()));
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(400, 600); // Ajustar tamaño del personaje
            characterImage.setPosition(150, 80); // Ajustar posición del personaje
            stage.addActor(characterImage);
        }

        // Configurar bocadillo de diálogo
        Texture speechBubbleTexture = new Texture(Gdx.files.internal("assets/photos/Scenes/Speech_Bubble.png"));
        speechBubble = new Image(new TextureRegionDrawable(speechBubbleTexture));
        speechBubble.setSize(300, 200); // Ajustar tamaño del bocadillo
        speechBubble.setPosition(500, Gdx.graphics.getHeight() - 230); // Ajustar posición del bocadillo
        stage.addActor(speechBubble);



        // Configurar fuente y diálogo inicial
        dialogueLabel = new Label(currentDecision != null ? currentDecision.getDialogue() : "No hay decisiones.", new Label.LabelStyle(font, null));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWidth(Gdx.graphics.getWidth() - 100);
        dialogueLabel.setPosition(50, Gdx.graphics.getHeight() - 150);
        stage.addActor(dialogueLabel);

        // Configurar tabla para opciones
        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);

        showOptions();



    }

    private void showOptions() {
        optionsTable.clear();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(1000, 30, Color.PURPLE, 0.8f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(1000, 30, Color.GRAY, 0.8f)));
        buttonStyle.over = new TextureRegionDrawable(new Texture(createRoundedPixmap(1000, 30, Color.DARK_GRAY, 0.8f)));
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.font = font;
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
        // Actualizar puntos en el juego
        GameManager.updatePoints(gameId, option.getPointsM(), option.getPointsR());

        if (option.getNextDecisionId() != null) {
            // Cargar la siguiente decisión
            currentDecision = DecisionManager.getDecisionById(option.getNextDecisionId());
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();
        } else {
            // Pasar a la siguiente escena o capítulo
            //goToNextSceneOrChapter();
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
        font.dispose();

    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
    }

}
