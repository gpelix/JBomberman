package app.jbomberman.model.game.entity.gamecharacter;

import app.jbomberman.JBomberManException;
import app.jbomberman.audio.AudioHandle;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.KeyBoardController;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.MoveManager;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.AutoBomb;
import app.jbomberman.model.game.entity.Bomb;
import app.jbomberman.model.game.entity.ControlledBomb;
import app.jbomberman.model.game.entity.EntityFactory;
import app.jbomberman.model.game.entity.Powerup;
import app.jbomberman.model.game.entity.state.BomberManAliveState;
import app.jbomberman.model.game.entity.state.BomberManDeadState;
import app.jbomberman.model.scenes.GameScene;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BomberMan extends GameCharacter implements Observer
{
    private final Set<Direction> directions;

    private static final String HORIZONTAL_MOVE_SOUND_FILE =
        "sounds/Bomberman SFX horizontal.wav";

    private static final String BOMB_PLACED_SOUND =
        "sounds/Bomberman SFX bomb in place.wav";

    private static final String BOMBERMAN_DEATH_SOUND =
        "sounds/Bomberman SFX bomberman death sound.wav";

    private static final long INVINCIBILITY_DURATION_IN_MILLIS =
        30_000;

    private static Maybe<Timer> invincibilityTimer =
        Maybe.nothing();

    private final GameScene gameScene;

    private final Queue<Bomb> bombs = new LinkedList<>();

    private Maybe<AudioHandle> soundHandle;

    public BomberMan(Vector2D modelCoordinate, BomberManCapabilities capabilities,
                     GameGrid gameGrid, MovingImageManager movingImageManager,
                     ImageManager deathImages,
                     GameScene gameScene)
    {
        super(modelCoordinate, capabilities, gameGrid);

        setState(new BomberManAliveState(this, movingImageManager,
            new BomberManDeadState(this, deathImages)));

        directions = new HashSet<>();

        soundHandle = Maybe.nothing();

        this.gameScene = gameScene;
    }

    private double stepSize = 0;

    @Override
    public void move()
    {
        if (directions.isEmpty())
            return;

        Maybe<Direction> maybeMoveDirection = CollectionUtils.pick(directions);

        if (maybeMoveDirection.isNothing())
            return;

        Direction moveDirection = maybeMoveDirection.unwrap();

        GameGrid gameGrid = getGameGrid();
        MoveManager moveManager = gameGrid.getMoveManager();

        int integerStepSize = (int) stepSize;
        Vector2D finalPlace =
            this.sum(moveDirection.getAssociatedCoordinate().multiply(integerStepSize));

        if (!moveManager.canAccessForSmallEntity(getCapabilities(), finalPlace,
            gameGrid.getCenterInGridCoord(this), moveDirection))
            return;

        try {
            if (stepSize < 1)
                return;

            // Realligning
            Vector2D topLeft = finalPlace;
            Vector2D topRight = moveManager.getTopRight(finalPlace);
            Vector2D bottomLeft = moveManager.getBottomLeft(finalPlace);
            Vector2D bottomRight = moveManager.getBottomRight(finalPlace);

            Direction pushDir = moveDirection.opposite();


            switch (moveDirection) {
                case EAST: // FALLTHROUGH
                case WEST:
                    // If two laterals are wrong, we push it slightly the opposite direction
                    //  If only one of the ones up is wrong, we push slightly down
                    //  If only one of the ones down is wrong, we push slightly down
                    while ((!checkCoordinateAccess(topRight, maybeMoveDirection) &&
                        !checkCoordinateAccess(bottomRight, maybeMoveDirection)) ||
                        (!checkCoordinateAccess(bottomLeft, maybeMoveDirection) &&
                            !checkCoordinateAccess(topLeft, maybeMoveDirection))) {
                        finalPlace = finalPlace.sum(pushDir.getAssociatedCoordinate());
                        topLeft = finalPlace;
                        topRight = moveManager.getTopRight(finalPlace);
                        bottomLeft = moveManager.getBottomLeft(finalPlace);
                        bottomRight = moveManager.getBottomRight(finalPlace);
                    }

                    while (!checkCoordinateAccess(topLeft, maybeMoveDirection) ||
                        !checkCoordinateAccess(topRight, maybeMoveDirection)) {
                        finalPlace = finalPlace.sum(Direction.SOUTH.getAssociatedCoordinate());
                        topLeft = finalPlace;
                        topRight = moveManager.getTopRight(finalPlace);
                        bottomLeft = moveManager.getBottomLeft(finalPlace);
                        bottomRight = moveManager.getBottomRight(finalPlace);
                    }

                    while (!checkCoordinateAccess(bottomLeft, maybeMoveDirection) ||
                        !checkCoordinateAccess(bottomRight, maybeMoveDirection)) {
                        finalPlace = finalPlace.sum(Direction.NORTH.getAssociatedCoordinate());
                        bottomLeft = moveManager.getBottomLeft(finalPlace);
                        bottomRight = moveManager.getBottomRight(finalPlace);
                    }
                    break;
                case NORTH:  // FALLTHROUGH
                case SOUTH:
                    // If two verticals are wrong, we push it slightly the opposite direction
                    //  If only one of the ones left is wrong, we push slightly right
                    //  If only one of the ones right is wrong, we push slightly left
                    while ((!checkCoordinateAccess(topRight, maybeMoveDirection) &&
                        !checkCoordinateAccess(topLeft, maybeMoveDirection)) ||
                        (!checkCoordinateAccess(bottomLeft, maybeMoveDirection) &&
                            !checkCoordinateAccess(bottomRight, maybeMoveDirection))) {
                        finalPlace = finalPlace.sum(pushDir.getAssociatedCoordinate());
                        topLeft = finalPlace;
                        topRight = moveManager.getTopRight(finalPlace);
                        bottomLeft = moveManager.getBottomLeft(finalPlace);
                        bottomRight = moveManager.getBottomRight(finalPlace);
                    }

                    while (!checkCoordinateAccess(topLeft, maybeMoveDirection) ||
                        !checkCoordinateAccess(bottomLeft, maybeMoveDirection)) {
                        finalPlace = finalPlace.sum(Direction.EAST.getAssociatedCoordinate());
                        topLeft = finalPlace;
                        topRight = moveManager.getTopRight(finalPlace);
                        bottomLeft = moveManager.getBottomLeft(finalPlace);
                        bottomRight = moveManager.getBottomRight(finalPlace);
                    }

                    while (!checkCoordinateAccess(topRight, maybeMoveDirection) ||
                        !checkCoordinateAccess(bottomRight, maybeMoveDirection)) {
                        finalPlace = finalPlace.sum(Direction.WEST.getAssociatedCoordinate());
                        bottomRight = moveManager.getBottomRight(finalPlace);
                        topRight = moveManager.getTopRight(finalPlace);
                    }
                    break;
                default:
                    throw new JBomberManException("Shouldn't be here");
            }

            stepSize -= integerStepSize;


            getGameGrid().moveCharacter(this,
                this,
                finalPlace
            );

            getValuesFrom(finalPlace);
        } finally {
            stepSize += getCapabilities().getSpeedInstance();
        }


        if (!soundHandle.compute(false, AudioHandle::isPlaying))
            soundHandle = Maybe.just(
                AudioPlayer.getInstance().playSound(HORIZONTAL_MOVE_SOUND_FILE));
    }

    private boolean checkCoordinateAccess(Vector2D modelCoordinate, Maybe<Direction> dir)
    {
        return getGameGrid().getMoveManager().checkCoordinate(
            modelCoordinate,
            getCapabilities(),
            getGameGrid().getCenterInGridCoord(this),
            dir
        );
    }

    public void update(KeyBoardController.KeyBoardUpdateInfo updateInfo)
    {
        KeyBoardController.KeyBoardControllerKeyCode keyCode = updateInfo.getKeyCode();
        switch (updateInfo.getKeyBoardEventType()) {
            case KEY_RELEASED ->
                Direction.fromKeyCode(keyCode).maybeDo(directions::remove);
            case KEY_PRESSED -> {
                BomberManCapabilities capabilities = getCapabilities();
                switch (keyCode) {
                    case SPACE -> {
                        if (bombs.size() != capabilities.getAvailableBombs() &&
                            !getUnderlyingFreeCell().hasBomb() &&
                            !getUnderlyingFreeCell().hasWall())
                        {
                            GameGrid gameGrid = getGameGrid();

                            Vector2D currentFreeCellGridCoordinate =
                                gameGrid.modelCoord2GridCoord(gameGrid.getCenter(this));


                            Bomb newBomb = EntityFactory.getInstance().buildBomb(
                                gameGrid.gridCoord2modelCoor(currentFreeCellGridCoordinate),
                                getCapabilities().getBombPower(),
                                this,
                                gameGrid,
                                (capabilities.hasRemoveControl()) ? ControlledBomb::new :
                                    AutoBomb::new
                            );

                            bombs.add(newBomb);

                            AudioPlayer.getInstance().playSound(BOMB_PLACED_SOUND);

                            gameGrid.addEntityInFreeCell(
                                newBomb,
                                currentFreeCellGridCoordinate);
                        }
                    }
                    case B -> {
                        if (!bombs.isEmpty())
                            bombs.remove().makeExplode();
                    }
                    default ->
                        Direction.fromKeyCode(keyCode).maybeDo(directions::add);
                }
            }
        }
    }

    @Override
    public void updateInModel()
    {
        invincibilityTimer.maybeDo(
            timer -> {
                if (timer.timeIsUp())
                    getCapabilities().unsetIsInvincible();
            }
        );
        super.updateInModel();
    }

    @Override
    public void update(Observable observable)
    {
        if (observable instanceof GameScene)
            update(((GameScene) observable).getKeyBoardUpdateInfo().unwrap());
    }

    public void takePowerup(Powerup powerup)
    {
        BomberManCapabilities capabilities = getCapabilities();

        capabilities.addDelta(powerup.getDeltaCapabilities());

        if (capabilities.isInvincible()) {
            Timer timer = new Timer(INVINCIBILITY_DURATION_IN_MILLIS);
            timer.start();
            invincibilityTimer = Maybe.just(timer);
        }

        powerup.pick(this);
    }


    public Maybe<AudioHandle> getSoundHandle()
    {
        return soundHandle;
    }

    public void setSoundHandle(Maybe<AudioHandle> soundHandle)
    {
        this.soundHandle = soundHandle;
    }

    public static String getBombermanDeathSound()
    {
        return BOMBERMAN_DEATH_SOUND;
    }

    public GameScene getGameScene()
    {
        return gameScene;
    }

    public Set<Direction> getDirections()
    {
        return directions;
    }

    public void bombHasExploded(Bomb bomb)
    {
        bombs.remove(bomb);
    }

    @Override
    public BomberManCapabilities getCapabilities()
    {
        return (BomberManCapabilities) super.getCapabilities();
    }

    public void caughtByEnemy()
    {
        if (getState() instanceof BomberManAliveState aliveState) {
            aliveState.notifycaughtByenemy();
        }
    }

    @Override
    public void setCapabilities(GameCharacterCapabilities capabilities)
    {
        throw new JBomberManException("This must not be used. The capabilities bomberman has directly map to the ones of the account and must not be substituted");
    }

    public void stopListening()
    {
        directions.clear();
    }
}
