package ru.mipt.bit.platformer.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.FieldListener;

/** Tracks the lifetime of the presentations supplied by the factory. */
public final class SpriteLayer implements FieldListener {
    private final Function<Entity, Runnable> factory;
    private final Map<Entity, Runnable> sprites = new LinkedHashMap<>();

    public SpriteLayer(Function<Entity, Runnable> factory) {
        this.factory = factory;
    }

    public void added(Entity entity) {
        sprites.put(entity, factory.apply(entity));
    }

    public void removed(Entity entity) {
        sprites.remove(entity);
    }

    public void draw() {
        sprites.values().forEach(Runnable::run);
    }
}
