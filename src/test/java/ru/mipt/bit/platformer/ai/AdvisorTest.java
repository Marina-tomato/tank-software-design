package ru.mipt.bit.platformer.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.*;
import ru.mipt.bit.platformer.model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AdvisorTest {
    private final AdvisorAdapter adapter = new AdvisorAdapter(new AimAdvisor());

    @Test void adviceTurnsTowardPlayerThenFiresOnClearLane() {
        var player = new Tank(new Cell(0, 0), 1);
        var enemy = new Tank(new Cell(3, 0), 1);
        var field = new PlayingField(4, 1, List.of(), player);
        field.addTank(enemy);
        var turn = adapter.recommend(enemy, field, 31).orElseThrow();
        assertInstanceOf(Move.class, turn);
        turn.execute();
        field.update(1);
        var fire = adapter.recommend(enemy, field, 31).orElseThrow();
        assertInstanceOf(Fire.class, fire);
        fire.execute();
        field.update(1);
        assertEquals(69, player.health());
    }

    @Test void obstacleOrDiagonalLeavesDecisionToRandomDriver() {
        var player = new Tank(new Cell(0, 0), 1);
        var field = new PlayingField(4, 2, List.of(new Tree(new Cell(1, 0))), player);
        assertTrue(adapter.recommend(new Tank(new Cell(2, 0), 1), field, 1).isEmpty());
        assertTrue(adapter.recommend(new Tank(new Cell(2, 1), 1), field, 1).isEmpty());
    }
}
