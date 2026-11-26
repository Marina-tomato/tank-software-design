package ru.mipt.bit.platformer.model;

/** Domain port for testing whether a destination is available to a moving tank. */
@FunctionalInterface
public interface Passability {
    boolean isFree(Cell cell);
}
