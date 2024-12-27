package com.us.TFG.screens;

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
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;
import com.us.TFG.manager.UserManager;
import com.us.TFG.utils.validators.InputValidator;


public class ChangePasswordScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private TextField newPasswordField, confirmPasswordField;
    private Label feedbackLabel;
    private BitmapFont font;
    private String currentUsername;
    private Imagen fondo;
    private SpriteBatch batch;
    private TopBar topBar;


    public ChangePasswordScreen(String username) {
        this.currentUsername = username;
    }

    @Override
    public void show() {
        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        //Cargar el fondo
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
        topBar = new TopBar(currentUsername, stage, font);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 50, Color.DARK_GRAY, 0.5f)));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.GRAY, 0.5f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.DARK_GRAY, 0.5f)));

        newPasswordField = new TextField("", textFieldStyle);
        newPasswordField.setMessageText("Nueva Contraseña");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        confirmPasswordField = new TextField("", textFieldStyle);
        confirmPasswordField.setMessageText("Confirmar Contraseña");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');

        feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(true);

        TextButton saveButton = new TextButton("Cambiar Contraseña", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String newPassword = newPasswordField.getText().trim();
                String confirmPassword = confirmPasswordField.getText().trim();

                String validationMessage = InputValidator.validatePassword(newPassword);
                if (validationMessage != null) {
                    feedbackLabel.setText(validationMessage);
                    feedbackLabel.setColor(Color.RED);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    feedbackLabel.setText("Las contraseñas no coinciden.");
                    feedbackLabel.setColor(Color.RED);
                    return;
                }

                if (UserManager.updatePassword(currentUsername, newPassword)) {
                    feedbackLabel.setText("Contraseña actualizada con éxito.");
                    feedbackLabel.setColor(Color.GREEN);
                } else {
                    feedbackLabel.setText("Error al cambiar la contraseña.");
                    feedbackLabel.setColor(Color.RED);
                }
            }
        });

        TextButton backButton = new TextButton("Volver", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new EditProfileScreen(currentUsername));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Cambiar Contraseña", labelStyle)).colspan(2).padBottom(30);
        table.row();
        table.add(newPasswordField).width(400).padBottom(15);
        table.row();
        table.add(confirmPasswordField).width(400).padBottom(15);
        table.row();
        table.add(saveButton).width(300).padTop(15);
        table.row();
        table.add(backButton).width(300).padTop(15);
        table.row();
        table.add(feedbackLabel).width(350).padTop(8);

        stage.addActor(table);

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
