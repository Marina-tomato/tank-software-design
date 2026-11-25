package ru.mipt.bit.platformer.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import ru.mipt.bit.platformer.model.*;

public final class EnemySpawner {
    public static void populate(PlayingField field, Random random, int limit, float stepSeconds) {
        var free = new ArrayList<Cell>();
        for (int y = 0; y < field.height(); y++) {
            for (int x = 0; x < field.width(); x++) {
                var cell = new Cell(x, y);
                if (field.isFree(cell)) free.add(cell);
            }
        }
        int capacity = Math.min(limit, free.size());
        if (capacity <= 0) return;
        Collections.shuffle(free, random);
        int count = 1 + random.nextInt(capacity);
        for (int i = 0; i < count; i++) field.addTank(new Tank(free.get(i), stepSeconds));
    }

    private EnemySpawner() {}
}
