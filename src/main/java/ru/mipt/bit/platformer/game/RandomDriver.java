package ru.mipt.bit.platformer.game;

import java.util.List;
import java.util.Random;
import ru.mipt.bit.platformer.model.*;

public final class RandomDriver implements Controller {
    private final Tank tank;
    private final PlayingField field;
    private final Random random;
    private float untilDecision;

    public RandomDriver(Tank tank, PlayingField field, Random random) {
        this.tank = tank;
        this.field = field;
        this.random = random;
    }

    public void collect(float seconds, List<GameCommand> commands) {
        untilDecision -= seconds;
        if (tank.isMoving() || untilDecision > 0) return;
        untilDecision = 0.4f;
        var directions = Direction.values();
        commands.add(new Move(tank, directions[random.nextInt(directions.length)], field));
    }
}
