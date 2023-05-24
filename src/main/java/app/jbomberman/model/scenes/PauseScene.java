package app.jbomberman.model.scenes;

import app.jbomberman.JBomberManException;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.drawing.VerticalMenu;
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

    private Maybe<VerticalMenu> menu;

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

        menu = Maybe.just(new VerticalMenu(
            new MenuEntry("resume", true, () -> {
                doTransition(gameScene);
                gameScene.resume();
            }),
            new MenuEntry("exit", true, () -> {
                AudioPlayer.getInstance().stopAll();
                doTransition(new StartDisplay(controller, getModel()));
            })
        ));

        gameScene.getCurrentDimensions().doOrThrow(
            dimensions -> {
                menu.unwrap().setHeight(dimensions.getY());
                menu.unwrap().setTopLeft(Vector2D.of(dimensions.getX() / 2, 0));
            },
            () -> new JBomberManException("gameScene didn't set currentWidth and currentHeight in the setup method!!")
        );

        controller.attachKeyBoardObserver(menu.unwrap());
    }

    @Override
    public void cleanup()
    {
        AudioPlayer.getInstance().resumePlaying();
        controller.detachKeyBoardObserver(menu.unwrap());
    }

    @Override
    public Drawing update()
    {
        return menu.unwrap().getDrawing();
    }
}
