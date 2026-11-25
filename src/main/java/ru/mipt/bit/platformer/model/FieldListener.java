package ru.mipt.bit.platformer.model;

public interface FieldListener {
    void added(Entity entity);
    void removed(Entity entity);
}
