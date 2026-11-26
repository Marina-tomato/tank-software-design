package ru.mipt.bit.platformer.view;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Cell;
import ru.mipt.bit.platformer.model.Entity;
import static org.junit.jupiter.api.Assertions.*;

class SpriteLayerTest {
    @Test void factoryHandlesANewEntityWithoutChangingTheSubscriber() {
        record Marker(Cell cell) implements Entity {}
        var first = new Marker(new Cell(1, 0));
        var second = new Marker(new Cell(2, 1));
        var created = new ArrayList<Entity>();
        var drawn = new ArrayList<Entity>();
        var layer = new SpriteLayer(entity -> {
            created.add(entity);
            return () -> drawn.add(entity);
        });
        layer.added(first);
        layer.added(second);
        layer.draw();
        layer.removed(first);
        layer.draw();
        assertEquals(List.of(first, second), created);
        assertEquals(List.of(first, second, second), drawn);
    }
}
