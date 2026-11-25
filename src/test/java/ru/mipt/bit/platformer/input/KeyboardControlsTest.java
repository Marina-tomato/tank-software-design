package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class KeyboardControlsTest {
    @Test void aliasesProduceOneActionAndBindingsKeepTheirOrder() {
        var keys = new KeyboardControls();
        var actions = new ArrayList<String>();
        keys.bind(1, 2, () -> actions.add("up"));
        keys.bind(3, 4, () -> actions.add("down"));
        keys.poll(key -> false);
        keys.poll(key -> key == 2);
        keys.poll(key -> true);
        assertEquals(List.of("up", "up", "down"), actions);
    }
}
