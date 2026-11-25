package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import java.util.List;
import ru.mipt.bit.platformer.game.*;
import ru.mipt.bit.platformer.model.*;

import static com.badlogic.gdx.Input.Keys.*;

/** Input adapter translating held movement keys and individual key presses into commands. */
public final class DesktopKeyboard implements Controller {
    private final KeyboardControls movement = new KeyboardControls();
    private final Fire fire;
    private final ToggleHealthBars healthBars;

    public DesktopKeyboard(PlayingField field, int damage, ToggleHealthBars healthBars) {
        Tank player = field.player();
        movement.bind(UP, W, new Move(player, Direction.UP, field));
        movement.bind(LEFT, A, new Move(player, Direction.LEFT, field));
        movement.bind(DOWN, S, new Move(player, Direction.DOWN, field));
        movement.bind(RIGHT, D, new Move(player, Direction.RIGHT, field));
        fire = new Fire(player, field, damage);
        this.healthBars = healthBars;
    }

    public void collect(float seconds, List<GameCommand> commands) {
        movement.poll(Gdx.input::isKeyPressed, commands);
        if (Gdx.input.isKeyJustPressed(SPACE)) commands.add(fire);
        if (Gdx.input.isKeyJustPressed(L)) commands.add(healthBars);
    }
}
