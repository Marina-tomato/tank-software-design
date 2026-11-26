package ru.mipt.bit.platformer.model;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameTimingTest {
    private Tank crossLineOfFire(float... frames) {
        var shooter = new Tank(new Cell(0, 0), 0.4f);
        var crossing = new Tank(new Cell(2, 0), 0.4f);
        var field = new PlayingField(4, 2, List.of(), shooter);
        field.addTank(crossing);
        crossing.move(Direction.UP, field);
        shooter.shoot(field, 40);
        for (float seconds : frames) field.update(seconds);
        assertTrue(field.bullets().isEmpty());
        return crossing;
    }

    @Test void crossingTankIsHitBeforeItCanVacateTheCell() {
        for (Tank tank : List.of(crossLineOfFire(0.4f), crossLineOfFire(0.12f, 0.28f))) {
            assertEquals(60, tank.health());
            assertEquals(new Cell(2, 0), tank.cell());
            assertEquals(0.65f, tank.progress(), 0.00001f);
        }
    }

    @Test void damageSlowsOnlyThePortionOfTheFrameAfterImpact() {
        for (Tank tank : List.of(crossLineOfFire(0.24f), crossLineOfFire(0.12f, 0.12f),
                                 crossLineOfFire(0.06f, 0.06f, 0.12f))) {
            assertEquals(60, tank.health());
            assertEquals(0.45f, tank.progress(), 0.00001f);
        }
    }

    @Test void cannotRegisterATankMovingTowardsAnOccupiedCell() {
        var player = new Tank(new Cell(1, 0), 1);
        var field = new PlayingField(3, 1, List.of(), player);
        var arriving = new Tank(new Cell(0, 0), 1);
        arriving.move(Direction.RIGHT, cell -> true);
        assertThrows(IllegalArgumentException.class, () -> field.addTank(arriving));
        assertEquals(List.of(player), field.tanks());
        assertTrue(field.isFree(arriving.cell()));
    }
}
