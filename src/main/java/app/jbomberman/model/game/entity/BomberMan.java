package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.audio.AudioHandle;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.MoveManager;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.state.BomberManAliveState;
import app.jbomberman.model.game.entity.state.BomberManDeadState;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;
import app.jbomberman.view.scenes.GameScene;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static app.jbomberman.util.Direction.EAST;
import static app.jbomberman.util.Direction.NORTH;
import static app.jbomberman.util.Direction.SOUTH;
import static app.jbomberman.util.Direction.WEST;

public class BomberMan extends GameCharacter implements Observer
{
	private final Set<Direction> directions;

	private static final String HORIZONTAL_MOVE_SOUND_FILE =
		Properties.getInstance().getSoundPath("Bomberman SFX horizontal.wav");

	private static final String BOMB_PLACED_SOUND =
		Properties.getInstance().getSoundPath("Bomberman SFX bomb in place.wav");

	private static final String BOMBERMAN_DEATH_SOUND =
		Properties.getInstance().getSoundPath("Bomberman SFX bomberman death sound.wav");

	private static final long INVINCIBILITY_DURATION_IN_MILLIS =
		30_000;

	private static Maybe<Timer> invincibilityTimer =
		Maybe.nothing();

	private final Queue<Bomb> bombs = new LinkedList<>();

	private Maybe<AudioHandle> soundHandle;

	private boolean hasSteppedOnDoor = false;

	public BomberMan(Vector2D modelCoordinate, BomberManCapabilities capabilities,
									 GameGrid gameGrid, MovingImageManager movingImageManager,
									 ImageManager deathImages)
	{
		super(modelCoordinate, capabilities, gameGrid);

		setState(new BomberManAliveState(this, movingImageManager,
			new BomberManDeadState(this, deathImages)));

		directions = new HashSet<>();

		soundHandle = Maybe.nothing();
	}

	private double stepSize = 0;

	@Override
	public void move()
	{
		hasChanged();

		if (directions.isEmpty())
			return;

		//System.out.println("hasStepped On Door " + hasSteppedOnDoor);

		if (hasSteppedOnDoor)
			return;

		Maybe<Direction> maybeMoveDirection = CollectionUtils.pick(directions);

		if (maybeMoveDirection.isNothing())
			return;

		hasChanged();

		Direction moveDirection = maybeMoveDirection.fromJust();

		GameGrid gameGrid = getGameGrid();
		MoveManager moveManager = gameGrid.getMoveManager();

		int integerStepSize = (int) stepSize;
		Vector2D finalPlace =
			this.sum(moveDirection.getVersor().multiply(integerStepSize));

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
						finalPlace = finalPlace.sum(pushDir.getVersor());
						topLeft = finalPlace;
						topRight = moveManager.getTopRight(finalPlace);
						bottomLeft = moveManager.getBottomLeft(finalPlace);
						bottomRight = moveManager.getBottomRight(finalPlace);
					}

					while (!checkCoordinateAccess(topLeft, maybeMoveDirection) ||
						!checkCoordinateAccess(topRight, maybeMoveDirection)) {
						finalPlace = finalPlace.sum(Direction.SOUTH.getVersor());
						topLeft = finalPlace;
						topRight = moveManager.getTopRight(finalPlace);
						bottomLeft = moveManager.getBottomLeft(finalPlace);
						bottomRight = moveManager.getBottomRight(finalPlace);
					}

					while (!checkCoordinateAccess(bottomLeft, maybeMoveDirection) ||
						!checkCoordinateAccess(bottomRight, maybeMoveDirection)) {
						finalPlace = finalPlace.sum(Direction.NORTH.getVersor());
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
						finalPlace = finalPlace.sum(pushDir.getVersor());
						topLeft = finalPlace;
						topRight = moveManager.getTopRight(finalPlace);
						bottomLeft = moveManager.getBottomLeft(finalPlace);
						bottomRight = moveManager.getBottomRight(finalPlace);
					}

					while (!checkCoordinateAccess(topLeft, maybeMoveDirection) ||
						!checkCoordinateAccess(bottomLeft, maybeMoveDirection)) {
						finalPlace = finalPlace.sum(Direction.EAST.getVersor());
						topLeft = finalPlace;
						topRight = moveManager.getTopRight(finalPlace);
						bottomLeft = moveManager.getBottomLeft(finalPlace);
						bottomRight = moveManager.getBottomRight(finalPlace);
					}

					while (!checkCoordinateAccess(topRight, maybeMoveDirection) ||
						!checkCoordinateAccess(bottomRight, maybeMoveDirection)) {
						finalPlace = finalPlace.sum(Direction.WEST.getVersor());
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

			if (!hasSteppedOnDoor)
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

	public void update(KeyBoardUpdateInfo updateInfo)
	{
		KeyBoardControllerKeyCode keyCode = updateInfo.getKeyCode();
		switch (updateInfo.getKeyBoardEventType()) {
			case KEY_RELEASED ->
				keyCodeToDirection(keyCode).maybeDo(directions::remove);
			case KEY_PRESSED -> {
				BomberManCapabilities capabilities = getCapabilities();
				switch (keyCode) {
					case SPACE -> {
						if (bombs.size() != capabilities.getAvailableBombs() &&
							getUnderlyingFreeCell().allowsBombs())
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

							gameGrid.addBomb(
								newBomb,
								currentFreeCellGridCoordinate);
						}
					}
					case B -> {
						if (!bombs.isEmpty())
							bombs.remove().makeExplode();
					}
					default -> keyCodeToDirection(keyCode).maybeDo(directions::add);
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
	public boolean allowsBombs()
	{
		return true;
	}

	@Override
	public void update(Observable observable)
	{
		if (observable instanceof GameScene gameScene)
			update(gameScene.getKeyBoardUpdateInfo().unwrapOrThrow(() ->
				new JBomberManException("No updateInfo available")));
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
			aliveState.notifyCaughtByEnemy();
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

	public Maybe<Direction> keyCodeToDirection(KeyBoardControllerKeyCode keyCode)
	{
		return switch (keyCode) {
			case UP -> Maybe.just(NORTH);
			case DOWN -> Maybe.just(SOUTH);
			case LEFT -> Maybe.just(WEST);
			case RIGHT -> Maybe.just(EAST);
			default -> Maybe.nothing();
		};
	}

	public void steppedOnDoor(Door door)
	{
		getCapabilities().addLife();
		stopListening();
		getValuesFrom(door); // go onto the door
		hasSteppedOnDoor = true;
		//System.out.println("Door is at " + this.getX() + " " + this.getY());
		//System.out.println("And after getting the values bomberman is at " + getX() + " " + getY());
		hasChanged();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
