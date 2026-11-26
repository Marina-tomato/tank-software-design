package ru.mipt.bit.platformer.view;

import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.game.ToggleHealthBars;
import ru.mipt.bit.platformer.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HealthBarTest {
    @Test void toggleAffectsAllDecoratorsAndReadsCurrentHealthEachFrame() {
        var toggle = new ToggleHealthBars();
        var draws = new ArrayList<String>();
        Tank player = new Tank(new Cell(0, 0), 1);
        Tank enemy = new Tank(new Cell(1, 0), 1, 80);
        var field = new PlayingField(2, 1, List.of(), player);
        field.addTank(enemy);
        var playerBounds = new Rectangle(4, 12, 90, 82);
        var enemyBounds = new Rectangle(130, 6, 90, 82);
        var a = new HealthBarDecorator(rect -> {
            assertSame(playerBounds, rect);
            draws.add("player");
        }, toggle::visible, player::healthFraction, (rect, fraction) -> {
            assertSame(playerBounds, rect);
            draws.add("p=" + fraction);
        });
        var b = new HealthBarDecorator(rect -> {
            assertSame(enemyBounds, rect);
            draws.add("enemy");
        }, toggle::visible, enemy::healthFraction, (rect, fraction) -> {
            assertSame(enemyBounds, rect);
            draws.add("e=" + fraction);
        });
        a.accept(playerBounds);
        toggle.execute();
        player.shoot(field, 20);
        a.accept(playerBounds);
        b.accept(enemyBounds);
        toggle.execute();
        b.accept(enemyBounds);
        assertEquals(List.of("player", "player", "p=1.0", "enemy", "e=0.75", "enemy"), draws);
    }

    @Test void initialHealthMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new Cell(0, 0), 1, 0));
    }
}
