package ru.mipt.bit.platformer.model;

/** Domain port used by a tank to register a newly fired projectile in its level. */
@FunctionalInterface
public interface ProjectileSink {
    void spawn(Bullet bullet);
}
