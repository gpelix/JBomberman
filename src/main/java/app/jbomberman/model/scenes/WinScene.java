
package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;

public class WinScene extends SoundyBomberManScene
{
    private final Controller controller;

    private Maybe<Text> winText = Maybe.nothing();

    private static final String GAME_OVER_SOUND = "sounds/08 Ending.wav";

    protected WinScene(Model model, Controller controller)
    {
        super(model);

        this.controller = controller;
    }

    @Override
    public Drawing update()
    {
        if (!isPlayingSomething()) {
            doTransition(new StartDisplay(controller, getModel()));
        }

        Drawing a = winText.unwrap().getDrawing();
        a.setDimensions(getModel().getDimensions().unwrap());

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

        winText.unwrap().setPosition(
            getModel().getDimensions().unwrap().div(2)
        );
    }
}
