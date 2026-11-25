package ru.mipt.bit.platformer.model;

public final class Tank implements Entity {
    private int health = 100;
    private Cell cell;
    private Cell destination;
    private Direction direction = Direction.RIGHT;
    private float progress = 1f;

    private final float stepSeconds;

    public Tank(Cell cell, float stepSeconds) {
        if (!Float.isFinite(stepSeconds) || stepSeconds <= 0f) {
            throw new IllegalArgumentException("Invalid step duration");
        }
        this.stepSeconds = stepSeconds;
        this.cell = cell;
        this.destination = cell;
    }

    public void move(Direction requested, Passability field) {
        if (!isAlive() || isMoving()) return;
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
        progress = Math.min(1f, progress + seconds / stepSeconds);
        if (!isMoving()) cell = destination;
    }

    public void shoot(PlayingField field, int damage) {
        if (damage <= 0) throw new IllegalArgumentException("Damage must be positive");
        if (isAlive()) field.spawn(new Bullet(direction.next(cell), direction, damage, this));
    }

    void takeDamage(int damage) { health = Math.max(0, health - damage); }
    public boolean isAlive() { return health > 0; }
    public int health() { return health; }

    public Cell cell() { return cell; }
    public Cell destination() { return destination; }
    public Direction direction() { return direction; }
    public float progress() { return progress; }
    public boolean isMoving() { return progress < 1f; }
}
