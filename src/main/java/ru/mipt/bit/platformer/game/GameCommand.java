package ru.mipt.bit.platformer.game;

@FunctionalInterface
public interface GameCommand {
    void execute();
}
