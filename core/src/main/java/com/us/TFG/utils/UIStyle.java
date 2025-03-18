package com.us.TFG.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class UIStyle {
    private static BitmapFont font;
    private static BitmapFont titleFont;
    private static Skin skin;

    static {
        // Cargar la fuente
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Caveat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 34;
        font = generator.generateFont(parameter);
        FreeTypeFontGenerator.FreeTypeFontParameter titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParameter.size = 40;
        titleFont = generator.generateFont(titleParameter);
        generator.dispose();

        // Crear y configurar el Skin
        skin = new Skin();
        skin.add("Caveat", font);
    }

    public static Label.LabelStyle getLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        return labelStyle;
    }

    public static Label.LabelStyle getTitleLabelStyle() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = titleFont;
        titleFont.getData().setScale(1.2f);
        titleLabelStyle.fontColor = Color.WHITE;
        return titleLabelStyle;
    }

    public static TextButton.TextButtonStyle getButtonStyle() {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.LIGHT_GRAY, 0.4f)));
        buttonStyle.down = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.GRAY, 0.4f)));
        buttonStyle.over = new TextureRegionDrawable(new Texture(createRoundedPixmap(350, 80, Color.DARK_GRAY, 0.4f)));
        return buttonStyle;
    }
    public static void showError(Label feedbackLabel, String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setColor(Color.RED);
    }



    public static TextureRegionDrawable getBackgroundDrawable() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.6f, 0.6f, 0.6f, 0.4f));
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        return backgroundDrawable;
    }

    public static TextField.TextFieldStyle getTextFieldStyle() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(new Texture(createRoundedPixmap(400, 70, Color.DARK_GRAY, 0.5f)));
        return textFieldStyle;
    }

    public static Pixmap createRoundedPixmap(int width, int height, Color color, float transparency) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        pixmap.setColor(new Color(color.r, color.g, color.b, transparency));
        pixmap.fillRectangle(0, 20, width, height - 40);
        return pixmap;
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static Skin getSkin() {
        return skin;
    }

}
