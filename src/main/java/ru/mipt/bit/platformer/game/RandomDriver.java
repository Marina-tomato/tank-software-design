package ru.mipt.bit.platformer.game;

import java.util.List;
import java.util.Random;
import ru.mipt.bit.platformer.model.*;

/** Controller implementation that proposes AI commands for one tank. */
public final class RandomDriver implements Controller {
    private final Tank tank;
    private final PlayingField field;
    private final Random random;
    private float untilDecision;
    private final RecommendationSource advisor;
    private final int damage;

    public RandomDriver(Tank tank, PlayingField field, Random random, RecommendationSource advisor, int damage) {
        this.tank = tank;
        this.field = field;
        this.random = random;
        this.advisor = advisor;
        this.damage = damage;
    }

    public void collect(float seconds, List<GameCommand> commands) {
        untilDecision -= seconds;
        if (!tank.isAlive() || tank.isMoving() || untilDecision > 0) return;
        untilDecision = 0.4f;
        commands.add(advisor.recommend(tank, field, damage).orElseGet(this::randomCommand));
    }

    private GameCommand randomCommand() {
        var directions = Direction.values();
        int choice = random.nextInt(directions.length + 1);
        if (choice == directions.length) return new Fire(tank, field, damage);
        return new Move(tank, directions[choice], field);
    }
}
