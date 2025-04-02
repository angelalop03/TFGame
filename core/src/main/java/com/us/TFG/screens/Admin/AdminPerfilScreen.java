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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.us.TFG.elements.Imagen;
import com.us.TFG.screens.General.LoginScreen;
import com.us.TFG.screens.General.OpenScreen;
import com.us.TFG.screens.Player.ChangePasswordScreen;
import com.us.TFG.utils.*;

public class AdminPerfilScreen implements Screen {
    private String username;
    private Imagen fondo;
    private SpriteBatch b;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private TopBar topBar;

    public AdminPerfilScreen(String username) {
        this.username = username;
    }

    @Override
    public void show() {
        // Cargar el fondo
        fondo = new Imagen(Resources.FONDON);
        fondo.setSize(Config.WIDTH, Config.HEIGHT);
        b = Render.batch;
        // Crear Stage y Skin
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = UIStyle.getFont();
        topBar = new TopBar(username, stage, font);
        skin = new Skin();
        // Añadir la fuente generada al skin
        skin.add("Caveat", font);


        // Crear el cuadro gris translúcido
        Pixmap pixmap = new Pixmap(700, 200, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f)); // Gris claro con transparencia
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - 700) / 2, 230);
        stage.addActor(background);

        // Crear la tabla para mostrar la información
        Table table = new Table();
        table.setFillParent(true);

        Label usernameLabel = new Label("Usuario: " + username, UIStyle.getLabelStyle());
        table.add(usernameLabel).padBottom(10);
        table.row();

        // Botón para editar perfil
        TextButton changepasswordButton = new TextButton("Cambiar Contraseña", UIStyle.getButtonStyle());
        changepasswordButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Render.app.setScreen(new ChangePasswordScreen(username));
            }

        });
        table.add(changepasswordButton).size(250, 60).padBottom(10);
        table.row();

        // Botón para cerrar sesión
        TextButton logOutButton = new TextButton("Cerrar Sesión", UIStyle.getButtonStyle());
        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                showLogoutDialog();
            }
        });
        table.add(logOutButton).size(250, 60).padBottom(10);

        // Ajustar la posición de la tabla sobre el cuadro gris
        table.setPosition(0, -50);
        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        b.begin();
        fondo.draw();
        b.end();

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


    private void showLogoutDialog() {
        // Crear estilo del diálogo
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.titleFont = font;
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.background = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(500, 300, Color.PINK, 1f)));

        Dialog logoutDialog = new Dialog("", dialogStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(150, 80, Color.GRAY, 0.7f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(UIStyle.createRoundedPixmap(150, 80, Color.LIGHT_GRAY, 0.7f)));

        // Etiqueta de confirmación
        Label confirmLabel = new Label("¿Estás seguro de que deseas cerrar sesión?", labelStyle);

        // Botón de confirmar
        TextButton confirmButton = new TextButton("Confirmar", buttonStyle);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Render.app.setScreen(new OpenScreen());
            }
        });

        // Botón de cancelar
        TextButton cancelButton = new TextButton("Cancelar", buttonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logoutDialog.hide();
            }
        });

        logoutDialog.getContentTable().add(confirmLabel).padBottom(10).row();
        logoutDialog.getButtonTable().add(confirmButton).pad(10);
        logoutDialog.getButtonTable().add(cancelButton).pad(10);

        logoutDialog.show(stage);
    }

}
