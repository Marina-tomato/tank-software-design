package ru.mipt.bit.platformer.model;

public final class Tank {
    private Cell cell;
    private Cell destination;
    private Direction direction = Direction.RIGHT;
    private float progress = 1f;

    public Tank(Cell cell) {
        this.cell = cell;
        this.destination = cell;
    }

    public void move(Direction requested, PlayingField field) {
        if (isMoving()) return;
        direction = requested;
        Cell next = direction.next(cell);
        if (field.isFree(next)) {
            destination = next;
            progress = 0f;
        }
    }

    public void update(float seconds) {
        if (!Float.isFinite(seconds) || seconds < 0f) {
            throw new IllegalArgumentException("Invalid frame duration");
        }
        progress = Math.min(1f, progress + seconds / 0.4f);
        if (!isMoving()) cell = destination;
    }

    public Cell cell() { return cell; }
    public Cell destination() { return destination; }
    public Direction direction() { return direction; }
    public float progress() { return progress; }
    public boolean isMoving() { return progress < 1f; }
}
