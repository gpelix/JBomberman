package app.jbomberman.view;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observer;
import app.jbomberman.view.scenes.BomberManScene;

public abstract class AbstractView implements Observer
{
    private final KeyBoardController inputController;

    private Maybe<BomberManScene> scene;

    private final AbstractGraphicFactory agf;

    protected AbstractView(KeyBoardController inputController, AbstractGraphicFactory agf)
    {
        this.inputController = inputController;
        this.agf = agf;
    }

    public KeyBoardController getInputController()
    {
        return inputController;
    }

    public void setScene(BomberManScene scene)
    {
        this.scene.maybeDo(inputController::detachObserver);
        this.scene = Maybe.just(scene);
        inputController.attachObserver(scene);
    }

    public AbstractGraphicFactory getAbstractGraphicFactory()
    {
        return agf;
    }

    public abstract void close();

    public abstract void setCloseOperationCallBack(Callback callBack);

    public abstract void setIcon(String path);

    public abstract void setTitle(String title);

    public abstract void draw(Drawing drawing);

    public abstract void update();
}
