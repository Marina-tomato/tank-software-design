package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Tree extends GameObject {
    public Tree(TextureRegion graphics, GridPoint2 initialCoordinates, TiledMapTileLayer groundLayer) {
        super(graphics, initialCoordinates);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}