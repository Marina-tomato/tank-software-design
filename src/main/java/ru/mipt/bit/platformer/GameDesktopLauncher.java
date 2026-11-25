package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.game.GameLoop;
import ru.mipt.bit.platformer.input.KeyboardControls;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.view.GameView;

import static com.badlogic.gdx.Input.Keys.*;

public final class GameDesktopLauncher extends ApplicationAdapter {
    private GameLoop game;
    private GameView view;

    @Override
    public void create() {
        PlayingField field = new PlayingField(new Tree(new Cell(1, 3)), new Tank(new Cell(1, 1), 0.4f));
        KeyboardControls keyboard = new KeyboardControls();
        keyboard.bind(UP, W, () -> field.player().move(Direction.UP, field));
        keyboard.bind(LEFT, A, () -> field.player().move(Direction.LEFT, field));
        keyboard.bind(DOWN, S, () -> field.player().move(Direction.DOWN, field));
        keyboard.bind(RIGHT, D, () -> field.player().move(Direction.RIGHT, field));
        view = new GameView();
        game = new GameLoop(field, () -> keyboard.poll(Gdx.input::isKeyPressed), view::draw);
    }

    @Override
    public void render() {
        game.frame(Gdx.graphics.getDeltaTime());
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
