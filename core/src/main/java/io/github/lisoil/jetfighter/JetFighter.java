package io.github.lisoil.jetfighter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class JetFighter extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); //default
        viewport = new FitViewport(16, 12);

        //font has 15pt, scaling to viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale((viewport.getWorldHeight() / Gdx.graphics.getHeight()) * 2);

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
