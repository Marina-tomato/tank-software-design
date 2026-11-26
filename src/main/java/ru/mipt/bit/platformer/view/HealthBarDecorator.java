package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.math.Rectangle;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.ObjDoubleConsumer;

public final class HealthBarDecorator implements Consumer<Rectangle> {
    private final Consumer<Rectangle> sprite;
    private final BooleanSupplier visible;
    private final DoubleSupplier healthFraction;
    private final ObjDoubleConsumer<Rectangle> drawBar;

    public HealthBarDecorator(Consumer<Rectangle> sprite, BooleanSupplier visible, DoubleSupplier healthFraction,
                              ObjDoubleConsumer<Rectangle> drawBar) {
        this.sprite = sprite;
        this.visible = visible;
        this.healthFraction = healthFraction;
        this.drawBar = drawBar;
    }

    @Override
    public void accept(Rectangle bounds) {
        sprite.accept(bounds);
        if (visible.getAsBoolean()) drawBar.accept(bounds, healthFraction.getAsDouble());
    }
}
