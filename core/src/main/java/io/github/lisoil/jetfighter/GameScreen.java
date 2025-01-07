package io.github.lisoil.jetfighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;


public class GameScreen implements Screen {
    final JetFighter game;

    Texture bucketTexture;

    Texture jetBlackTexture;
    Texture jetWhiteTexture;

    Sprite jetBlackSprite;
    Sprite jetWhiteSprite;

    Texture bulletBlackTexture;
    Texture bulletWhiteTexture;
    Array<Sprite> bulletBlackSprites;
    Array<Sprite> bulletWhiteSprites;

    float bulletTimer;

    Rectangle jetBlackRectangle;
    Rectangle jetWhiteRectangle;

    Rectangle bulletBlackRectangle;
    Rectangle bulletWhiteRectangle;

    int blackHits;
    int whiteHits;

    public GameScreen(final JetFighter game) {
        this.game = game;

        bucketTexture = new Texture("bucket.png");

        jetBlackTexture = new Texture("jet_black.PNG");
        jetWhiteTexture = new Texture("jet_white.PNG");

        float worldWidth = game.viewport.getWorldWidth();

        jetBlackSprite = new Sprite(jetBlackTexture);
        jetBlackSprite.setSize(1, 1);
        jetBlackSprite.setOrigin(0.5f,1);

        jetBlackSprite.setPosition(1, 0);

        jetWhiteSprite = new Sprite(jetWhiteTexture);
        jetWhiteSprite.setSize(1, 1);
        jetWhiteSprite.setOrigin(0.5f,1);

        jetWhiteSprite.setPosition(worldWidth - 2, 0);

        bulletBlackTexture = new Texture("bullet_black.PNG");
        bulletWhiteTexture = new Texture("bullet_white.PNG");
        bulletBlackSprites = new Array<>();
        bulletWhiteSprites = new Array<>();

        jetBlackRectangle = new Rectangle();
        jetWhiteRectangle = new Rectangle();

        bulletBlackRectangle = new Rectangle();
        bulletWhiteRectangle = new Rectangle();

    }

