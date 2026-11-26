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
        if (!field.player().isAlive()) {
            commands.add(randomCommand());
            return;
        }
        var intention = advisor.recommend(sight());
        commands.add(switch (intention) {
            case FIRE -> new Fire(tank, field, damage);
            case MOVE_UP -> new Move(tank, Direction.UP, field);
            case MOVE_DOWN -> new Move(tank, Direction.DOWN, field);
            case MOVE_LEFT -> new Move(tank, Direction.LEFT, field);
            case MOVE_RIGHT -> new Move(tank, Direction.RIGHT, field);
            case NONE -> randomCommand();
        });
    }

    private RecommendationSource.Sight sight() {
        Cell from = tank.cell();
        Cell target = field.player().cell();
        int dx = target.x() - from.x();
        int dy = target.y() - from.y();
        boolean clear = dx == 0 || dy == 0;
        for (int i = 1; clear && i < Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            var cell = new Cell(from.x() + i * Integer.signum(dx), from.y() + i * Integer.signum(dy));
            clear = field.isFree(cell);
        }
        return new RecommendationSource.Sight(dx, dy, tank.direction(), clear);
    }

    private GameCommand randomCommand() {
        var directions = Direction.values();
        int choice = random.nextInt(directions.length + 1);
        if (choice == directions.length) return new Fire(tank, field, damage);
        return new Move(tank, directions[choice], field);
    }
}
