package ru.mipt.bit.platformer.model;

public enum DamageState implements TankState {
    LIGHT(1f, true), MEDIUM(0.5f, true), HEAVY(1f / 3f, false);

    private final float speed;
    private final boolean armed;

    DamageState(float speed, boolean armed) {
        this.speed = speed;
        this.armed = armed;
    }

    public static DamageState forHealth(double fraction) {
        if (fraction > 0.70) return LIGHT;
        if (fraction < 0.15) return HEAVY;
        return MEDIUM;
    }

    public float travelSpeed() { return speed; }
    public float shotIntervalSeconds() { return 0.4f; }
    public boolean canFire() { return armed; }
}
