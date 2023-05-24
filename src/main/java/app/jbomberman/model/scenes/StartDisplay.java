package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.DrawObject;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.util.FunctionalUtils;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

/**
 * StartDisplay is the first scene of the Application.
 *
 * @author gp
 */
public class StartDisplay extends SoundyBomberManScene
{
    /**
     * startDisplaySound is the soundtrack to play during the scene
     */
    private static final String startDisplaySound = "sounds/01 Title Screen.wav";

    private static final double MENU_POSITION = 21. / 32;

    private final AbstractImage startImage;

    private Maybe<HorizontalMenu> menu;

    private final Controller controller;

    /**
     * setup() displays the images, text and sets up callbacks
     */
    public StartDisplay(Controller controller, Model model)
    {
        super(model);
        this.controller = controller;

        startImage = FlightWeightImagesFactory.getInstance().getImage(
            "StartDisplay.png");

        menu = Maybe.nothing();
    }

    @Override
    public void setup()
    {
        Vector2D modelDimensions = getModel().getDimensions();

        int width = modelDimensions.getX();
        int height = modelDimensions.getY();

        menu = Maybe.just(new HorizontalMenu(
            new MenuEntry("Start", true,
                () -> doTransition(new LoadingLevelScene(controller, getModel()))),
            new MenuEntry("Log In", true,
                () -> doTransition(new LogInScene(controller, getModel()))),
            new MenuEntry("Exit", true, getModel()::close)
        ));
        menu.unwrap().setTopLeft(Vector2D.of(0, (int) (height * MENU_POSITION)));
        menu.unwrap().setWidth(width);

        controller.attachKeyBoardObserver(menu.unwrap());
    }

    @Override
    public void cleanup()
    {
        controller.detachKeyBoardObserver(menu.unwrap());
        AudioPlayer.getInstance().stopAll();
    }

    @Override
    public Drawing update()
    {
        playIfNeeded(startDisplaySound);

        Drawing drawing = new Drawing(
            new DrawObject(Vector2D.of(0, 0), startImage));

        drawing.merge(menu.unwrap().getDrawing(), FunctionalUtils::inc);

        return drawing;
    }
}
