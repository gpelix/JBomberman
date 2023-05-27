package app.jbomberman.drawing;

import app.jbomberman.controller.Controller;
import app.jbomberman.controller.KeyBoardController;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;

// Observe the KeyBoardController
public class TypingBox implements Observer, Drawable
{
    private final Text text;
    private final Controller controller;

    public TypingBox(Controller controller, boolean black, Vector2D modelCoordinateOfCenter)
    {
        text = new Text(black, modelCoordinateOfCenter);
        this.controller = controller;
        controller.attachKeyBoardObserver(this);
    }

    public void destruct()
    {
        controller.detachKeyBoardObserver(this);
    }

    @Override
    public void update(Observable observable)
    {
        if (observable instanceof KeyBoardController keyBoardController) {
            KeyBoardController.KeyBoardUpdateInfo updateInfo =
                keyBoardController.getUpdateInfo();

            if (updateInfo.getKeyBoardEventType() == KeyBoardController.KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED)
                return;

            KeyBoardController.KeyBoardControllerKeyCode code =
                updateInfo.getKeyCode();

            if (code == KeyBoardController.KeyBoardControllerKeyCode.BACK_SPACE &&
                text.length() != 0)
                text.pop();

            if (code != KeyBoardController.KeyBoardControllerKeyCode.OTHER
                && code.isPrintable())
            {
                text.append(updateInfo.getKeyCode().getRepr().fromJust().toString());
            }
        }
    }

    public String getText()
    {
        return text.getText();
    }

    @Override
    public Drawing getDrawing()
    {
        return text.getDrawing();
    }
}
