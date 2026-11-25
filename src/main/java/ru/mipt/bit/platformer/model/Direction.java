package ru.mipt.bit.platformer.model;

public enum Direction {
    UP(0, 1, 90), LEFT(-1, 0, -180), DOWN(0, -1, -90), RIGHT(1, 0, 0);

    private final int dx;
    private final int dy;
    private final int rotation;

    Direction(int dx, int dy, int rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }

    public Cell next(Cell cell) {
        return new Cell(cell.x() + dx, cell.y() + dy);
    }

    public int rotation() {
        return rotation;
    }
}
