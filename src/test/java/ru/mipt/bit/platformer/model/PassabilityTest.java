package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PassabilityTest {
    @Test void movementAcceptsAnIndependentTerrainRule() {
        var tank = new Tank(new Cell(0, 0), 2f);
        tank.move(Direction.RIGHT, cell -> cell.y() > 0);
        assertFalse(tank.isMoving());
        tank.move(Direction.UP, cell -> cell.y() > 0);
        tank.update(1f);
        assertEquals(0.5f, tank.progress());
    }

    @Test void aStepMustHaveAPositiveFiniteDuration() {
        for (float duration : new float[]{0, -1, Float.NaN, Float.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> new Tank(new Cell(0, 0), duration));
        }
    }
}
