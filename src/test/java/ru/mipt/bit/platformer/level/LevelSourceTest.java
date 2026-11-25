package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Cell;
import java.util.Random;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class LevelSourceTest {
    @Test void readsTheTopRowAtTheHighestY() {
        Layout map = new TextLevel("T__\r\n_XT\r\n").load();
        assertEquals(3, map.width());
        assertEquals(2, map.height());
        assertEquals(new Cell(1, 0), map.player());
        assertEquals(Set.of(new Cell(0, 1), new Cell(2, 0)), map.trees());
    }

    @Test void rejectsAmbiguousOrIncompleteFiles() {
        for (String bad : new String[]{"", "_", "XX", "X?", "X_\n_", "X\n\n_"}) {
            assertThrows(IllegalArgumentException.class, () -> new TextLevel(bad).load(), bad);
        }
    }

    @Test void randomLevelCanFillAllButThePlayerTileWithoutRetrying() {
        Layout map = new RandomLevel(3, 4, 11, new Random(4)).load();
        assertEquals(11, map.trees().size());
        assertFalse(map.trees().contains(map.player()));
        assertEquals(map, new RandomLevel(3, 4, 11, new Random(4)).load());
        assertThrows(IllegalArgumentException.class, () -> new RandomLevel(2, 2, 4, new Random()));
    }
}
