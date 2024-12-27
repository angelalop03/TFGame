package com.us.TFG.screens.Admin;

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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.manager.UserManager;
import com.us.TFG.screens.Player.PlayerPerfilScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;
import com.us.TFG.utils.validators.InputValidator;

import java.util.List;


public class AdminViewPlayersScreen implements Screen {

    private String adminUsername;
    private Imagen fondo;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private TopBar topBar;
    private Label feedbackLabel;


    public AdminViewPlayersScreen(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        batch = Render.batch;

        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        // Generar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Crear y agregar la barra superior
        topBar = new TopBar(adminUsername, stage, font);

        // Crear el fondo gris translúcido para la tabla
        Pixmap pixmap = new Pixmap(1000, 500, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - 1000) / 2f, 100);
        stage.addActor(background);



        // Crear la tabla de jugadores
        Table playersTable = createPlayersTable(labelStyle);

        // Crear el ScrollPane para los jugadores
        ScrollPane scrollPane = new ScrollPane(playersTable);
        scrollPane.setSize(1000, 500);
        scrollPane.setPosition((Gdx.graphics.getWidth() - 1000) / 2f, 100);
        scrollPane.setScrollingDisabled(true, false); // Solo permitir desplazamiento vertical
        scrollPane.setFadeScrollBars(true); // Mostrar siempre las barras de desplazamiento