    @Override
    public void show() {
        //music.play();
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 5f;
        float rotationSpeed = 180f;
        float delta = Gdx.graphics.getDeltaTime();

        // Convert angle to radians for trigonometric functions
        float angleWhiteRad = (float) Math.toRadians(jetWhiteSprite.getRotation());

        float directionWhiteX = -(float) Math.sin(angleWhiteRad);
        float directionWhiteY = (float) Math.cos(angleWhiteRad); // Y movement corresponds to the upward direction

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            jetWhiteSprite.rotate(rotationSpeed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            jetWhiteSprite.rotate(-rotationSpeed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            jetWhiteSprite.translate(directionWhiteX * speed * delta, directionWhiteY * speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            jetWhiteSprite.translate(-directionWhiteX * speed * delta, -directionWhiteY * speed * delta);
        }

        float angleBlackRad = (float) Math.toRadians(jetBlackSprite.getRotation());

        float directionBlackX = -(float) Math.sin(angleBlackRad);
        float directionBlackY = (float) Math.cos(angleBlackRad);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            jetBlackSprite.rotate(rotationSpeed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            jetBlackSprite.rotate(-rotationSpeed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            jetBlackSprite.translate(directionBlackX * speed * delta, directionBlackY * speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            jetBlackSprite.translate(-directionBlackX * speed * delta, -directionBlackY * speed * delta);
        }

        //If there's a more efficient way of doing that I can't be bothered.
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float jetWidth = jetBlackSprite.getWidth();
        float jetHeight = jetBlackSprite.getHeight();

        // applying jet positions and sizes to jetRectangles
        jetBlackRectangle.set(jetBlackSprite.getX(), jetBlackSprite.getY(), jetWidth, jetHeight);
        jetWhiteRectangle.set(jetWhiteSprite.getX(), jetWhiteSprite.getY(), jetWidth, jetHeight);

        // stopping out of bounds (it's good enough I can't be bothered to do math)
        jetBlackSprite.setX(MathUtils.clamp(jetBlackSprite.getX(), -jetWidth, worldWidth - (jetWidth / 2))); // don't ask why it's (jetWidth / 2) it just is and yes I wrote that myself
        jetWhiteSprite.setX(MathUtils.clamp(jetWhiteSprite.getX(), -jetWidth, worldWidth - (jetWidth / 2)));

        jetBlackSprite.setY(MathUtils.clamp(jetBlackSprite.getY(), -jetHeight, worldHeight - jetHeight));
        jetWhiteSprite.setY(MathUtils.clamp(jetWhiteSprite.getY(), -jetHeight, worldHeight - jetHeight));

        float delta = Gdx.graphics.getDeltaTime();
        float bulletSpeed = 3f;

        for (int i = bulletBlackSprites.size - 1; i >= 0; i--) {
            Sprite bulletBlackSprite = bulletBlackSprites.get(i); //Get sprite from list
            float bulletWidth = bulletBlackSprite.getWidth();
            float bulletHeight = bulletBlackSprite.getHeight();

            bulletBlackSprite.translateY(-bulletSpeed * delta);

            // applying bullet position and size to bulletRectangle
            bulletBlackRectangle.set(bulletBlackSprite.getX(), bulletBlackSprite.getY(), bulletWidth, bulletHeight);

            // if top of bullet goes out of view remove
            if (bulletBlackSprite.getY() < -bulletHeight) bulletBlackSprites.removeIndex(i);
            else if (bulletBlackSprite.getX() < -bulletWidth) bulletBlackSprites.removeIndex(i);
            // if collision between black bullets and white jet
            else if (jetWhiteRectangle.overlaps(bulletBlackRectangle)) {
                blackHits++;
                bulletBlackSprites.removeIndex(i);
            }
        }

        for (int i = bulletWhiteSprites.size - 1; i >= 0; i--) {
            Sprite bulletWhiteSprite = bulletWhiteSprites.get(i);//Get sprite from list
            float bulletWidth = bulletWhiteSprite.getWidth();
            float bulletHeight = bulletWhiteSprite.getHeight();

            bulletWhiteSprite.translateY(-bulletSpeed * delta);

            bulletWhiteRectangle.set(bulletWhiteSprite.getX(), bulletWhiteSprite.getY(), bulletWidth, bulletHeight);

            // if top of bullet goes out of view remove
            if (bulletWhiteSprite.getY() < -bulletHeight) bulletWhiteSprites.removeIndex(i);
            else if (bulletWhiteSprite.getX() < -bulletWidth) bulletWhiteSprites.removeIndex(i);
            // if collision between white bullets and black jet
            else if (jetBlackRectangle.overlaps(bulletWhiteRectangle)) {
                whiteHits++;
                bulletWhiteSprites.removeIndex(i);
            }
        }

        bulletTimer += delta; // slows bullet spawn rate
        if (bulletTimer > 1f) {
            bulletTimer = 0;
            createBullet();
        }

    }

    private void draw() {
        ScreenUtils.clear(Color.GRAY);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

//        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw  background (don't have one atm)
        jetBlackSprite.draw(game.batch);
        jetWhiteSprite.draw(game.batch);

        game.font.draw(game.batch, "" + blackHits, 1, worldHeight - 1);
        game.font.draw(game.batch, "" + whiteHits, worldWidth - 1 - game.font.getSpaceXadvance(), worldHeight - 1);

        for (Sprite bulletBlackSprite : bulletBlackSprites) {
            bulletBlackSprite.draw(game.batch); //temp
        }

        for (Sprite bulletWhiteSprite : bulletWhiteSprites) {
            bulletWhiteSprite.draw(game.batch); //temp
        }

        game.batch.end();
    }

    private void createBullet() {
        float bulletWidth = 0.15f;
        float bulletHeight = 0.225f;
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        Sprite bulletBlackSprite = new Sprite(bulletBlackTexture);
        Sprite bulletWhiteSprite = new Sprite(bulletWhiteTexture);

        bulletBlackSprite.setSize(bulletWidth, bulletHeight);
        bulletWhiteSprite.setSize(bulletWidth, bulletHeight);

        bulletBlackSprite.setX(MathUtils.random(0f, worldWidth - bulletWidth));
        bulletBlackSprite.setY(worldHeight);

        bulletWhiteSprite.setX(MathUtils.random(0f, worldWidth - bulletWidth));
        bulletWhiteSprite.setY(worldHeight);

        bulletBlackSprites.add(bulletBlackSprite);
        bulletWhiteSprites.add(bulletWhiteSprite);

    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        // Invoked when application is paused.
    }

    @Override
    public void resume() {
        // Invoked when application is resumed after pause.
    }

    @Override
    public void dispose() {
        //backgroundTexture.dispose();
        jetBlackTexture.dispose();
        jetWhiteTexture.dispose();
        bulletBlackTexture.dispose();
        bulletWhiteTexture.dispose();
    }
}
