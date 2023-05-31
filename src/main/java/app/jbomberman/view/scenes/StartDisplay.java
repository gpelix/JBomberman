package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.DrawObject;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
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
    private static final String startDisplaySound = Properties.getInstance().getSoundPath("01 Title Screen.wav");

    private static final double MENU_POSITION = 21. / 32;

    private final AbstractImage startImage;

    private HorizontalMenu menu;

    /**
     * setup() displays the images, text and sets up callbacks
     */
    public StartDisplay()
    {
        startImage = FlightWeightImagesFactory.getInstance().getImage(
            "StartDisplay.png");

        Model model = Model.getInstance();
        Vector2D modelDimensions = model.getDimensions();

        int width = modelDimensions.getX();
        int height = modelDimensions.getY();

        menu = new HorizontalMenu(
            new MenuEntry("Start", true,
                () -> doTransition(LoadingLevelScene::new)),
            new MenuEntry("Log In", true,
                () -> doTransition(LogInScene::new)),
            new MenuEntry("Exit", true, model::close)
        );
        menu.setTopLeft(Vector2D.of(0, (int) (height * MENU_POSITION)));
        menu.setWidth(width);

        Model.getController().attachKeyBoardObserver(menu);
    }

    @Override
    public void cleanup()
    {
        Model.getController().detachKeyBoardObserver(menu);
        AudioPlayer.getInstance().stopAll();
    }

    @Override
    public Drawing update()
    {
        playIfNeeded(startDisplaySound);

        Drawing drawing = new Drawing(
            new DrawObject(Vector2D.of(0, 0), startImage));

        drawing.merge(menu.getDrawing(), x -> x + 1);

        return drawing;
    }
}
