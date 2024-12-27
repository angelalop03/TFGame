package com.us.TFG.screens.Player;

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
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;
import com.us.TFG.utils.validators.InputValidator;

public class EditProfileScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Imagen fondo;
    private TopBar topBar;
    private TextField usernameField, firstnameField, lastnameField, emailField;
    private Label feedbackLabel;
    private SpriteBatch batch;
    private BitmapFont font;
    private String currentUsername;
    private Image profileImage;
    private String currentProfileImage;
    private final String[] profileImages = {"default_image.png", "Perfil_2.png", "Perfil_3.jpg", "Perfil_4.jpg"};
    private int currentImageIndex = 0;


    public EditProfileScreen(String username) {
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


        // Crear estilo para los botones

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 40, Color.DARK_GRAY, 0.5f)));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.GRAY, 0.5f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(300, 50, Color.DARK_GRAY, 0.5f)));

        // Crear campos de texto para los datos del perfil
        usernameField = new TextField("", textFieldStyle);


        firstnameField = new TextField("", textFieldStyle);
        lastnameField = new TextField("", textFieldStyle);
        emailField = new TextField("", textFieldStyle);

        // Etiqueta para mostrar mensajes de feedback
        feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(true);

        //Cambiarimagen de perfil
        profileImage = new Image();
        Button changeImageButton = new TextButton("Cambiar Foto", buttonStyle);

        changeImageButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeProfileImage();
            }

        });

        // Cargar los datos actuales del usuario en los campos
        UserManager.loadUserProfile(currentUsername, usernameField, firstnameField, lastnameField, emailField,currentProfileImage, profileImage);

        // Botón para guardar los cambios
        TextButton saveButton = new TextButton("Guardar", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Obtener los valores ingresados por el usuario
                String newUsername = usernameField.getText().trim();
                String firstname = firstnameField.getText().trim();
                String lastname = lastnameField.getText().trim();
                String email = emailField.getText().trim();

                // Validar los campos ingresados
                String validationMessage = validateInputs(newUsername, firstname, lastname, email);
                if (validationMessage != null) {
                    showError(validationMessage);
                    return;
                }

                // Actualizar el perfil si los datos son válidos
                if (UserManager.updateProfile(currentUsername,newUsername, firstname, lastname, email, currentProfileImage)) {
                    feedbackLabel.setText("Perfil actualizado con éxito.");
                    feedbackLabel.setColor(Color.GREEN);

                } else {
                    showError("Error al actualizar el perfil.");
                }
            }
        });

        //Boton para pantalla de cambio de contraseña
        TextButton passwordButton = new TextButton("Cambiar Contraseña", buttonStyle);
        passwordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new ChangePasswordScreen(currentUsername));
            }
        });


        // Botón para volver a la pantalla anterior
        TextButton backButton = new TextButton("Volver", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new PlayerPerfilScreen(currentUsername));
            }
        });

        // Crear una tabla para organizar los elementos en la pantalla
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Editar Perfil", labelStyle)).colspan(2).padBottom(30);
        table.row();
        table.add(profileImage).size(100, 100).padBottom(15);
        table.row();
        table.add(changeImageButton).width(200).padBottom(20);
        table.row();
        table.add(usernameField).width(400).padBottom(15);
        table.row();
        table.add(firstnameField).width(400).padBottom(15);
        table.row();
        table.add(lastnameField).width(400).padBottom(15);
        table.row();
        table.add(emailField).width(400).padBottom(15);
        table.row();
        table.add(passwordButton).width(300).padTop(15);
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

    private void changeProfileImage() {
        currentImageIndex = (currentImageIndex + 1) % profileImages.length;
        currentProfileImage = profileImages[currentImageIndex];
        profileImage.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("assets/photos/players/" + currentProfileImage))));
    }

    private void showError(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setColor(Color.RED);
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
    }
    private String validateInputs(String username, String firstname, String lastname, String email) {
        if (InputValidator.validateUsername(username) != null) return "Nombre de usuario no válido.";
        if (InputValidator.validateFirstname(firstname) != null) return "Nombre no válido.";
        if (InputValidator.validateLastname(lastname) != null) return "Apellido no válido.";
        if (InputValidator.validateEmail(email) != null) return "Correo electrónico no válido.";
        return null;
    }
}
