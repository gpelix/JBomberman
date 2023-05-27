package app.jbomberman.model.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

/**
 * LoadingLevelScene is the scene loaded before starting the gameplay
 * Its only duty is the one of displaying a little tiny text telling which
 * level the player is going to play and playing a sound
 *
 * @author gp
 */
public class LoadingLevelScene extends SoundyBomberManScene
{
    /**
     * loadingLevelSceneSound is the sound to be played during this scene
     */
    private static final String LOADING_LEVEL_SCENE_SOUND =
        Properties.getInstance().getSoundPath("02 Stage Start.wav");

    private Maybe<Drawing> drawing;

    /**
     * setup() sets up a text indicating the level and Loading...
     */
    @Override
    public void setup()
    {
        AudioPlayer.getInstance().stopAll();

        Model model = Model.getInstance();

        Vector2D dimensions = model.getDimensions();

        Text stageText = new Text("Stage " + model.getAccount().getLevel(), true, dimensions.div(2));
        Text loadingText = new Text("Loading...", true,
            dimensions.sub(
                Vector2D.of(dimensions.getX() / 2,
                    FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)));


        drawing = Maybe.just(new Drawing(loadingText.getDrawing(), stageText.getDrawing()));
        drawing.fromJust().setDimensions(dimensions);

        playIfNeeded(LOADING_LEVEL_SCENE_SOUND);
    }


    @Override
    public Drawing update()
    {
        if (!isPlayingSomething())
            doTransition(new GameScene());

        return drawing.fromJust();
    }

    @Override
    public void cleanup()
    {
        // This should not be necessary
        AudioPlayer.getInstance().stopAll();
    }
}
