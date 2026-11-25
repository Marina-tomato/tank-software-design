package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Passability;
import ru.mipt.bit.platformer.model.Tank;

public record Move(Tank tank, Direction direction, Passability field) implements GameCommand {
    public void execute() {
        tank.move(direction, field);
    }
}
