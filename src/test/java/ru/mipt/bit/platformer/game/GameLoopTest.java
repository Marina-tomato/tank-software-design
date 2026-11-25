package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {
    @Test void inputMovesTheModelBeforeItIsDrawn() {
        Tank player = new Tank(new Cell(0, 0), 1f);
        var field = new PlayingField(4, 4, List.of(new Tree(new Cell(2, 0))), player);
        var positions = new ArrayList<Cell>();
        var loop = new GameLoop(field, List.of((seconds, queue) -> queue.add(new Move(player, Direction.UP, field))),
                () -> positions.add(field.player().cell()));
        loop.frame(1f);
        assertEquals(List.of(new Cell(0, 1)), positions);
    }
}
