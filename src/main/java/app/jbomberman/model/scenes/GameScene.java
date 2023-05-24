package app.jbomberman.model.scenes;

import app.jbomberman.controller.Controller;
import app.jbomberman.controller.KeyBoardController;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalPanel;
import app.jbomberman.drawing.Text;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.ConcreteGameGridBuilder;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GameGridBuilderDirector;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.model.game.entity.gamecharacter.BomberManCapabilities;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * GameScene represents a game
 *
 * @author gp
 */
public class GameScene extends SoundyBomberManScene implements Observable, Observer
{
    /**
     * normalBackgroundSound is the filename that contains the sound to be played
     * during the game before any powerup is picked
     */
    private static final String NORMAL_BACKGROUND_NOISE =
        "sounds/03 Main BGM.wav";

    private static final long TIME = 200_000;

    private static final String POWERUP_TAKEN_BACKGROUND_NOISE =
        "sounds/04 Power-Up Get.wav";

    private static final String GAME_OVER_SOUND =
        "sounds/09 Miss.wav";

    private static final String WIN_SOUND =
        "sounds/05 Stage Clear.wav";
    private static final int LAST_LEVEL = 50;

    private String currentlyPlayingBackgroundNoiseName;

    private Maybe<Vector2D> dimensionsUponEntry = Maybe.nothing();
    private Maybe<Vector2D> currentDimensions = Maybe.nothing();

    /**
     * GameGrid is the gameGrid built by the builder
     */
    private final GameGrid gameGrid;
    private final Controller controller;

    private boolean exiting = false;

    private final HorizontalPanel<Text> topPanel;

    private final Text timeEntry;

    private final Text scoreEntry;

    private final Timer timer;

    public GameScene(Model model, Controller controller)
    {
        super(model);
        this.controller = controller;

        int levelNumber = model.getAccount().getLevel();

        currentlyPlayingBackgroundNoiseName = NORMAL_BACKGROUND_NOISE;

        ConcreteGameGridBuilder builder = new ConcreteGameGridBuilder(
            Vector2D.of(0, 2 * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL));

        Text lives = new Text("left  " + model.getAccount().getCapabilities().getNumberOfLives(), true);
        timeEntry = new Text("", true);
        scoreEntry = new Text("", true);

        topPanel = new HorizontalPanel<>(
            timeEntry,
            scoreEntry,
            lives);

        if (model.getAccount().getCapabilities().getNumberOfLives() < 0)
            model.getAccount().setCapabilities(BomberManCapabilities.defaultCapabilities);

        GameGridBuilderDirector director =
            new GameGridBuilderDirector(this, builder, levelNumber,
                model.getAccount().getCapabilities());

        director.construct();

        gameGrid = builder.getResult();

        gameGrid.setScene(this);

        timer = new Timer(TIME);
    }

    @Override
    public void setup()
    {
        gameGrid.attachObserver(this);

        initialize();
    }

    private boolean timeGone = false;

    @Override
    public Drawing update()
    {
        if (!timer.hasStarted())
            timer.start();
        else if (timer.timeIsUp() && !timeGone) {
            timeGone = true;
            gameGrid.timeHasEnded();
        }

        gameGrid.update();

        scoreEntry.setText(String.valueOf(gameGrid.getScore()));

        if (!exiting) {
            playIfNeeded(currentlyPlayingBackgroundNoiseName);
        } else {
            if (!isPlayingSomething()) {
                if (gameGrid.getBomberMan().getCapabilities().getNumberOfLives() >= 0) {
                    if (getModel().getAccount().getLevel() == LAST_LEVEL + 1) {
                        doTransition(new WinScene(getModel(), controller));
                        getModel().getAccount().setLevel(1);
                    } else
                        doTransition(new LoadingLevelScene(controller, getModel()));
                } else
                    doTransition(new GameOverScene(getModel(), controller));
            }
        }

        Drawing drawing = gameGrid.getDrawing();
        timeEntry.setText("time  " + Math.max(0, timer.millisToEnd() / 1000));
        drawing.merge(topPanel.getDrawing());
        return drawing;
    }

    /**
     * resume() is a function called by the PauseScene to resume the game
     */
    public void resume()
    {
        initialize();
    }

    /**
     * initialize() is a utility function that gets called from both resume and setup.
     * We use this function to avoid code duplication
     */
    private void initialize()
    {
        controller.attachKeyBoardObserver(this); // Listen for ESC

        dimensionsUponEntry = Maybe.just(getModel().getDimensions().unwrap());
        currentDimensions =
            Maybe.just(gameGrid.getDimensions().multiply(Entity.SIZE_IN_MODEL));

        getModel().setDimensions(currentDimensions.unwrap());

        topPanel.setTopLeft(Vector2D.of(0, 0));
        topPanel.setWidth(currentDimensions.unwrap().getX());
    }

    @Override
    public void cleanup()
    {
        controller.detachKeyBoardObserver(this);
    }

    private Maybe<KeyBoardController.KeyBoardUpdateInfo> keyBoardUpdateInfo =
        Maybe.nothing();


    @Override
    public void update(Observable observable)
    {
        if (observable instanceof KeyBoardController keyBoardController) {
            KeyBoardController.KeyBoardUpdateInfo keyBoardUpdateInfo =
                keyBoardController.getUpdateInfo();

            if (keyBoardUpdateInfo.getKeyBoardEventType() == KeyBoardController.KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED
                && keyBoardUpdateInfo.getKeyCode() == KeyBoardController.KeyBoardControllerKeyCode.ESC)
            {
                doTransition(new PauseScene(getModel(), controller, this));
            } else {
                if (!exiting) {
                    // Forward it to bomberman
                    this.keyBoardUpdateInfo = Maybe.just(keyBoardUpdateInfo);
                    updateAllObservers();
                }
            }
        } else if (observable instanceof GameGrid) { // That's telling me a powerup has been taken
            replace(POWERUP_TAKEN_BACKGROUND_NOISE);
            currentlyPlayingBackgroundNoiseName = POWERUP_TAKEN_BACKGROUND_NOISE;
        }
    }

    // Called by bomberman or whatever
    public Maybe<KeyBoardController.KeyBoardUpdateInfo> getKeyBoardUpdateInfo()
    {
        return keyBoardUpdateInfo;
    }

    public Maybe<Vector2D> getDimensionsUponEntry()
    {
        return dimensionsUponEntry;
    }

    public Maybe<Vector2D> getCurrentDimensions()
    {
        return currentDimensions;
    }

    // Just BomberMan character for now. Why the hell is this. I'd say we move
    // the observation to bomberman directly
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public Collection<Observer> getObservers()
    {
        return observers;
    }

    public void endGame(boolean victory)
    {
        if (victory) {
            replace(WIN_SOUND);
            getModel().getAccount().incrementLevel();
        } else {
            replace(GAME_OVER_SOUND);
        }

        exiting = true;
    }

    public void removeLife()
    {
        getModel().getAccount().getCapabilities().removeLife();
    }
}
