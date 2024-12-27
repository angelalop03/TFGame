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

import java.sql.*;

public class PerfilScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private int pointsM;
    private int pointsR;
    private SpriteBatch batch;
    private Imagen fondo;
    private BitmapFont font;
    private TopBar topBar;
    private String username;




    public PerfilScreen(String username) {

        this.username = username;

        // Conectar y obtener la información del jugador directamente en PerfilScreen
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:game.db")) {
            String sql = "SELECT p.firstname, p.lastname, p.email, p.image, p.pointsM, p.pointsR " +
                "FROM players p JOIN users u ON p.user_id = u.id " +
                "WHERE u.username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                firstname = resultSet.getString("firstname");
                lastname = resultSet.getString("lastname");
                email = resultSet.getString("email");
                image = resultSet.getString("image");
                pointsM = resultSet.getInt("pointsM");
                pointsR = resultSet.getInt("pointsR");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();

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
        skin.add("Caveat", font);

        // Crear el cuadro grisáceo transparente
        Pixmap pixmap = new Pixmap(700, 600, Pixmap.Format.RGBA8888); // Tamaño del cuadro
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));  // Gris claro con 60% de transparencia
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Image background = new Image(backgroundDrawable);
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, 100); // Centramos el fondo en la pantalla
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

                Render.app.setScreen(new EditProfileScreen( username ));
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
