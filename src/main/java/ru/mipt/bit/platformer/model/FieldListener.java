package ru.mipt.bit.platformer.model;

/** Output port for replaying the initial entities and publishing subsequent additions and removals. */
public interface FieldListener {
    void added(Entity entity);
    void removed(Entity entity);
}
