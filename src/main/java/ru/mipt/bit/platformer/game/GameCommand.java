package ru.mipt.bit.platformer.game;

/** Input boundary for one requested game action. */
@FunctionalInterface
public interface GameCommand {
    void execute();
}
