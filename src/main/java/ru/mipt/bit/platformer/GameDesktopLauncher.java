package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Tank player;
    private Tree tree;

    // Текстуры
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Загрузка текстур
        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");

        // Создание объектов
        player = new Tank(new TextureRegion(blueTankTexture), new GridPoint2(1, 1));
        tree = new Tree(new TextureRegion(greenTreeTexture), new GridPoint2(1, 3), groundLayer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обработка ввода
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            player.move(Direction.UP, tileMovement);
        } else if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            player.move(Direction.DOWN, tileMovement);
        } else if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            player.move(Direction.LEFT, tileMovement);
        } else if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            player.move(Direction.RIGHT, tileMovement);
        }

        // Обновление состояния
        player.update(deltaTime);

        // Отрисовка
        levelRenderer.render();
        batch.begin();
        tree.render(batch);
        player.render(batch, tileMovement);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}