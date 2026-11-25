package ru.mipt.bit.platformer.game;

import java.util.List;

@FunctionalInterface
public interface Controller {
    void collect(float seconds, List<GameCommand> commands);
}
