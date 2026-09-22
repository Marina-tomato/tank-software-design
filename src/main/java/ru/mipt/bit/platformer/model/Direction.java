package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public enum Direction {
    UP(90f) {
        @Override public GridPoint2 apply(GridPoint2 coords) { return incrementedY(coords); }
    },
    DOWN(-90f) {
        @Override public GridPoint2 apply(GridPoint2 coords) { return decrementedY(coords); }
    },
    LEFT(-180f) {
        @Override public GridPoint2 apply(GridPoint2 coords) { return decrementedX(coords); }
    },
    RIGHT(0f) {
        @Override public GridPoint2 apply(GridPoint2 coords) { return incrementedX(coords); }
    };

    private final float rotation;
    Direction(float rotation) { this.rotation = rotation; }
    public float getRotation() { return rotation; }
    public abstract GridPoint2 apply(GridPoint2 coords);
}