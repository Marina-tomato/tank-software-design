package ru.mipt.bit.platformer.model;

/** Domain policy applied by a tank while it occupies a given state. */
public interface TankState {
    float travelSpeed();
    boolean canFire();
    float shotIntervalSeconds();
}
