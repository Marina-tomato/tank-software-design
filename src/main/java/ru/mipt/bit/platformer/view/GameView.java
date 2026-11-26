package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import java.util.function.BooleanSupplier;
import ru.mipt.bit.platformer.game.GameLoop;
import ru.mipt.bit.platformer.model.FieldListener;
import ru.mipt.bit.platformer.model.Tank;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public final class GameView implements Disposable, GameLoop.Display {
    private final SpriteBatch batch = new SpriteBatch();
    private final TiledMap map = new TmxMapLoader().load("level.tmx");
    private final MapRenderer background;
    private final SpriteFactory factory;
    private final SpriteLayer sprites;

    public GameView(int width, int height, Tank player, BooleanSupplier healthBars) {
        TiledMapTileLayer template = getSingleLayer(map);
        var ground = new TiledMapTileLayer(width, height, (int) template.getTileWidth(), (int) template.getTileHeight());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ground.setCell(x, y, template.getCell(x % template.getWidth(), y % template.getHeight()));
            }
        }
        map.getLayers().remove(template);
        map.getLayers().add(ground);
        background = createSingleLayerMapRenderer(map, batch);
        factory = new SpriteFactory(batch, ground, player, healthBars);
        sprites = new SpriteLayer(factory::create);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, ground.getWidth() * ground.getTileWidth(),
                ground.getHeight() * ground.getTileHeight());
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        background.render();
        batch.begin();
        sprites.draw();
        batch.end();
    }

    public FieldListener entities() {
        return sprites;
    }

    @Override
    public void dispose() {
        factory.dispose();
        map.dispose();
        batch.dispose();
    }
}
