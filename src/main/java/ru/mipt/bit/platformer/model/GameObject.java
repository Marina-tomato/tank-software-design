package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public abstract class GameObject {
    protected TextureRegion graphics;
    protected Rectangle rectangle;
    protected GridPoint2 coordinates;

    public GameObject(TextureRegion graphics, GridPoint2 initialCoordinates) {
        this.graphics = graphics;
        this.rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(initialCoordinates);
    }

    public TextureRegion getGraphics() { return graphics; }
    public Rectangle getRectangle() { return rectangle; }
    public GridPoint2 getCoordinates() { return coordinates; }

    public void setCoordinates(GridPoint2 newCoordinates) {
        this.coordinates.set(newCoordinates);
    }
}