package app.jbomberman.model.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;

public class WinScene extends SoundyBomberManScene
{
    private Maybe<Text> winText = Maybe.nothing();

    private static final String GAME_OVER_SOUND = Properties.getInstance().getSoundPath("08 Ending.wav");

    @Override
    public Drawing update()
    {
        if (!isPlayingSomething()) {
            doTransition(new StartDisplay());
        }

        Drawing a = winText.fromJust().getDrawing();
        a.setDimensions(Model.getInstance().getDimensions());

        return a;
    }

    @Override
    public void cleanup()
    {
        // Do nothing
    }

    @Override
    public void setup()
    {
        AudioPlayer.getInstance().stopAll();
        playIfNeeded(GAME_OVER_SOUND);

        winText = Maybe.just(new Text("You have won", true));

        winText.fromJust().setPosition(
            Model.getInstance().getDimensions().div(2)
        );
    }
}
