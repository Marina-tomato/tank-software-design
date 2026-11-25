package ru.mipt.bit.platformer.model;

import java.util.List;

public final class PlayingField implements Passability {
    private final int width;
    private final int height;
    private final List<Tree> trees;
    private final Tank player;

    public PlayingField(int width, int height, List<Tree> trees, Tank player) {
        this.width = width;
        this.height = height;
        this.trees = List.copyOf(trees);
        this.player = player;
    }

    public boolean isFree(Cell cell) {
        return trees.stream().noneMatch(tree -> tree.cell().equals(cell));
    }

    public int width() { return width; }
    public int height() { return height; }
    public Tank player() { return player; }
    public List<Tree> trees() { return trees; }
}
