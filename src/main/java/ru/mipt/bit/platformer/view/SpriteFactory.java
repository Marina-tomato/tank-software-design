package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import java.util.function.BooleanSupplier;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public final class SpriteFactory implements Disposable {
    private final SpriteBatch batch;
    private final TiledMapTileLayer ground;
    private final TileMovement motion;
    private final Tank player;
    private final BooleanSupplier healthBars;
    private final Texture pixel;
    private final Texture tankTexture = new Texture("images/tank_blue.png");
    private final Texture treeTexture = new Texture("images/greenTree.png");
    private final TextureRegion tankImage = new TextureRegion(tankTexture);
    private final TextureRegion treeImage = new TextureRegion(treeTexture);

    public SpriteFactory(SpriteBatch batch, TiledMapTileLayer ground, Tank player, BooleanSupplier healthBars) {
        this.batch = batch;
        this.ground = ground;
        this.motion = new TileMovement(ground, Interpolation.smooth);
        this.player = player;
        this.healthBars = healthBars;
        Pixmap image = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        image.setColor(1, 1, 1, 1);
        image.fill();
        pixel = new Texture(image);
        image.dispose();
    }

    public Runnable create(Entity entity) {
        if (entity instanceof Tree tree) {
            Rectangle bounds = createBoundingRectangle(treeImage);
            moveRectangleAtTileCenter(ground, bounds, grid(tree.cell()));
            return () -> drawTextureRegionUnscaled(batch, treeImage, bounds, 0);
        }
        if (entity instanceof Tank tank) {
            Rectangle bounds = createBoundingRectangle(tankImage);
            var sprite = new HealthBarDecorator(rect -> drawTank(tank, rect), healthBars,
                    tank::healthFraction, this::drawHealthBar);
            return () -> {
                motion.moveRectangleBetweenTileCenters(bounds, grid(tank.cell()), grid(tank.destination()), tank.progress());
                sprite.accept(bounds);
            };
        }
        if (entity instanceof Bullet bullet) return () -> drawBullet(bullet);
        throw new IllegalArgumentException("No sprite for " + entity.getClass().getName());
    }

    private void drawBullet(Bullet bullet) {
        float x = (bullet.cell().x() + 0.5f) * ground.getTileWidth();
        float y = (bullet.cell().y() + 0.5f) * ground.getTileHeight();
        batch.setColor(1, 0.85f, 0.15f, 1);
        batch.draw(pixel, x - 8, y - 8, 16, 16);
        batch.setColor(1, 1, 1, 1);
    }

    private void drawTank(Tank tank, Rectangle bounds) {
        if (tank != player) batch.setColor(1f, 0.45f, 0.4f, 1f);
        drawTextureRegionUnscaled(batch, tankImage, bounds, tank.direction().rotation());
        batch.setColor(1, 1, 1, 1);
    }

    private void drawHealthBar(Rectangle bounds, double fraction) {
        float width = ground.getTileWidth() * 0.68f;
        float x = bounds.x + (bounds.width - width) / 2f;
        float y = bounds.y + bounds.height * 0.84f;
        batch.setColor(0.15f, 0.1f, 0.1f, 1f);
        batch.draw(pixel, x, y, width, 8);
        batch.setColor(0.25f, 0.9f, 0.3f, 1f);
        batch.draw(pixel, x + 1, y + 1, (width - 2) * (float) fraction, 6);
        batch.setColor(1, 1, 1, 1);
    }

    private static GridPoint2 grid(Cell cell) {
        return new GridPoint2(cell.x(), cell.y());
    }

    public void dispose() {
        pixel.dispose();
        tankTexture.dispose();
        treeTexture.dispose();
    }
}
