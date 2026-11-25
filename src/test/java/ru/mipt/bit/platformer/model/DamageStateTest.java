package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DamageStateTest {
    @Test void thresholdsUseInitialHealthAndIncludeBothEndpointsInMedium() {
        // Initial HP 200 makes all four boundary cases exactly representable in HP.
        int[] health = {141, 140, 30, 29};
        float[] progress = {1, 0.5f, 0.5f, 1f / 3f};
        boolean[] canFire = {true, true, true, false};
        for (int i = 0; i < health.length; i++) {
            var tank = new Tank(new Cell(0, 0), 1, 200);
            tank.takeDamage(200 - health[i]);
            tank.move(Direction.RIGHT, cell -> true);
            tank.update(1);
            assertEquals(progress[i], tank.progress(), 0.00001f);
            var shots = new ArrayList<Bullet>();
            tank.shoot(shots::add, 1);
            assertEquals(canFire[i], !shots.isEmpty());
        }
    }

    @Test void damageDuringMovementChangesOnlyRemainingTravelTime() {
        var player = new Tank(new Cell(0, 0), 1);
        var field = new PlayingField(3, 1, List.of(), player);
        player.move(Direction.RIGHT, field);
        field.update(0.25f);
        player.takeDamage(40);
        field.update(1);
        assertEquals(0.75f, player.progress());
        player.takeDamage(50);
        player.shoot(field, 25);
        assertTrue(field.bullets().isEmpty());
        field.update(0.75f);
        assertEquals(new Cell(1, 0), player.cell());
    }
}
