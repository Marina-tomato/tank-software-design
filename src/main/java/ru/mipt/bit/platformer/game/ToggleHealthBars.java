package ru.mipt.bit.platformer.game;

public final class ToggleHealthBars implements GameCommand {
    private boolean visible;

    @Override
    public void execute() {
        visible = !visible;
    }

    public boolean visible() {
        return visible;
    }
}
