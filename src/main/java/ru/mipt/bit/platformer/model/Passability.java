package ru.mipt.bit.platformer.model;

@FunctionalInterface
public interface Passability {
    boolean isFree(Cell cell);
}
