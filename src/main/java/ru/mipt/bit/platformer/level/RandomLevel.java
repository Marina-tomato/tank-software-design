package ru.mipt.bit.platformer.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import ru.mipt.bit.platformer.model.Cell;

public final class RandomLevel implements LevelSource {
    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevel(int width, int height, int treeCount, Random random) {
        if (width <= 0 || height <= 0 || treeCount < 0 || treeCount >= Math.multiplyExact(width, height)) {
            throw new IllegalArgumentException("Reserve at least one free tile for the player");
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    public Layout load() {
        var cells = new ArrayList<Cell>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) cells.add(new Cell(x, y));
        }
        Collections.shuffle(cells, random);
        return new Layout(width, height, cells.get(0), new HashSet<>(cells.subList(1, treeCount + 1)));
    }
}
