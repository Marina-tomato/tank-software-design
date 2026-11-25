package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class EnemySpawnerTest {
    @Test void uniqueEnemyStartsRespectCapacityAndObstacles() {
        for (int seed = 0; seed < 20; seed++) {
            var player = new Tank(new Cell(0, 0), 1);
            var tree = new Tree(new Cell(2, 1));
            var field = new PlayingField(3, 2, List.of(tree), player);
            EnemySpawner.populate(field, new Random(seed), 10, 1);
            assertTrue(field.tanks().size() >= 2);
            var occupied = new HashSet<Cell>();
            occupied.add(tree.cell());
            for (var tank : field.tanks()) assertTrue(occupied.add(tank.cell()));
            assertSame(player, field.player());
        }
    }

    @Test void fullMapAllowsPlayerOnly() {
        var field = new PlayingField(1, 1, List.of(), new Tank(new Cell(0, 0), 1));
        EnemySpawner.populate(field, new Random(1), 4, 1);
        assertEquals(List.of(field.player()), field.tanks());
    }
}
