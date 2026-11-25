package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.PlayingField;

public final class GameLoop {
    @FunctionalInterface
    public interface Input { void poll(); }

    @FunctionalInterface
    public interface Display { void draw(PlayingField field); }

    private final PlayingField field;
    private final Input input;
    private final Display display;

    public GameLoop(PlayingField field, Input input, Display display) {
        this.field = field;
        this.input = input;
        this.display = display;
    }

    public void frame(float seconds) {
        input.poll();
        field.player().update(seconds);
        display.draw(field);
    }
}
