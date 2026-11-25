package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class PlayingField {
    private final Tree tree;

    public PlayingField(Tree tree) {
        this.tree = tree;
    }

    public boolean isFree(GridPoint2 cell) {
        return !tree.getCoordinates().equals(cell);
    }
}
