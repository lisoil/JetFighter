package io.github.lisoil.jetfighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndMenuScreen implements Screen {

    final JetFighter game;
    String winner;
    int blackPoints;
    int whitePoints;

    public EndMenuScreen(final JetFighter game, String winner, int blackPoints, int whitePoints) {
        this.game = game;
        this.winner = winner;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.font.draw(game.batch, "Created by Liya", 1, 6f);

        game.font.draw(game.batch, winner + " jet has won", 1, 4f);

        game.font.draw(game.batch, "Black: " + blackPoints, 1, 3f);
        game.font.draw(game.batch, "White: " + whitePoints, 1, 2f);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
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

    }
}
