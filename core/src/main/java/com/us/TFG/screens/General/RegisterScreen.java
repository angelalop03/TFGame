package com.us.TFG.screens.General;

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
import com.us.TFG.manager.UserManager;
import com.us.TFG.screens.Player.PlayerMenuScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.validators.InputValidator;

public class RegisterScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private TextField usernameField, passwordField, firstnameField, lastnameField, emailField;
    private Label feedbackLabel;
    private Imagen fondo;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.MENU);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Generar fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();
        skin.add("Caveat", font);

        // Configuración de estilo para los campos de texto
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 40, Color.DARK_GRAY, 0.5f)));

        // Configuración de estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Configuración de estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 40, Color.GRAY, 0.5f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 40, Color.DARK_GRAY, 0.5f)));

        // Crear campos de entrada
        usernameField = new TextField("", textFieldStyle);
        usernameField.setMessageText("Nombre de usuario");
        passwordField = new TextField("", textFieldStyle);
        passwordField.setMessageText("Contraseña");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        firstnameField = new TextField("", textFieldStyle);
        firstnameField.setMessageText("Nombre");
        lastnameField = new TextField("", textFieldStyle);
        lastnameField.setMessageText("Apellido");
        emailField = new TextField("", textFieldStyle);
        emailField.setMessageText("Correo electrónico");

        // Botón de registro
        TextButton registerButton = new TextButton("Registrar", buttonStyle);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String firstname = firstnameField.getText().trim();
                String lastname = lastnameField.getText().trim();
                String email = emailField.getText().trim();

                String validationMessage = validateInputs(username, password, firstname, lastname, email);
                if (validationMessage != null) {
                    showError(validationMessage);
                    return;
                }

                if (UserManager.registerUser(username, password, firstname, lastname, email)) {
                    Render.app.setScreen(new PlayerMenuScreen(username));
                } else {
                    showError("Error: Nombre de usuario ya existe.");
                }
            }
        });

        // Botón de regresar
        TextButton backButton = new TextButton("Volver", buttonStyle);
        backButton.setSize(300, 40);
        backButton.setPosition(Gdx.graphics.getWidth() / 2f - 150, 60);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new OpenScreen());
            }
        });
        stage.addActor(backButton);

        // Etiqueta de feedback para mostrar mensajes
        feedbackLabel = new Label("", labelStyle);

        // Organizar los elementos en una tabla
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Registrar Nuevo Usuario", labelStyle)).colspan(2).padBottom(30);
        table.row();
        table.add(usernameField).width(400).padBottom(15);
        table.row();
        table.add(passwordField).width(400).padBottom(15);
        table.row();
        table.add(firstnameField).width(400).padBottom(15);
        table.row();
        table.add(lastnameField).width(400).padBottom(15);
        table.row();
        table.add(emailField).width(400).padBottom(15);
        table.row();
        table.add(registerButton).width(300).padTop(20).padBottom(20);
        table.row();
        table.add(feedbackLabel).colspan(2);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }



    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
    }

    private String validateInputs(String username, String password, String firstname, String lastname, String email) {
        String error;
        error = InputValidator.validateUsername(username);
        if (error != null) return error;

        error = InputValidator.validatePassword(password);
        if (error != null) return error;

        error = InputValidator.validateFirstname(firstname);
        if (error != null) return error;

        error = InputValidator.validateLastname(lastname);
        if (error != null) return error;

        return InputValidator.validateEmail(email);
    }


    private void showError(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
    }
}
