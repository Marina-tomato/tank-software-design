package ru.mipt.bit.platformer.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class PlayingField implements Passability {
    private final int width;
    private final int height;
    private final List<Tree> trees;
    private final Tank player;
    private final List<Tank> tanks = new ArrayList<>();

    public PlayingField(int width, int height, List<Tree> trees, Tank player) {
        this.width = width;
        this.height = height;
        this.trees = List.copyOf(trees);
        this.player = player;
        addTank(player);
    }

    public boolean isFree(Cell cell) {
        return cell.x() >= 0 && cell.x() < width && cell.y() >= 0 && cell.y() < height
                && trees.stream().noneMatch(tree -> tree.cell().equals(cell))
                && tanks.stream().noneMatch(tank -> tank.cell().equals(cell) || tank.destination().equals(cell));
    }

    public void addTank(Tank tank) {
        if (!isFree(tank.cell())) throw new IllegalArgumentException("Occupied spawn: " + tank.cell());
        tanks.add(tank);
    }

    public List<Tank> tanks() { return Collections.unmodifiableList(tanks); }

    public void update(float seconds) {
        for (Tank tank : tanks) tank.update(seconds);
    }

    public int width() { return width; }
    public int height() { return height; }
    public Tank player() { return player; }
    public List<Tree> trees() { return trees; }
}
