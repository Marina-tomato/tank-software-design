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
        var commands = new ArrayList<ru.mipt.bit.platformer.game.GameCommand>();
        keys.poll(key -> false, commands);
        keys.poll(key -> key == 2, commands);
        keys.poll(key -> true, commands);
        assertTrue(actions.isEmpty());
        commands.forEach(command -> command.execute());
        assertEquals(List.of("up", "up", "down"), actions);
    }
}
