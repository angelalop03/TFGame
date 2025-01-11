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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.manager.PlayerManager;
import com.us.TFG.manager.UserManager;
import com.us.TFG.screens.General.LogOutScreen;
import com.us.TFG.screens.General.OpenScreen;
import com.us.TFG.utils.Config;
import com.us.TFG.utils.Render;
import com.us.TFG.utils.Resources;
import com.us.TFG.utils.TopBar;


public class PlayerPerfilScreen implements Screen {

    private Stage stage;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private int pointsM;
    private int pointsR;
    private SpriteBatch batch;
    private Imagen fondo;
    private BitmapFont font;
    private Skin skin;
    private TopBar topBar;
    private String username;




    public PlayerPerfilScreen(String username) {

        this.username = username;

        // Obtener datos del jugador desde PlayerManager
        PlayerManager.PlayerData playerData = PlayerManager.getPlayerData(username);
        if (playerData != null) {
            this.firstname = playerData.getFirstname();
            this.lastname = playerData.getLastname();
            this.email = playerData.getEmail();
            this.image = playerData.getImage();
            this.pointsM = playerData.getPointsM();
            this.pointsR = playerData.getPointsR();
        } else {
            // Manejar caso en el que no se encuentren datos del jugador
            System.err.println("No se encontraron datos para el jugador: " + username);
        }
    }

    @Override
    public void show() {
        //Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
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

        // Añadir la fuente generada al skin
        skin.add("Caveat", font);

        // Crear el cuadro grisáceo transparente
        Pixmap pixmap = new Pixmap(700, 750, Pixmap.Format.RGBA8888); // Tamaño del cuadro
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));  // Gris claro con 60% de transparencia
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, 65); // Centramos el fondo en la pantalla
        stage.addActor(background);

        // Crear estilo para Label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Crear y agregar la barra superior

        topBar = new TopBar(username, stage, font);


        // Crear estilo para TextButton
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.DARK_GRAY, 0.4f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 100, Color.GRAY, 0.4f)));
        buttonStyle.font = font;

        skin.add("default", buttonStyle);

        Table table = new Table();
        table.setFillParent(true);

        // Mostrar la imagen del jugador
        Texture profileImage = new Texture(Gdx.files.internal("assets/photos/players/" + image));
        Image playerImage = new Image(profileImage);
        table.add(playerImage).size(200, 200).padBottom(20);
        table.row();

        // Mostrar el nombre completo del jugador
        Label nameLabel = new Label("Nombre: " + firstname + " " + lastname, skin);
        table.add(nameLabel).padBottom(10);
        table.row();

        // Mostrar el correo electrónico
        Label emailLabel = new Label("Email: " + email, skin);
        table.add(emailLabel).padBottom(10);
        table.row();

        // Mostrar los puntos de métrica
        Label pointsMLabel = new Label("Puntos Mental: " + pointsM, skin);
        table.add(pointsMLabel).padBottom(10);
        table.row();

        Label pointsRLabel = new Label("Puntos Relacion: " + pointsR, skin);
        table.add(pointsRLabel).padBottom(10);
        table.row();

        // Botón de editar perfil
        TextButton editProfileButton = new TextButton("Editar Perfil", buttonStyle);
        editProfileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Render.app.setScreen(new EditProfileScreen(username));
            }
        });
        table.add(editProfileButton).size(200, 50).padBottom(10);
        table.row();

        // Botón de cerrar sesión
        TextButton logOutButton = new TextButton("Cerrar Sesión", buttonStyle);
        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Render.app.setScreen(new LogOutScreen(username));
            }
        });
        table.add(logOutButton).size(200, 50).padBottom(10);

        // Botón para borrar el perfil
        TextButton deleteProfileButton = new TextButton("Borrar Perfil", buttonStyle);
        deleteProfileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showDeleteProfileDialog();
            }
        });
        table.row();
        table.add(deleteProfileButton).size(200, 50).padBottom(10);



        // Agregar la tabla al stage
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
    private void showDeleteProfileDialog() {
        // Crear estilo del diálogo
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(500, 300, Color.VIOLET, 1f)));

        Dialog deleteDialog = new Dialog("", dialogStyle);


        // Crear estilos para  campos de texto
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 80, Color.DARK_GRAY, 0.8f)));

        // Crear campo para ingresar contraseña
        TextField passwordField = new TextField("", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');


        //Estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Etiqueta para mostrar mensajes de feedback
        Label feedbackLabel = new Label("", labelStyle);
        feedbackLabel.setWrap(false);
        feedbackLabel.setAlignment(Align.center);


        // Estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 80, Color.DARK_GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(150, 80, Color.GRAY, 0.7f)));

        // Botón de confirmar
        TextButton confirmButton = new TextButton("Confirmar", buttonStyle);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String password = passwordField.getText().trim();

                if (UserManager.verifyLogin(username, password)) {
                    if (UserManager.deleteUser(username)) {
                        feedbackLabel.setText("Perfil eliminado con éxito.");
                        feedbackLabel.setColor(Color.GREEN);

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                Render.app.setScreen(new OpenScreen()); // Redirigir a la pantalla OpenScreen
                            }
                        }, 3);                    }
                    else {
                        feedbackLabel.setText("Error al eliminar el perfil.");
                        feedbackLabel.setColor(Color.RED);
                    }
                } else {
                    feedbackLabel.setText("Contraseña incorrecta.");
                    feedbackLabel.setColor(Color.RED);
                }

            }
        });

        // Botón de cancelar
        TextButton cancelButton = new TextButton("Cerrar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteDialog.hide();
            }
        });
        deleteDialog.getContentTable().add(new Label("¿Estás seguro de borrar tu perfil?", labelStyle)).padBottom(5).padTop(25).row();
        deleteDialog.getContentTable().add(new Label("Introduce tu contraseña:", labelStyle)).padBottom(5).row();
        //deleteDialog.getContentTable().add(new Label("Contraseña", new Label.LabelStyle(font, Color.WHITE))).pad(5);
        deleteDialog.getContentTable().add(passwordField).width(300).pad(5).padBottom(5).row();
        deleteDialog.getContentTable().add(feedbackLabel).colspan(2).center().pad(10).row();
        deleteDialog.getButtonTable().add(confirmButton).padBottom(10);
        deleteDialog.getButtonTable().add(cancelButton).padBottom(10);

        deleteDialog.show(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fillRectangle(0, 20, width, height - 40);
        return pixmap;
    }


}
