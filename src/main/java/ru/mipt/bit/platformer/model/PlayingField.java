package ru.mipt.bit.platformer.model;

import java.util.*;

public final class PlayingField implements Passability, ProjectileSink {
    private static final float BULLET_STEP_SECONDS = 0.12f;
    private final int width;
    private final int height;
    private final List<Tree> trees;
    private final Tank player;
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<FieldListener> listeners = new ArrayList<>();
    private double bulletTime;

    public PlayingField(int width, int height, List<Tree> trees, Tank player) {
        this.width = width;
        this.height = height;
        this.trees = List.copyOf(trees);
        this.player = player;
        addTank(player);
    }

    public void subscribe(FieldListener listener) {
        listeners.add(listener);
        trees.forEach(listener::added);
        tanks.forEach(listener::added);
        bullets.forEach(listener::added);
    }

    public void unsubscribe(FieldListener listener) {
        listeners.remove(listener);
    }

    public boolean isFree(Cell cell) {
        return !isTerrainBlocked(cell) && tankAt(cell, null) == null;
    }

    private boolean isTerrainBlocked(Cell cell) {
        return cell.x() < 0 || cell.x() >= width || cell.y() < 0 || cell.y() >= height
                || trees.stream().anyMatch(tree -> tree.cell().equals(cell));
    }

    private Tank tankAt(Cell cell, Tank except) {
        for (Tank tank : tanks) {
            if (tank != except && (tank.cell().equals(cell) || tank.destination().equals(cell))) return tank;
        }
        return null;
    }

    public void addTank(Tank tank) {
        if (tank.isMoving()) throw new IllegalArgumentException("A tank must be stationary at spawn");
        if (!isFree(tank.cell())) throw new IllegalArgumentException("Occupied spawn: " + tank.cell());
        tanks.add(tank);
        listeners.forEach(listener -> listener.added(tank));
    }

    @Override
    public void spawn(Bullet bullet) {
        if (!tanks.contains(bullet.owner())) return;
        for (Bullet other : List.copyOf(bullets)) {
            if (other.cell().equals(bullet.cell())) {
                remove(other);
                return;
            }
        }
        if (hitsTerrainOrTank(bullet, bullet.cell())) return;
        bullets.add(bullet);
        listeners.forEach(listener -> listener.added(bullet));
    }

    private boolean hitsTerrainOrTank(Bullet bullet, Cell cell) {
        if (isTerrainBlocked(cell)) return true;
        Tank hit = tankAt(cell, bullet.owner());
        if (hit == null) return false;
        hit.takeDamage(bullet.damage());
        if (!hit.isAlive()) {
            tanks.remove(hit);
            listeners.forEach(listener -> listener.removed(hit));
        }
        return true;
    }

    private void remove(Bullet bullet) {
        bullets.remove(bullet);
        listeners.forEach(listener -> listener.removed(bullet));
    }

    private void stepBullets() {
        // Resolve pairs from a single snapshot, including shots crossing the same edge.
        var collisions = new HashSet<Bullet>();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet a = bullets.get(i);
            for (int j = i + 1; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                if (a.next().equals(b.next()) ||
                        (a.next().equals(b.cell()) && b.next().equals(a.cell()))) {
                    collisions.add(a);
                    collisions.add(b);
                }
            }
        }
        for (Bullet bullet : List.copyOf(bullets)) {
            if (collisions.contains(bullet) || hitsTerrainOrTank(bullet, bullet.next())) remove(bullet);
            else bullet.advance();
        }
    }

    public void update(float seconds) {
        if (!Float.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("Invalid frame duration");
        // A tank may have just reserved a cell already containing a shot.
        for (Bullet bullet : List.copyOf(bullets)) {
            if (hitsTerrainOrTank(bullet, bullet.cell())) remove(bullet);
        }
        double remaining = seconds;
        while (!bullets.isEmpty() && remaining >= BULLET_STEP_SECONDS - bulletTime) {
            double untilImpact = BULLET_STEP_SECONDS - bulletTime;
            advanceTanks(untilImpact);
            remaining -= untilImpact;
            bulletTime = 0;
            stepBullets();
        }
        advanceTanks(remaining);
        bulletTime = bullets.isEmpty() ? 0 : bulletTime + remaining;
    }

    private void advanceTanks(double seconds) {
        for (Tank tank : tanks) tank.update(seconds);
    }

    public int width() { return width; }
    public int height() { return height; }
    public Tank player() { return player; }
    public List<Tree> trees() { return trees; }
    public List<Tank> tanks() { return Collections.unmodifiableList(tanks); }
    public List<Bullet> bullets() { return Collections.unmodifiableList(bullets); }
}
