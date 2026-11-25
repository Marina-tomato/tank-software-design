package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import ru.mipt.bit.platformer.game.GameCommand;
import java.util.List;
import java.util.function.IntPredicate;

public final class KeyboardControls {
    private record Binding(int key, int alternate, GameCommand action) {}
    private final List<Binding> bindings = new ArrayList<>();

    public void bind(int key, int alternate, GameCommand action) {
        bindings.add(new Binding(key, alternate, action));
    }

    public void poll(IntPredicate pressed, List<GameCommand> commands) {
        for (Binding binding : bindings) {
            if (pressed.test(binding.key()) || pressed.test(binding.alternate())) {
                commands.add(binding.action());
            }
        }
    }
}
