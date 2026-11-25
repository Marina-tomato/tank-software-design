package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.game.GameLoop;
import ru.mipt.bit.platformer.input.KeyboardControls;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.level.*;
import java.util.Random;
import ru.mipt.bit.platformer.view.GameView;

import static com.badlogic.gdx.Input.Keys.*;

public final class GameDesktopLauncher extends ApplicationAdapter {
    private GameLoop game;
    private GameView view;

    private final String levelOption;

    public GameDesktopLauncher(String levelOption) {
        this.levelOption = levelOption;
    }

    private LevelSource levelSource() {
        if ("random".equals(levelOption)) return new RandomLevel(10, 8, 14, new Random());
        var file = levelOption == null ? Gdx.files.internal("level.txt") : Gdx.files.absolute(levelOption);
        return new TextLevel(file.readString("UTF-8"));
    }

    @Override
    public void create() {
        Layout layout = levelSource().load();
        PlayingField field = new PlayingField(layout.width(), layout.height(),
                layout.trees().stream().map(Tree::new).toList(), new Tank(layout.player(), 0.4f));
        KeyboardControls keyboard = new KeyboardControls();
        keyboard.bind(UP, W, () -> field.player().move(Direction.UP, field));
        keyboard.bind(LEFT, A, () -> field.player().move(Direction.LEFT, field));
        keyboard.bind(DOWN, S, () -> field.player().move(Direction.DOWN, field));
        keyboard.bind(RIGHT, D, () -> field.player().move(Direction.RIGHT, field));
        view = new GameView(layout.width(), layout.height());
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
        new Lwjgl3Application(new GameDesktopLauncher(args.length == 0 ? null : args[0]), config);
    }
}
