package ru.mipt.bit.platformer.model;

public final class Bullet implements Entity {
    private Cell cell;
    private final Direction direction;
    private final int damage;
    private final Tank owner;

    Bullet(Cell cell, Direction direction, int damage, Tank owner) {
        this.cell = cell;
        this.direction = direction;
        this.damage = damage;
        this.owner = owner;
    }

    public Cell cell() { return cell; }
    Cell next() { return direction.next(cell); }
    void advance() { cell = next(); }
    int damage() { return damage; }
    Tank owner() { return owner; }
}
