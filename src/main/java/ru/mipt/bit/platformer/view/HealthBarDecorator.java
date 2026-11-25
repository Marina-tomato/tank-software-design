package ru.mipt.bit.platformer.view;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public final class HealthBarDecorator implements Runnable {
    private final Runnable sprite;
    private final BooleanSupplier visible;
    private final DoubleSupplier healthFraction;
    private final DoubleConsumer drawBar;

    public HealthBarDecorator(Runnable sprite, BooleanSupplier visible, DoubleSupplier healthFraction,
                              DoubleConsumer drawBar) {
        this.sprite = sprite;
        this.visible = visible;
        this.healthFraction = healthFraction;
        this.drawBar = drawBar;
    }

    @Override
    public void run() {
        sprite.run();
        if (visible.getAsBoolean()) drawBar.accept(healthFraction.getAsDouble());
    }
}
