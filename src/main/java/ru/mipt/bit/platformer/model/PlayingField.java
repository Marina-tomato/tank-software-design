package ru.mipt.bit.platformer.model;

public final class PlayingField implements Passability {
    private final Tree tree;
    private final Tank player;

    public PlayingField(Tree tree, Tank player) {
        this.tree = tree;
        this.player = player;
    }

    public boolean isFree(Cell cell) {
        return !tree.cell().equals(cell);
    }

    public Tank player() { return player; }
    public Tree tree() { return tree; }
}
