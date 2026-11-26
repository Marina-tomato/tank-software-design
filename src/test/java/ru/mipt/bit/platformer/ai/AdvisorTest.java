package ru.mipt.bit.platformer.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.*;
import ru.mipt.bit.platformer.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdvisorTest {
    private final AdvisorAdapter adapter = new AdvisorAdapter(new AimAdvisor());

    @Test void adviceTurnsTowardPlayerThenFiresOnClearLane() {
        var player = new Tank(new Cell(0, 0), 1);
        var enemy = new Tank(new Cell(3, 0), 1);
        var field = new PlayingField(4, 1, List.of(), player);
        field.addTank(enemy);
        var driver = new RandomDriver(enemy, field, new Random(1), adapter, 31);
        var queue = new ArrayList<GameCommand>();
        driver.collect(0, queue);
        assertFalse(enemy.isMoving());
        assertInstanceOf(Move.class, queue.get(0)).execute();
        field.update(1);
        queue.clear();
        driver.collect(1, queue);
        assertTrue(field.bullets().isEmpty());
        assertInstanceOf(Fire.class, queue.get(0)).execute();
        field.update(1);
        assertEquals(69, player.health());
    }

    @Test void obstacleAndDiagonalProduceNoRecommendation() {
        var player = new Tank(new Cell(0, 0), 1);
        var field = new PlayingField(4, 2, List.of(new Tree(new Cell(1, 0))), player);
        for (Cell cell : List.of(new Cell(2, 0), new Cell(2, 1))) {
            var observed = new ArrayList<RecommendationSource.Sight>();
            var enemy = new Tank(cell, 1);
            var driver = new RandomDriver(enemy, field, new Random(1), sight -> {
                observed.add(sight);
                return adapter.recommend(sight);
            }, 1);
            driver.collect(0, new ArrayList<>());
            var sight = observed.get(0);
            assertFalse(sight.clear());
            assertEquals(RecommendationSource.Intent.NONE, adapter.recommend(sight));
        }
    }
}
