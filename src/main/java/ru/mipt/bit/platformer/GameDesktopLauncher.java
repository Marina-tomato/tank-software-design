package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.input.KeyboardControls;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.view.GameView;

import static com.badlogic.gdx.Input.Keys.*;

public final class GameDesktopLauncher extends ApplicationAdapter {
    private PlayingField field;
    private KeyboardControls keyboard;
    private GameView view;

    @Override
    public void create() {
        field = new PlayingField(new Tree(new Cell(1, 3)), new Tank(new Cell(1, 1)));
        keyboard = new KeyboardControls();
        keyboard.bind(UP, W, () -> field.player().move(Direction.UP, field));
        keyboard.bind(LEFT, A, () -> field.player().move(Direction.LEFT, field));
        keyboard.bind(DOWN, S, () -> field.player().move(Direction.DOWN, field));
        keyboard.bind(RIGHT, D, () -> field.player().move(Direction.RIGHT, field));
        view = new GameView();
    }

    @Override
    public void render() {
        keyboard.poll(Gdx.input::isKeyPressed);
        field.player().update(Gdx.graphics.getDeltaTime());
        view.draw(field);
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Tanks");
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
