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
        var loop = new GameLoop(field, () -> player.move(Direction.UP, field),
                frame -> positions.add(frame.player().cell()));
        loop.frame(1f);
        assertEquals(List.of(new Cell(0, 1)), positions);
    }
}
