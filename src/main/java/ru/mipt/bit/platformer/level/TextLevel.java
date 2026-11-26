package ru.mipt.bit.platformer.level;

import java.util.HashSet;
import java.util.List;
import ru.mipt.bit.platformer.model.Cell;

/** LevelSource adapter for a rectangular text map. */
public final class TextLevel implements LevelSource {
    private final String text;

    public TextLevel(String text) {
        this.text = text;
    }

    public Layout load() {
        List<String> rows = text.lines().toList();
        if (rows.isEmpty()) throw new IllegalArgumentException("Empty level");
        int width = rows.get(0).length();
        var trees = new HashSet<Cell>();
        Cell player = null;
        for (int row = 0; row < rows.size(); row++) {
            String line = rows.get(row);
            if (line.length() != width) throw new IllegalArgumentException("Unequal row widths");
            for (int x = 0; x < width; x++) {
                Cell cell = new Cell(x, rows.size() - 1 - row);
                switch (line.charAt(x)) {
                    case 'T' -> trees.add(cell);
                    case 'X' -> {
                        if (player != null) throw new IllegalArgumentException("Multiple player starts");
                        player = cell;
                    }
                    case '_' -> { }
                    default -> throw new IllegalArgumentException("Unknown tile at " + cell);
                }
            }
        }
        if (player == null) throw new IllegalArgumentException("Missing player start");
        return new Layout(width, rows.size(), player, trees);
    }
}
