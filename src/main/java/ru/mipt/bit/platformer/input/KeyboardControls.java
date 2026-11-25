package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

public final class KeyboardControls {
    private record Binding(int key, int alternate, Runnable action) {}
    private final List<Binding> bindings = new ArrayList<>();

    public void bind(int key, int alternate, Runnable action) {
        bindings.add(new Binding(key, alternate, action));
    }

    public void poll(IntPredicate pressed) {
        for (Binding binding : bindings) {
            if (pressed.test(binding.key()) || pressed.test(binding.alternate())) {
                binding.action().run();
            }
        }
    }
}
