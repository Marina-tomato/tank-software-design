package ru.mipt.bit.platformer.level;

import java.util.Set;
import ru.mipt.bit.platformer.model.Cell;

public record Layout(int width, int height, Cell player, Set<Cell> trees) {
    public Layout {
        trees = Set.copyOf(trees);
        if (width <= 0 || height <= 0 || !inside(player, width, height)
                || trees.contains(player) || trees.stream().anyMatch(cell -> !inside(cell, width, height))) {
            throw new IllegalArgumentException("Invalid level layout");
        }
    }

    private static boolean inside(Cell cell, int width, int height) {
        return cell.x() >= 0 && cell.y() >= 0 && cell.x() < width && cell.y() < height;
    }
}
