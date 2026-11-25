package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class TrafficTest {
    private final Tank player = new Tank(new Cell(1, 0), 1);
    private final PlayingField field = new PlayingField(3, 2, List.of(), player);

    @Test void bothCellsStayReservedUntilArrival() {
        var enemy = new Tank(new Cell(0, 0), 1);
        field.addTank(enemy);
        new Move(player, Direction.RIGHT, field).execute();
        field.update(0.5f);
        assertFalse(field.isFree(new Cell(1, 0)));
        assertFalse(field.isFree(new Cell(2, 0)));
        new Move(enemy, Direction.RIGHT, field).execute();
        assertFalse(enemy.isMoving());
        field.update(0.5f);
        assertTrue(field.isFree(new Cell(1, 0)));
        assertFalse(field.isFree(new Cell(2, 0)));
    }

    @Test void boundaryAndConflictingSpawnAreBlocked() {
        player.move(Direction.DOWN, field);
        assertFalse(player.isMoving());
        assertFalse(field.isFree(new Cell(3, 1)));
        assertFalse(field.isFree(new Cell(-1, 1)));
        assertFalse(field.isFree(new Cell(1, 2)));
        assertThrows(IllegalArgumentException.class, () -> field.addTank(new Tank(player.cell(), 1)));
    }

    @Test void randomDriverQueuesAnActionAndWaitsBetweenDecisions() {
        var driver = new RandomDriver(player, field, new Random(2),
                new ru.mipt.bit.platformer.ai.AdvisorAdapter(new ru.mipt.bit.platformer.ai.AimAdvisor()), 25);
        var queue = new ArrayList<GameCommand>();
        driver.collect(0, queue);
        driver.collect(0.1f, queue);
        assertEquals(1, queue.size());
        assertInstanceOf(Move.class, queue.get(0));
        assertFalse(player.isMoving());
    }
}
