package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovementTest {
    private final Cell start = new Cell(1, 1);
    private final Tank tank = new Tank(start);
    private final PlayingField field = new PlayingField(new Tree(new Cell(1, 2)), tank);

    @Test void directionDefinesOffsetAndRotation() {
        Cell[] cells = {new Cell(1, 2), new Cell(0, 1), new Cell(1, 0), new Cell(2, 1)};
        int[] angles = {90, -180, -90, 0};
        for (Direction d : Direction.values()) {
            assertEquals(cells[d.ordinal()], d.next(start));
            assertEquals(angles[d.ordinal()], d.rotation());
        }
    }

    @Test void treeBlocksTravelButAllowsTurning() {
        assertSame(tank, field.player());
        assertFalse(field.isFree(field.tree().cell()));
        tank.move(Direction.UP, field);
        assertEquals(Direction.UP, tank.direction());
        assertFalse(tank.isMoving());
        assertEquals(start, tank.destination());
    }

    @Test void moveFinishesAfterItsFullDuration() {
        tank.move(Direction.LEFT, field);
        tank.update(0.2f);
        assertEquals(start, tank.cell());
        assertEquals(0.5f, tank.progress(), 0.0001f);
        tank.move(Direction.DOWN, field);
        assertEquals(Direction.LEFT, tank.direction());
        tank.update(5f);
        assertEquals(new Cell(0, 1), tank.cell());
        assertFalse(tank.isMoving());
    }

    @Test void invalidTimeCannotCorruptProgress() {
        assertThrows(IllegalArgumentException.class, () -> tank.update(-1));
        assertThrows(IllegalArgumentException.class, () -> tank.update(Float.NaN));
    }
}