        // Añadir encabezados y ScrollPane al stage
        stage.addActor(scrollPane);
    }

    private Table createPlayersTable(Label.LabelStyle labelStyle) {
        Table table = new Table();
        table.top(); // Alineación inicial de la tabla

        // Estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.DARK_GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.GRAY, 0.7f)));

        // Obtener la lista de jugadores desde PlayerManager
        List<PlayerManager.PlayerData> players = PlayerManager.getAllPlayers();
        for (PlayerManager.PlayerData player : players) {
            table.add(new Label(player.getFirstname() + " " + player.getLastname(), labelStyle)).width(300).pad(10).center();
            table.add(new Label(player.getEmail(), labelStyle)).width(300).pad(10).center();

            // Botón de Editar
            TextButton editButton = new TextButton("Editar", buttonStyle);
            editButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showEditDialog(player);

                }
            });
            table.add(editButton).width(150).pad(10).center();

            // Botón de Historial
            TextButton historyButton = new TextButton("Historial", buttonStyle);
            historyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Render.app.setScreen(new HistorialScreen(player.getUsername())); // Cambia a la pantalla del historial
                }
            });
            table.add(historyButton).width(150).pad(10).center();
            table.row();
        }

        return table;
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fill();
        return pixmap;
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
        font.dispose();
    }


    private void showEditDialog(PlayerManager.PlayerData player) {
        // Crear un estilo para el diálogo
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 300, Color.VIOLET, 1.0f)));

        // Crear el diálogo
        Dialog editDialog = new Dialog("", dialogStyle);

        // Crear estilos para los campos de texto
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 50, Color.GRAY, 0.8f)));

        // Crear campos de texto con valores iniciales
        TextField usernameField = new TextField(PlayerManager.getUsername(player.getId()), textFieldStyle);
        TextField firstnameField = new TextField(player.getFirstname(), textFieldStyle);
        TextField lastnameField = new TextField(player.getLastname(), textFieldStyle);
        TextField emailField = new TextField(player.getEmail(), textFieldStyle);

        // Añadir campos al diálogo
        editDialog.getContentTable().add(new Label("Usuario:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        editDialog.getContentTable().add(usernameField).width(300).pad(5).row();
        editDialog.getContentTable().add(new Label("Nombre:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        editDialog.getContentTable().add(firstnameField).width(300).pad(5).row();
        editDialog.getContentTable().add(new Label("Apellido:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        editDialog.getContentTable().add(lastnameField).width(300).pad(5).row();
        editDialog.getContentTable().add(new Label("Correo:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        editDialog.getContentTable().add(emailField).width(300).pad(5).row();

        //Estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Etiqueta para mostrar mensajes de feedback
        feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(false);
        feedbackLabel.setAlignment(Align.center); // Centra el texto horizontalmente

        // Estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.DARK_GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.GRAY, 0.7f)));

        // Botón Cambiar Contraseña
        TextButton changePasswordButton = new TextButton("Cambiar Contraseña", buttonStyle);
        changePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showChangePasswordDialog(player);
                editDialog.hide();
                editDialog.remove();
                feedbackLabel.clear();

            }
        });
        editDialog.getContentTable().add(changePasswordButton).colspan(2).center().pad(10).row();

        //Boton Guardar
        TextButton saveButton = new TextButton("Guardar", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String usernameError = InputValidator.validateUsername(usernameField.getText());
                String firstnameError = InputValidator.validateFirstname(firstnameField.getText());
                String lastnameError = InputValidator.validateLastname(lastnameField.getText());
                String emailError = InputValidator.validateEmail(emailField.getText());

                if (usernameError != null || firstnameError != null || lastnameError != null || emailError != null) {
                    feedbackLabel.setText(usernameError != null ? usernameError :
                        firstnameError != null ? firstnameError :
                            lastnameError != null ? lastnameError : emailError);
                    feedbackLabel.setColor(Color.RED);
                } else {
                    boolean success = UserManager.updateProfile(
                        PlayerManager.getUsername(player.getId()),
                        usernameField.getText(),
                        PlayerManager.getPassword(player.getId()),
                        firstnameField.getText(),
                        lastnameField.getText(),
                        emailField.getText(),
                        player.getImage()
                    );

                    if (success) {
                        feedbackLabel.setText("Perfil actualizado con éxito.");
                        feedbackLabel.setColor(Color.GREEN);
                    } else {
                        feedbackLabel.setText("Error al actualizar el perfil.");
                        feedbackLabel.setColor(Color.RED);
                    }
                }
            }
        });
        editDialog.getButtonTable().add(saveButton).pad(10);

        // Botón de Cancelar
        TextButton cancelButton = new TextButton("Cerrar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editDialog.hide();
                editDialog.remove();
            }
        });
        editDialog.getButtonTable().add(cancelButton).pad(10);
        editDialog.getContentTable().add(feedbackLabel).colspan(2).center().pad(10).row();

        // Mostrar el diálogo
        editDialog.show(stage);

    }

    private void showChangePasswordDialog(PlayerManager.PlayerData player) {
        // Crear un estilo para el diálogo
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(500, 300, Color.PINK, 1.0f)));

        Dialog passwordDialog = new Dialog("", dialogStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 50, Color.LIGHT_GRAY, 0.8f)));

        TextField newPasswordField = new TextField("", textFieldStyle);
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        TextField confirmPasswordField = new TextField("", textFieldStyle);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');

        passwordDialog.getContentTable().add(new Label("Nueva Contraseña:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        passwordDialog.getContentTable().add(newPasswordField).width(300).pad(5).row();

        passwordDialog.getContentTable().add(new Label("Confirmar Contraseña:", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        passwordDialog.getContentTable().add(confirmPasswordField).width(300).pad(5).row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.DARK_GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 50, Color.GRAY, 0.7f)));

        //Boton guardar
        TextButton saveButton = new TextButton("Guardar", buttonStyle);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String passwordError = InputValidator.validatePassword(newPasswordField.getText());
                if (passwordError != null) {
                    feedbackLabel.setText(passwordError);
                    feedbackLabel.setColor(Color.RED);
                } else if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                    feedbackLabel.setText("Las contraseñas no coinciden.");
                    feedbackLabel.setColor(Color.RED);
                } else {
                    //Necesito la funcion updatePassword que esta en otra rama
                    //Ahora pone que las contraseñas no coinciden
//                    boolean success = UserManager.updatePassword(
//                        PlayerManager.getUsername(player.getId()),
//                        newPasswordField.getText()
//                    );

//                    if (success) {
//                        feedbackLabel.setText("Contraseña actualizada con éxito.");
//                        feedbackLabel.setColor(Color.GREEN);
//                    } else {
//                        feedbackLabel.setText("Error al actualizar la contraseña.");
//                        feedbackLabel.setColor(Color.RED);
//                    }

                }
            }
        });

        TextButton cancelButton = new TextButton("Cancelar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passwordDialog.hide();
                passwordDialog.remove();
                showEditDialog(player);
                feedbackLabel.remove();


            }
        });

        passwordDialog.getButtonTable().add(saveButton).pad(10);
        passwordDialog.getButtonTable().add(cancelButton).pad(10);
        passwordDialog.getContentTable().add(feedbackLabel).colspan(2).center().pad(10).row();

        passwordDialog.show(stage);
    }




}
