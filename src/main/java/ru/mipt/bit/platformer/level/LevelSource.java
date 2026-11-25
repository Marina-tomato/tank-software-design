package ru.mipt.bit.platformer.level;

/** Output port supplying a complete level layout at startup. */
@FunctionalInterface
public interface LevelSource {
    Layout load();
}
