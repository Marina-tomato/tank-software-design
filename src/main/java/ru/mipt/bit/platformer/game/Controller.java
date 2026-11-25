package ru.mipt.bit.platformer.game;

import java.util.List;

/** Input port for collecting player or AI commands for the next frame. */
@FunctionalInterface
public interface Controller {
    void collect(float seconds, List<GameCommand> commands);
}
