package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.PlayingField;
import ru.mipt.bit.platformer.model.Tank;

public record Fire(Tank tank, PlayingField field, int damage) implements GameCommand {
    public void execute() {
        tank.shoot(field, damage);
    }
}
