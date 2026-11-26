package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.Direction;

/** Output port for obtaining an intention from an immutable view of the target. */
@FunctionalInterface
public interface RecommendationSource {
    record Sight(int dx, int dy, Direction facing, boolean clear) {}
    enum Intent { MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, FIRE, NONE }

    Intent recommend(Sight sight);
}
