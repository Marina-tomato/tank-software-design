package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Cell;
import ru.mipt.bit.platformer.model.PlayingField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public final class GameView implements Disposable {
    private final SpriteBatch batch = new SpriteBatch();
    private final TiledMap map = new TmxMapLoader().load("level.tmx");
    private final TiledMapTileLayer ground;
    private final MapRenderer background;
    private final TileMovement motion;
    private final Texture tankTexture = new Texture("images/tank_blue.png");
    private final Texture treeTexture = new Texture("images/greenTree.png");
    private final TextureRegion tankImage = new TextureRegion(tankTexture);
    private final TextureRegion treeImage = new TextureRegion(treeTexture);
    private final Rectangle tankBounds = createBoundingRectangle(tankImage);
    private final Rectangle treeBounds = createBoundingRectangle(treeImage);

    public GameView(int width, int height) {
        TiledMapTileLayer template = getSingleLayer(map);
        ground = new TiledMapTileLayer(width, height, (int) template.getTileWidth(), (int) template.getTileHeight());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ground.setCell(x, y, template.getCell(x % template.getWidth(), y % template.getHeight()));
            }
        }
        map.getLayers().remove(template);
        map.getLayers().add(ground);
        background = createSingleLayerMapRenderer(map, batch);
        motion = new TileMovement(ground, Interpolation.smooth);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, ground.getWidth() * ground.getTileWidth(),
                ground.getHeight() * ground.getTileHeight());
    }

    public void draw(PlayingField field) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        background.render();
        batch.begin();
        for (var tree : field.trees()) {
            moveRectangleAtTileCenter(ground, treeBounds, grid(tree.cell()));
            drawTextureRegionUnscaled(batch, treeImage, treeBounds, 0);
        }
        Tank tank = field.player();
        motion.moveRectangleBetweenTileCenters(tankBounds, grid(tank.cell()), grid(tank.destination()), tank.progress());
        drawTextureRegionUnscaled(batch, tankImage, tankBounds, tank.direction().rotation());
        batch.end();
    }

    private static GridPoint2 grid(Cell cell) {
        return new GridPoint2(cell.x(), cell.y());
    }

    @Override
    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        map.dispose();
        batch.dispose();
    }
}
