package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank extends GameObject {
    private static final float MOVEMENT_SPEED = 0.4f;

    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(TextureRegion graphics, GridPoint2 initialCoordinates) {
        super(graphics, initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.rotation = 0f;
    }

    public float getRotation() { return rotation; }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public void move(Direction direction, TileMovement tileMovement) {
        if (isMoving()) return;

        GridPoint2 nextCoords = direction.apply(coordinates);
        destinationCoordinates.set(nextCoords);
        movementProgress = 0f;
        rotation = direction.getRotation();
    }

    public void render(Batch batch, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}