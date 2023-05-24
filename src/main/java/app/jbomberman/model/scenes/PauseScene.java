package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

public class PauseScene extends BomberManScene
{
    /**
     * gameNode is the group that contains the game that has been interrupted
     */
    private final GameScene gameScene;

    private final Controller controller;

    private Maybe<HorizontalMenu> menu;

    public PauseScene(Model model, Controller controller, GameScene gameScene)
    {
        super(model);

        this.gameScene = gameScene;
        this.controller = controller;
    }

    @Override
    public void setup()
    {
        AudioPlayer.getInstance().pausePlaying();

        menu = Maybe.just(new HorizontalMenu(
            new MenuEntry("resume", true, () -> {
                doTransition(gameScene);
                gameScene.resume();
            }),
            new MenuEntry("exit", true, () -> {
                AudioPlayer.getInstance().stopAll();
                doTransition(new StartDisplay(controller, getModel()));
            })
        ));

        Vector2D modelDims = Model.getDimensions();

        menu.fromJust().setWidth(modelDims.getX());
        menu.fromJust().setTopLeft(Vector2D.of(0, 0));

        controller.attachKeyBoardObserver(menu.fromJust());
    }

    @Override
    public void cleanup()
    {
        AudioPlayer.getInstance().resumePlaying();
        controller.detachKeyBoardObserver(menu.fromJust());
    }

    @Override
    public Drawing update()
    {
        return menu.fromJust().getDrawing();
    }
}
