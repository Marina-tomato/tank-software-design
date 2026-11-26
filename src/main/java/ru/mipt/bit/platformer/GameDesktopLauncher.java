package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.game.*;
import ru.mipt.bit.platformer.ai.*;
import java.util.ArrayList;
import ru.mipt.bit.platformer.input.DesktopKeyboard;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.level.*;
import java.util.Random;
import ru.mipt.bit.platformer.view.GameView;

/** Desktop entry adapter and composition root for LibGDX, the game loop and the domain. */
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
        var healthBars = new ToggleHealthBars();
        view = new GameView(layout.width(), layout.height(), field.player(), healthBars::visible);
        field.subscribe(view.entities());
        Random random = new Random();
        EnemySpawner.populate(field, random, 4, 0.4f);
        var controllers = new ArrayList<Controller>();
        controllers.add(new DesktopKeyboard(field, 25, healthBars));
        var advisor = new AdvisorAdapter(new AimAdvisor());
        for (Tank tank : field.tanks()) {
            if (tank != field.player()) controllers.add(new RandomDriver(tank, field, random, advisor, 25));
        }
        game = new GameLoop(field, controllers, view);
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
