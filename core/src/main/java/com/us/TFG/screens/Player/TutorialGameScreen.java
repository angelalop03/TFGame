package com.us.TFG.screens.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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


public class TutorialGameScreen implements Screen {
    private static final int CHAPTER_ID = 1; // Capítulo estático del tutorial


    private Stage stage;
    private Scene currentScene;
    private Decision currentDecision;
    private CharacterGame currentCharacter;
    private String username;
    private BitmapFont font;
    private Image background;
    private Image characterImage;
    private Label dialogueLabel;
    private Table optionsTable;
    private Skin skin;
    private Texture bgTexture, characterTexture;
    private ProgressBar psychologicalBar, relationshipBar;
    private int previousPsychologicalPoints =-1;
    private int previousRelationshipPoints=-1;
    private Image dialogueBackground;
    private int currentPsychologicalPoints =0;
    private int   currentRelationshipPoints =0;
    private boolean updateBarsFlag = false;

    public TutorialGameScreen(String username) {

        this.username = username;
        setupBars();

    }
    private void loadGameData() {
        this.currentScene = SceneManager.getFirstSceneOfChapter(CHAPTER_ID);
        this.currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());

        if (currentDecision != null) {
            this.currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            loadTexturesForCurrentScene();
        }
    }

    private void loadTexturesForCurrentScene() {
        if (currentScene != null) {
            String backgroundPath = "assets/photos/Scenes/" + currentScene.getBackgroundImage();

            if (Gdx.files.internal(backgroundPath).exists()) {
                bgTexture = new Texture(Gdx.files.internal(backgroundPath));
            } else {
                Pixmap pixmap = new Pixmap(1280, 720, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.DARK_GRAY);
                pixmap.fill();
                bgTexture = new Texture(pixmap);
                pixmap.dispose();

            }
        }
    }

    private void setupBars() {
        stage = new Stage(new ScreenViewport());

        int maxPoints = 100;
        int psychologicalPoints = 0;
        int relationshipPoints = 0;

        psychologicalBar = createBar(psychologicalPoints, maxPoints);
        relationshipBar = createBar(relationshipPoints, maxPoints);

        psychologicalBar.setPosition(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 400);
        relationshipBar.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 400);

        stage.addActor(psychologicalBar);
        stage.addActor(relationshipBar);

        Gdx.input.setInputProcessor(stage);  // Necesario para que el Stage reciba eventos de input
    }

    private ProgressBar createBar(int value, int max) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        Pixmap pixmap = new Pixmap(20, 200, Pixmap.Format.RGBA8888);

        // Fondo de la barra (gris oscuro)
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        style.background = new TextureRegionDrawable(new Texture(pixmap));

        pixmap.dispose();
        pixmap = new Pixmap(20, 200, Pixmap.Format.RGBA8888);

        // Parte de progreso de la barra (rosado)
        Color pink = new Color(1f, 0.4f, 0.7f, 1f);
        pixmap.setColor(pink);
        pixmap.fill();
        style.knobBefore = new TextureRegionDrawable(new Texture(pixmap)); // La parte llenada

        pixmap.dispose();

        ProgressBar bar = new ProgressBar(0, max, 1, true, style); // Orientación vertical
        bar.setValue(value);
        bar.setSize(20, 200); // Establece el tamaño real de la barra
        bar.setAnimateDuration(0.5f); // Permitir animación suave

        return bar;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        loadGameData();

        if (currentDecision == null) {
            displayErrorMessage("No se puede jugar este capítulo");
            return;
        }

        if (currentScene != null) {
            String backgroundPath = "assets/photos/Scenes/" + currentScene.getBackgroundImage();
            if (Gdx.files.internal(backgroundPath).exists()) {
                bgTexture = new Texture(Gdx.files.internal(backgroundPath));
            } else {
                Pixmap pixmap = new Pixmap(1280, 720, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.DARK_GRAY);
                pixmap.fill();
                bgTexture = new Texture(pixmap);
                pixmap.dispose();
            }
            background = new Image(new TextureRegionDrawable(bgTexture));
            background.setFillParent(true);
            stage.addActor(background);
        }

        if (currentCharacter != null) {
            String characterPath = "assets/photos/Characters/" + currentCharacter.getImagePath();
            if (Gdx.files.internal(characterPath).exists()) {
                characterTexture = new Texture(Gdx.files.internal(characterPath));
                characterImage = new Image(new TextureRegionDrawable(characterTexture));
                characterImage.setSize(390, 600);
                characterImage.setPosition(180, 0);
                stage.addActor(characterImage);
            }
        }

        // Crear el fondo blanco con bordes redondeados para los diálogos
        int cornerRadius = 20;  // Radio de las esquinas redondeadas
        int backgroundWidth = 500;
        int backgroundHeight = 200;

        Pixmap pixmap = new Pixmap(backgroundWidth, backgroundHeight, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        pixmap.setColor(Color.WHITE);

        pixmap.fillRectangle(cornerRadius, 0, backgroundWidth - 2 * cornerRadius, backgroundHeight);
        pixmap.fillRectangle(0, cornerRadius, backgroundWidth, backgroundHeight - 2 * cornerRadius);
        pixmap.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        pixmap.fillCircle(backgroundWidth - cornerRadius - 1, cornerRadius, cornerRadius);
        pixmap.fillCircle(cornerRadius, backgroundHeight - cornerRadius - 1, cornerRadius);
        pixmap.fillCircle(backgroundWidth - cornerRadius - 1, backgroundHeight - cornerRadius - 1, cornerRadius);

        Texture backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        dialogueBackground = new Image(new TextureRegionDrawable(backgroundTexture));
        stage.addActor(dialogueBackground);

        dialogueLabel = new Label(currentDecision.getDialogue(), new Label.LabelStyle(UIStyle.getFont(), Color.GRAY));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.topLeft);
        dialogueLabel.setWidth(backgroundWidth - 40);  // Restar padding para que el texto no quede pegado a los bordes
        dialogueLabel.setPosition(Gdx.graphics.getWidth() - 650, Gdx.graphics.getHeight() - 250); // Cambia la posición según necesites

        dialogueBackground.setPosition(dialogueLabel.getX() - 20, dialogueLabel.getY() - 20);
        dialogueBackground.setSize(backgroundWidth, backgroundHeight);

        stage.addActor(dialogueBackground);
        stage.addActor(dialogueLabel);

        optionsTable = new Table();
        optionsTable.setFillParent(true);
        optionsTable.bottom().pad(20);
        stage.addActor(optionsTable);

        showOptions();
        // Añadir las barras al escenario
        stage.addActor(psychologicalBar);
        stage.addActor(relationshipBar);

        Texture backArrowTexture = new Texture(Gdx.files.internal("assets/photos/flecha.png"));
        ImageButton backArrowButton = new ImageButton(new TextureRegionDrawable(backArrowTexture));
        backArrowButton.setSize(100, 100);
        backArrowButton.setPosition(10, Gdx.graphics.getHeight() - 150);
        backArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerMenuScreen(username));
            }
        });
        stage.addActor(backArrowButton);
    }

    @Override
    public void render(float delta) {
        updateBars();

        Gdx.gl.glClearColor(0.36f, 0.27f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (dialogueLabel != null && dialogueBackground != null) {

            float padding = 20f; // Margen alrededor del texto
            float padding2 = 15f; // Margen alrededor del texto

            dialogueLabel.layout(); // Calcular el tamaño del texto
            float labelWidth = dialogueLabel.getWidth();
            float labelHeight = dialogueLabel.getPrefHeight();

            dialogueBackground.setSize(labelWidth + 35f, labelHeight + padding * 2);
            dialogueBackground.setPosition(dialogueLabel.getX() - padding*2, dialogueLabel.getY()  - labelHeight+padding );
        }
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
    private void updateBars() {
        if (updateBarsFlag) {  // Solo actualiza si el flag está activo
            if (currentPsychologicalPoints != previousPsychologicalPoints) {
                psychologicalBar.setValue(currentPsychologicalPoints);
                previousPsychologicalPoints = currentPsychologicalPoints;
            }

            if (currentRelationshipPoints != previousRelationshipPoints) {
                relationshipBar.setValue(currentRelationshipPoints);
                previousRelationshipPoints = currentRelationshipPoints;
            }

            // Desactiva el flag después de actualizar
            updateBarsFlag = false;
        }
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

        int optionWidth = 600;
        int optionHeight = 80;

        // Cantidad de opciones disponibles
        int numberOfOptions = options.size();

        // Ajustar la posición vertical en función del número de opciones
        int baseTopPadding = 200;   // Punto más bajo de inicio para opciones múltiples
        int maxTopPadding = 650;    // Punto más alto de inicio para una única opción

        // Calcular padding dinámico según la cantidad de opciones
        int topPadding = maxTopPadding - (numberOfOptions - 1) * 100; // Espaciado progresivo

        // Limitar el padding superior para que no se salga de la pantalla
        topPadding = Math.max(baseTopPadding, topPadding);

        // Configurar la tabla de opciones para que esté a la derecha de la pantalla
        optionsTable.setFillParent(true);
        optionsTable.top().right().padTop(topPadding).padRight(20);

        for (Option option : options) {

            // Crear un Pixmap con fondo rosado transparente sin bordes redondeados
            Pixmap pixmap = new Pixmap(optionWidth, optionHeight, Pixmap.Format.RGBA8888);
            pixmap.setBlending(Pixmap.Blending.SourceOver);

            // Color rosado con transparencia (ej: 80% de opacidad)
            pixmap.setColor(new Color(1f, 0.4f, 0.7f, 0.8f));
            pixmap.fillRectangle(0, 0, optionWidth, optionHeight);

            Texture optionTexture = new Texture(pixmap);
            pixmap.dispose();

            Image optionBackground = new Image(new TextureRegionDrawable(optionTexture));

            // Crear un contenedor para el fondo y el botón
            Table optionContainer = new Table();
            optionContainer.add(optionBackground).size(optionWidth, optionHeight);

            // Crear el botón de opción
            TextButton optionButton = new TextButton(option.getText(), buttonStyle);
            optionButton.getLabel().setWrap(true);
            optionButton.pad(10);

            optionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleOptionSelection(option);
                }
            });

            // Añadir el botón encima del fondo
            optionContainer.add(optionButton).padLeft(-optionWidth).width(optionWidth - 20).height(optionHeight - 20);

            // Añadir la opción a la tabla de opciones alineadas a la derecha
            optionsTable.add(optionContainer).pad(10).row();
        }
    }



    private void handleOptionSelection(Option option) {

        if (currentDecision.getId() == 5) {
            // Cambia los puntos en función de la opción seleccionada
            currentPsychologicalPoints += option.getPointsM();
            currentRelationshipPoints += option.getPointsR();

            // Asegúrate de que los puntos estén dentro de un rango válido (0 a 100)
            currentPsychologicalPoints = Math.max(0, Math.min(100, currentPsychologicalPoints));
            currentRelationshipPoints = Math.max(0, Math.min(100, currentRelationshipPoints));

            // Activa el flag para actualizar las barras
            updateBarsFlag = true;
        }

        // Actualizar las barras si el flag está activado
        updateBars();
        if (option.getNextDecisionId() != null) {
            // Cargar la siguiente decisión
            currentDecision = DecisionManager.getDecisionById(option.getNextDecisionId());
            currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
            updateCharacterImage();
            dialogueLabel.setText(currentDecision.getDialogue());
            showOptions();


        } else {
            // Comprobar si todas las decisiones de la escena están completas

            goToNextSceneOrChapter();
            System.out.println("GotoNextSceneOrChapter");

        }
    }

    private void goToNextSceneOrChapter() {
        Scene nextScene = SceneManager.getNextSceneInChapter(currentScene.getId(), 1);

        if (nextScene != null) {
            currentScene = nextScene;
            currentDecision = DecisionManager.getFirstDecisionBySceneId(currentScene.getId());
            updateBackgroundImage();

            if (currentDecision != null) {
                currentCharacter = CharacterManager.getCharacterById(currentDecision.getCharacterId());
                updateCharacterImage();
                dialogueLabel.setText(currentDecision.getDialogue());
            }
            showOptions();

        } else {
            Render.app.setScreen(new PlayerMenuScreen(username));
        }
    }

    private void updateCharacterImage() {
        if (characterImage != null) {
            stage.getActors().removeValue(characterImage, true);
        }

        if (currentCharacter != null) {
            String characterPath = "assets/photos/Characters/" + currentCharacter.getImagePath();
            if (Gdx.files.internal(characterPath).exists()) {
                characterTexture = new Texture(Gdx.files.internal(characterPath));
                characterImage = new Image(new TextureRegionDrawable(characterTexture));
                characterImage.setSize(390, 600);
                characterImage.setPosition(180, 0);
                stage.addActor(characterImage);
            }
        }
    }

    private void updateBackgroundImage() {
        if (background != null) {
            stage.getActors().removeValue(background, true);
        }

        if (currentScene != null) {
            String backgroundPath = "assets/photos/Scenes/" + currentScene.getBackgroundImage();
            if (Gdx.files.internal(backgroundPath).exists()) {
                bgTexture = new Texture(Gdx.files.internal(backgroundPath));
            } else {
                Pixmap pixmap = new Pixmap(1280, 720, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.DARK_GRAY);
                pixmap.fill();
                bgTexture = new Texture(pixmap);
                pixmap.dispose();
            }
            background = new Image(new TextureRegionDrawable(bgTexture));
            background.setFillParent(true);
            stage.addActor(background);
            background.toBack();

        }
    }

}
