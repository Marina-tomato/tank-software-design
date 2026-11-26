package ru.mipt.bit.platformer.model;

public final class Tank implements Entity {
    private final int maxHealth;
    private int health;
    private TankState temporaryState;
    private double secondsSinceShot = Double.POSITIVE_INFINITY;
    private Cell cell;
    private Cell destination;
    private Direction direction = Direction.RIGHT;
    private double progress = 1f;

    private final float stepSeconds;

    public Tank(Cell cell, float stepSeconds) {
        this(cell, stepSeconds, 100);
    }

    public Tank(Cell cell, float stepSeconds, int maxHealth) {
        if (maxHealth <= 0) throw new IllegalArgumentException("Initial health must be positive");
        this.maxHealth = maxHealth;
        this.health = maxHealth;
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

    public void update(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0f) {
            throw new IllegalArgumentException("Invalid frame duration");
        }
        secondsSinceShot += seconds;
        progress = Math.min(1.0, progress + seconds * activeState().travelSpeed() / stepSeconds);
        if (!isMoving()) cell = destination;
    }

    public void shoot(ProjectileSink field, int damage) {
        if (damage <= 0) throw new IllegalArgumentException("Damage must be positive");
        TankState state = activeState();
        if (!isAlive() || !state.canFire() || secondsSinceShot < state.shotIntervalSeconds()) return;
        secondsSinceShot = 0;
        field.spawn(new Bullet(direction.next(cell), direction, damage, this));
    }

    void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public void applyTemporaryState(TankState state) {
        temporaryState = java.util.Objects.requireNonNull(state);
    }

    public void clearTemporaryState() {
        temporaryState = null;
    }

    private TankState activeState() {
        return temporaryState != null ? temporaryState : DamageState.forHealth(healthFraction());
    }

    public boolean isAlive() { return health > 0; }
    public double healthFraction() { return (double) health / maxHealth; }
    public int health() { return health; }

    public Cell cell() { return cell; }
    public Cell destination() { return destination; }
    public Direction direction() { return direction; }
    public float progress() { return (float) progress; }
    public boolean isMoving() { return progress < 1f; }
}
