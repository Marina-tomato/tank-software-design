package ru.mipt.bit.platformer.ai;

import ru.mipt.bit.platformer.game.RecommendationSource;
import ru.mipt.bit.platformer.model.Cell;

/** Translates the external advisor's vocabulary into application intentions. */
public final class AdvisorAdapter implements RecommendationSource {
    private final AimAdvisor advisor;

    public AdvisorAdapter(AimAdvisor advisor) {
        this.advisor = advisor;
    }

    public Intent recommend(Sight sight) {
        Cell facing = sight.facing().next(new Cell(0, 0));
        return switch (advisor.advise(sight.dx(), sight.dy(), facing.x(), facing.y(), sight.clear())) {
            case SHOOT -> Intent.FIRE;
            case NORTH -> Intent.MOVE_UP;
            case SOUTH -> Intent.MOVE_DOWN;
            case WEST -> Intent.MOVE_LEFT;
            case EAST -> Intent.MOVE_RIGHT;
            case WAIT -> Intent.NONE;
        };
    }
}
