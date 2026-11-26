package ru.mipt.bit.platformer.game;

import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.model.PlayingField;

public final class GameLoop {
    /** Output port that presents one frame after the model advances. */
    @FunctionalInterface
    public interface Display { void draw(); }

    private final PlayingField field;
    private final List<Controller> controllers;
    private final Display display;

    public GameLoop(PlayingField field, List<Controller> controllers, Display display) {
        this.field = field;
        this.controllers = List.copyOf(controllers);
        this.display = display;
    }

    public void frame(float seconds) {
        var commands = new ArrayList<GameCommand>();
        for (Controller controller : controllers) controller.collect(seconds, commands);
        for (GameCommand command : commands) command.execute();
        field.update(seconds);
        display.draw();
    }
}
