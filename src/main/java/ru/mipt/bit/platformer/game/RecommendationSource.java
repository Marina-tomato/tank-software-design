package ru.mipt.bit.platformer.game;

import java.util.Optional;
import ru.mipt.bit.platformer.model.PlayingField;
import ru.mipt.bit.platformer.model.Tank;

/** Output port through which an AI controller obtains an optional game command. */
@FunctionalInterface
public interface RecommendationSource {
    Optional<GameCommand> recommend(Tank tank, PlayingField field, int damage);
}
