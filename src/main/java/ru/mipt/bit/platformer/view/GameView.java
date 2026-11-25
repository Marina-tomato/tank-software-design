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
import com.badlogic.gdx.graphics.Pixmap;
import java.util.LinkedHashMap;
import java.util.Map;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public final class GameView implements Disposable, FieldListener {
    private final Map<Entity, Runnable> sprites = new LinkedHashMap<>();
    private final Tank player;
    private final Texture pixel;
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

    public GameView(int width, int height, Tank player) {
        this.player = player;
        Pixmap image = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        image.setColor(1, 1, 1, 1);
        image.fill();
        pixel = new Texture(image);
        image.dispose();
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
        sprites.values().forEach(Runnable::run);
        batch.end();
    }

    @Override
    public void added(Entity entity) {
        if (entity instanceof Tree tree) {
            sprites.put(tree, () -> {
                moveRectangleAtTileCenter(ground, treeBounds, grid(tree.cell()));
                drawTextureRegionUnscaled(batch, treeImage, treeBounds, 0);
            });
        } else if (entity instanceof Tank tank) {
            sprites.put(tank, () -> drawTank(tank));
        } else if (entity instanceof Bullet bullet) {
            sprites.put(bullet, () -> {
                float x = (bullet.cell().x() + 0.5f) * ground.getTileWidth();
                float y = (bullet.cell().y() + 0.5f) * ground.getTileHeight();
                batch.setColor(1, 0.85f, 0.15f, 1);
                batch.draw(pixel, x - 8, y - 8, 16, 16);
                batch.setColor(1, 1, 1, 1);
            });
        }
    }

    private void drawTank(Tank tank) {
        if (tank != player) batch.setColor(1f, 0.45f, 0.4f, 1f);
        motion.moveRectangleBetweenTileCenters(tankBounds, grid(tank.cell()), grid(tank.destination()), tank.progress());
        drawTextureRegionUnscaled(batch, tankImage, tankBounds, tank.direction().rotation());
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public void removed(Entity entity) {
        sprites.remove(entity);
    }

    private static GridPoint2 grid(Cell cell) {
        return new GridPoint2(cell.x(), cell.y());
    }

    @Override
    public void dispose() {
        pixel.dispose();
        tankTexture.dispose();
        treeTexture.dispose();
        map.dispose();
        batch.dispose();
    }
}
