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
import com.us.TFG.model.*;
import com.us.TFG.screens.Player.PlayerMenuScreen;
import com.us.TFG.utils.Render;

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
        Game game = GameManager.getGameById(gameId);
        if (game != null) {
            this.currentScene = SceneManager.getSceneById(game.getCurrentSceneId());
            if (currentScene != null) {
                this.currentDecision = DecisionManager.getDecisionById(game.getCurrentDecisionId());

                if (currentDecision != null) {
                    this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());

                }
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
            characterImage.setSize(320, 600); // Ajustar tamaño del personaje
            characterImage.setPosition(180, 80); // Ajustar posición del personaje
            stage.addActor(characterImage);
        }





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

        // Configurar botón de ajustes
        Texture settingsTexture = new Texture(Gdx.files.internal("assets/photos/Settings.png"));
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));
        settingsButton.setSize(64, 64);
        settingsButton.setPosition(Gdx.graphics.getWidth() - 74, Gdx.graphics.getHeight() - 74);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettingsDialog();
            }
        });
        stage.addActor(settingsButton);



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

    private void showSettingsDialog() {
        // Crear estilo del diálogo
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 600, Color.PINK, 0.8f)));

        Dialog logoutDialog = new Dialog("", dialogStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 60, Color.GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 60, Color.LIGHT_GRAY, 0.7f)));

        // Botón de confirmar
        TextButton confirmButton = new TextButton("Salir", buttonStyle);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerMenuScreen(username));
            }
        });

        // Botón de cancelar
        TextButton cancelButton = new TextButton("Cerrar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logoutDialog.hide();
            }
        });
        logoutDialog.getContentTable().row();
        logoutDialog.getContentTable().add(confirmButton).padBottom(10).row();
        logoutDialog.getContentTable().add(cancelButton).padBottom(10).row();

        logoutDialog.show(stage);
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
        //Actualice el PlayerSceneState
        PlayerDecisionStateManager.updateDecisionState(PlayerManager.getPlayerData(username).getId(),gameId, currentDecision.getId(), true);

        if (option.getNextDecisionId() != null) {
            // Cargar la siguiente decisión
            currentDecision = DecisionManager.getDecisionById(option.getNextDecisionId());
            DecisionManager.updateCurrentDecision(gameId, currentDecision.getId());
            currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            updateCharacterImage();
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();


        } else {
            // Comprobar si todas las decisiones de la escena están completas
            boolean allDecisionsComplete = PlayerDecisionStateManager.areAllDecisionsComplete(PlayerManager.getPlayerData(username).getId(), gameId, currentScene.getId());

            if (allDecisionsComplete) {
                goToNextSceneOrChapter();
                System.out.println("GotoNextSceneOrChapter");
            } else {
                dialogueLabel.setText("Completa todas las decisiones para avanzar.");
            }
        }
    }

    private void goToNextSceneOrChapter() {
        Scene nextScene = SceneManager.getNextSceneInChapter(currentScene.getId(), chapterId);

        if (nextScene != null) {
            // Avanzar a la siguiente escena
            currentScene = nextScene;
            currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());
            if (currentDecision !=null) {
                currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
                DecisionManager.updateCurrentDecision(gameId, currentDecision.getId());
                updateCharacterImage();
                dialogueLabel.setText(currentDecision.getDialogue());

            }
            SceneManager.updateCurrentScene(gameId, currentScene.getId());

            dialogueLabel.setText(currentDecision.getDialogue());
            updateCharacterImage();
            showOptions();
        } else {
            // No hay más escenas en este capítulo
            System.out.println("No hay más escenas en este capítulo.");
            ChapterManager.markChapterAsPlayed(gameId, chapterId);

            // Desbloquear el próximo capítulo basado en chapter_number
            int nextChapterId = ChapterManager.unlockNextChapter(gameId, chapterId);

            if (nextChapterId > 0) {
                Scene firstScene = SceneManager.getFirstSceneOfChapter(gameId, nextChapterId);
                if (firstScene !=null) {
                    currentScene = firstScene;
                    currentDecision = DecisionManager.getFirstDecisionBySceneId(firstScene.getId());
                    currentCharacter = (currentDecision != null) ? CharacterManager.getCharacterById(currentDecision.getCharacterId()) : null;
                    chapterId = nextChapterId;

                    // Actualizar juego en la base de datos
                    ChapterManager.updateCurrentChapter(gameId, nextChapterId);
                    SceneManager.updateCurrentScene(gameId, currentScene.getId());
                    DecisionManager.updateCurrentDecision(gameId, (currentDecision != null) ? currentDecision.getId() : 0);
                }
                Render.app.setScreen(new ChapterCatalogScreen(username, gameId));
            } else{
                System.out.println("El next chapter id no se pilla bien");
            }
        }
    }


    private void updateCharacterImage() {
        if (characterImage != null) {
            stage.getActors().removeValue(characterImage, true);
        }

        if (currentCharacter != null) {
            Texture characterTexture = new Texture(Gdx.files.internal("assets/photos/Characters/" + currentCharacter.getImagePath()));
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(320, 600); // Ajustar tamaño del personaje
            characterImage.setPosition(180, 80); // Ajustar posición del personaje
            stage.addActor(characterImage);
        }
    }






    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
    }

}
