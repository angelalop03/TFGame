package com.us.TFG.screens.Player.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.us.TFG.utils.UIStyle;

import java.util.List;


public class SceneGameScreen implements Screen {

    private Stage stage;
    private AssetManager assetManager;
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
    private Texture bgTexture, characterTexture;


    public SceneGameScreen(int gameId, int chapterId, String username) {
        this.gameId = gameId;
        this.chapterId = chapterId;
        this.username = username;
        this.assetManager = new AssetManager();

        loadGameData(gameId);
    }
    private void loadGameData(int gameId) {
        Game game = GameManager.getGameById(gameId);
        if (game != null) {
            this.currentScene = SceneManager.getSceneById(game.getCurrentSceneId());
            this.currentDecision = DecisionManager.getDecisionById(game.getCurrentDecisionId());
            if (currentDecision != null) {
                this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
                loadTexturesForCurrentScene();
                assetManager.finishLoading();
            }
        }
    }

    private void loadTexturesForCurrentScene() {
        if (currentScene != null) {
            String backgroundPath = "assets/photos/Scenes/" + currentScene.getBackgroundImage();

            if (!assetManager.isLoaded(backgroundPath)) {
                if (Gdx.files.internal(backgroundPath).exists()) {
                    assetManager.load(backgroundPath, Texture.class);
                    assetManager.finishLoading(); // Asegúrate de que se cargue antes de obtenerla
                    System.out.println("Textura cargada correctamente: " + backgroundPath);
                } else {
                    System.err.println("Error: La textura de fondo no existe en la ruta especificada: " + backgroundPath);
                }
            } else {
                System.out.println("Textura ya cargada: " + backgroundPath);
            }

            if (currentCharacter != null) {
                String characterPath = "assets/photos/Characters/" + currentCharacter.getImagePath();

                if (!assetManager.isLoaded(characterPath)) {
                    if (Gdx.files.internal(characterPath).exists()) {
                        assetManager.load(characterPath, Texture.class);
                        assetManager.finishLoading();
                        System.out.println("Textura de personaje cargada correctamente: " + characterPath);
                    } else {
                        System.err.println("Error: La textura de personaje no existe en la ruta especificada: " + characterPath);
                    }
                } else {
                    System.out.println("Textura de personaje ya cargada: " + characterPath);
                }
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        if (currentDecision == null) {
            handleMissingDecisionError();
            return;
        }

        if (currentScene != null && assetManager.isLoaded("assets/photos/Scenes/" + currentScene.getBackgroundImage())) {
            bgTexture = assetManager.get("assets/photos/Scenes/" + currentScene.getBackgroundImage(), Texture.class);
            background = new Image(new TextureRegionDrawable(bgTexture));
            background.setFillParent(true);
            stage.addActor(background);
        } else {
            displayErrorMessage("Error al cargar la textura de fondo.");
        }

        if (currentCharacter != null && assetManager.isLoaded("assets/photos/Characters/" + currentCharacter.getImagePath())) {
            characterTexture = assetManager.get("assets/photos/Characters/" + currentCharacter.getImagePath(), Texture.class);
            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(320, 600);
            characterImage.setPosition(180, 80);
            stage.addActor(characterImage);
        } else {
            displayErrorMessage("Error al cargar la textura del personaje.");
        }

        dialogueLabel = new Label(currentDecision.getDialogue(), new Label.LabelStyle(UIStyle.getFont(), Color.WHITE));
        dialogueLabel.setWrap(true);
        dialogueLabel.setPosition(50, Gdx.graphics.getHeight() - 150);
        stage.addActor(dialogueLabel);

        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);

        showOptions();
    }

    private void handleMissingDecisionError() {
        System.err.println("Error: No se ha encontrado una decisión válida para esta escena.");
        displayErrorMessage("No se puede ejecutar este capitulo ");
    }

    private void displayErrorMessage(String message) {
        Label errorLabel = new Label(message, new Label.LabelStyle(UIStyle.getFont(), Color.RED));
        errorLabel.setAlignment(Align.center);
        errorLabel.setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f);
        errorLabel.setWidth(600);
        errorLabel.setWrap(true);
        stage.addActor(errorLabel);

        TextButton continueButton = new TextButton("Continuar", UIStyle.getButtonStyle());
        continueButton.setPosition(Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f - 100);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerMenuScreen(username)); // Volver al menú principal
            }
        });
        stage.addActor(continueButton);
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
        dialogStyle.background = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(400, 600, Color.PINK, 0.8f)));

        Dialog logoutDialog = new Dialog("", dialogStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(300, 60, Color.GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(300, 60, Color.LIGHT_GRAY, 0.7f)));

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

        List<Option> options = OptionManager.getOptionsByDecisionId(currentDecision.getId());
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = UIStyle.getFont();
        buttonStyle.fontColor = Color.WHITE;

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
            currentScene = nextScene;
            currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());

            if (currentDecision != null) {
                currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());

                // Precargar las texturas necesarias antes de usarlas
                loadTexturesForCurrentScene();

                DecisionManager.updateCurrentDecision(gameId, currentDecision.getId());
                updateCharacterImage();
                dialogueLabel.setText(currentDecision.getDialogue());
            }
            SceneManager.updateCurrentScene(gameId, currentScene.getId());

            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();
        } else {
            System.out.println("No hay más escenas en este capítulo.");
            ChapterManager.markChapterAsPlayed(gameId, chapterId);

            int nextChapterId = ChapterManager.unlockNextChapter(gameId, chapterId);

            if (nextChapterId > 0) {
                Scene firstScene = SceneManager.getFirstSceneOfChapter(gameId, nextChapterId);
                if (firstScene != null) {
                    currentScene = firstScene;
                    currentDecision = DecisionManager.getFirstDecisionBySceneId(firstScene.getId());
                    currentCharacter = (currentDecision != null) ? CharacterManager.getCharacterById(currentDecision.getCharacterId()) : null;
                    chapterId = nextChapterId;

                    loadTexturesForCurrentScene();

                    ChapterManager.updateCurrentChapter(gameId, nextChapterId);
                    SceneManager.updateCurrentScene(gameId, currentScene.getId());
                    DecisionManager.updateCurrentDecision(gameId, (currentDecision != null) ? currentDecision.getId() : 0);

                    Render.app.setScreen(new ChapterCatalogScreen(username, gameId));
                }
            } else {
                System.out.println("El next chapter id no se pilla bien");
            }
        }
    }


    private void updateCharacterImage() {
        if (characterImage != null) {
            stage.getActors().removeValue(characterImage, true);
        }

        if (currentCharacter != null) {
            String characterPath = "assets/photos/Characters/" + currentCharacter.getImagePath();

            if (!assetManager.isLoaded(characterPath)) {
                assetManager.load(characterPath, Texture.class);
                assetManager.finishLoading();
            }

            characterTexture = assetManager.get(characterPath, Texture.class);

            characterImage = new Image(new TextureRegionDrawable(characterTexture));
            characterImage.setSize(320, 600);
            characterImage.setPosition(180, 80);
            stage.addActor(characterImage);
        }
    }








}
