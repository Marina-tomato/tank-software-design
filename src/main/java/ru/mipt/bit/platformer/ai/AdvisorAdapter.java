package ru.mipt.bit.platformer.ai;

import java.util.Optional;
import ru.mipt.bit.platformer.game.*;
import ru.mipt.bit.platformer.model.*;

/** Output adapter translating grid advice into commands understood by the game. */
public final class AdvisorAdapter implements RecommendationSource {
    private final AimAdvisor advisor;

    public AdvisorAdapter(AimAdvisor advisor) {
        this.advisor = advisor;
    }

    public Optional<GameCommand> recommend(Tank tank, PlayingField field, int damage) {
        if (!field.player().isAlive()) return Optional.empty();
        Cell from = tank.cell();
        Cell target = field.player().cell();
        int dx = target.x() - from.x();
        int dy = target.y() - from.y();
        boolean clear = dx == 0 || dy == 0;
        if (clear) {
            for (int i = 1; i < Math.max(Math.abs(dx), Math.abs(dy)); i++) {
                if (!field.isFree(new Cell(from.x() + i * Integer.signum(dx), from.y() + i * Integer.signum(dy)))) {
                    clear = false;
                    break;
                }
            }
        }
        Cell facing = tank.direction().next(new Cell(0, 0));
        var advice = advisor.advise(dx, dy, facing.x(), facing.y(), clear);
        return switch (advice) {
            case SHOOT -> Optional.of(new Fire(tank, field, damage));
            case NORTH -> Optional.of(new Move(tank, Direction.UP, field));
            case SOUTH -> Optional.of(new Move(tank, Direction.DOWN, field));
            case WEST -> Optional.of(new Move(tank, Direction.LEFT, field));
            case EAST -> Optional.of(new Move(tank, Direction.RIGHT, field));
            case WAIT -> Optional.empty();
        };
    }
}
