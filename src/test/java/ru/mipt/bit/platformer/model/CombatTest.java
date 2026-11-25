package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {
    private final Tank player = new Tank(new Cell(0, 0), 1);
    private final Tree tree = new Tree(new Cell(1, 1));
    private final PlayingField field = new PlayingField(7, 2, List.of(tree), player);

    @Test void adjacentHitDealsConfiguredDamageAndDestroysAtZero() {
        Tank enemy = new Tank(new Cell(1, 0), 1);
        field.addTank(enemy);
        player.shoot(field, 35);
        assertEquals(65, enemy.health());
        assertTrue(field.bullets().isEmpty());
        player.shoot(field, 65);
        assertFalse(field.tanks().contains(enemy));
        enemy.shoot(field, 50);
        assertTrue(field.bullets().isEmpty());
    }

    @Test void observerReceivesInitialAndDynamicObjectsOnce() {
        var added = new ArrayList<Entity>();
        var removed = new ArrayList<Entity>();
        var listener = new FieldListener() {
            public void added(Entity e) { added.add(e); }
            public void removed(Entity e) { removed.add(e); }
        };
        field.subscribe(listener);
        assertEquals(List.of(tree, player), added);
        player.shoot(field, 15);
        Bullet shot = field.bullets().get(0);
        assertEquals(new Cell(1, 0), shot.cell());
        assertSame(shot, added.get(2));
        field.update(0.12f);
        assertEquals(new Cell(2, 0), shot.cell());
        field.update(2);
        assertEquals(List.of(shot), removed);
        field.unsubscribe(listener);
        player.shoot(field, 15);
        assertEquals(3, added.size());
    }

    @Test void bulletsHitTreesAndCannotLeaveTheField() {
        var other = new Tank(new Cell(0, 1), 1);
        field.addTank(other);
        other.shoot(field, 1);
        player.move(Direction.LEFT, field);
        player.shoot(field, 1);
        assertTrue(field.bullets().isEmpty());
        assertTrue(field.trees().contains(tree));
    }

    @Test void oppositeShotsCollideAtSpawnInOneCellOrCrossingAnEdge() {
        for (int distance : new int[]{2, 3, 4}) {
            var left = new Tank(new Cell(0, 0), 1);
            var right = new Tank(new Cell(distance, 0), 1);
            var arena = new PlayingField(6, 1, List.of(), left);
            arena.addTank(right);
            right.move(Direction.LEFT, cell -> false);
            left.shoot(arena, 10);
            right.shoot(arena, 10);
            arena.update(0.12f);
            assertTrue(arena.bullets().isEmpty());
            assertEquals(100, left.health());
            assertEquals(100, right.health());
        }
    }

    @Test void longFrameStillHitsIntermediateTank() {
        var enemy = new Tank(new Cell(4, 0), 1);
        field.addTank(enemy);
        player.shoot(field, 20);
        field.update(2);
        assertEquals(80, enemy.health());
        assertTrue(field.bullets().isEmpty());
    }

    @Test void movingIntoAShotHitsTheReservingTank() {
        var enemy = new Tank(new Cell(2, 0), 1);
        field.addTank(enemy);
        player.shoot(field, 10);
        enemy.move(Direction.LEFT, field);
        field.update(0);
        assertEquals(90, enemy.health());
        assertTrue(field.bullets().isEmpty());
    }
}
