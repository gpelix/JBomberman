package app.jbomberman.model.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;

public class GameOverScene extends SoundyBomberManScene
{
    private final Controller controller;

    private Maybe<Text> gameOverText = Maybe.nothing();

    private static final String GAME_OVER_SOUND = Properties.getInstance().getSoundPath("10 Game Over.wav");

    protected GameOverScene(Model model, Controller controller)
    {
        super(model);

        this.controller = controller;
    }

    @Override
    public Drawing update()
    {
        if (!isPlayingSomething()) {
            getModel().getAccount().setLevel(1);
            doTransition(new StartDisplay(controller, getModel()));
        }

        return gameOverText.fromJust().getDrawing();
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

        gameOverText = Maybe.just(new Text("Game over", true));

        gameOverText.fromJust().setPosition(
            getModel().getDimensions().div(2)
        );
    }
}
