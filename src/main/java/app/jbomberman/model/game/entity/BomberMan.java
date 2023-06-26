package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.audio.AudioHandle;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GamePlayException;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static app.jbomberman.util.Direction.EAST;
import static app.jbomberman.util.Direction.NORTH;
import static app.jbomberman.util.Direction.SOUTH;
import static app.jbomberman.util.Direction.WEST;

/**
 * BomberMan is the class represents bomberman
 * <p>
 * It implements the Observer interface as it has to Observer the Model to
 * know when there are KeyBoardEvents. The Model doesn't have an instance of
 * BomberMan, so this must be the only way possible
 *
 * @author gp
 */
public class BomberMan extends GameCharacter implements Observer
{
	/**
	 * directions contain the Set of directions in which the BomberMan is moving.
	 * It's a Set as there might be more user input that we keep track of
	 */
	private final Set<Direction> directions;

	/**
	 * HORIZONTAL_MOVE_SOUND_FILE is the String containing the path of the
	 * audio file to play when the BomberMan is moving horizontally
	 */
	private static final String HORIZONTAL_MOVE_SOUND_FILE =
		Properties.getInstance().getSoundPath("Bomberman SFX horizontal.wav");

	/**
	 * BOMB_PLACED_SOUND is the String containing the path of the audio file to
	 * play when a Bomb gets placed
	 */
	private static final String BOMB_PLACED_SOUND =
		Properties.getInstance().getSoundPath("Bomberman SFX bomb in place.wav");

	/**
	 * BOMBERMAN_DEATH_SOUND is the String containing the path of the audio file
	 * to play when bomberman dies
	 */
	private static final String BOMBERMAN_DEATH_SOUND =
		Properties.getInstance().getSoundPath("Bomberman SFX bomberman death sound.wav");

	/**
	 * INVINCIBILITY_DURATION_IN_MILLIS is the duration of the invincibility period
	 */
	private static final long INVINCIBILITY_DURATION_IN_MILLIS = 30_000;

	/**
	 * invincibilityTimer is a timer used to keep track of whether the
	 * invincibility period has ended
	 */
	private static Maybe<Timer> invincibilityTimer =
		Maybe.nothing();

	/**
	 * bombs is Queue of Bombs to which newly created bombs must be added
	 */
	private final Queue<Bomb> bombs = new LinkedList<>();

	/**
	 * soundHandle is an AudioHandle that manages the sounds emitted by Bomberman
	 * It's a Maybe as there might also be no sound at all
	 */
	private Maybe<AudioHandle> soundHandle;

	/**
	 * hasSteppedOnDoor is a flag that keeps track of whether the BomberMan has
	 * stepped on a Door
	 */
	private boolean hasSteppedOnDoor = false;

	/**
	 * public constructor of BomberMan
	 *
	 * @param modelCoordinate    the model-coordinate to which the bomberman must
	 *                           be placed
	 * @param capabilities       the capabilities bomberman will have
	 * @param gameGrid           the GameGrid to which the BomberMan is attached
	 * @param movingImageManager the ImageManager that will be used when bomberman
	 *                           is Alive
	 * @param deathImages        the ImagerManager that will be used during when
	 *                           the BomberMan goes into death state
	 */
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

	/**
	 * deathPoints are the points that will be awarded to bomberman if the Enemy
	 * gets killed
	 */
	private double stepSize = 0;

	/**
	 * move() moves that BomberMan if possible
	 */
	@Override
	public void move()
	{
		hasChanged();

		if (directions.isEmpty())
			return;

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

			// Realigning
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

	/**
	 * checkCoordinateAccess() must be called to check if a model-coordinate is
	 * within the boundaries of the tracks in the GameGrid
	 *
	 * @param modelCoordinate the model-coordinate to that we must check
	 * @param dir             the direction to which the bomberman is moving
	 *                        if any
	 * @return whether the model-coordinate is within the boundaries of the tracks
	 * in the GameGrid
	 */
	private boolean checkCoordinateAccess(Vector2D modelCoordinate, Maybe<Direction> dir)
	{
		return getGameGrid().getMoveManager().checkCoordinate(
			modelCoordinate,
			getCapabilities(),
			getGameGrid().getCenterInGridCoord(this),
			dir
		);
	}

	/**
	 * update() must be called internally to handle a new KeyBoardEvent
	 *
	 * @param updateInfo the KeyBoardUpdateInfo produced by the keyboard event
	 */
	private void update(KeyBoardUpdateInfo updateInfo)
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
								(capabilities.hasRemoteControl()) ? ControlledBomb::new :
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

	/**
	 * updateInModel() resets invincibilityFlag in bomberman's capabilities if
	 * invincibilityTimer is up and gets the drawing
	 */
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

	/**
	 * update() must handle an update from the Model. In that case
	 *
	 * @param observable the observable that called the method update.
	 */
	@Override
	public void update(Observable observable)
	{
		if (observable instanceof Model)
			update(Model.getInstance().getKeyBoardUpdateInfo().unwrapOrThrow(() ->
				new JBomberManException("No updateInfo available")));
		else
			throw new GamePlayException("Observable not supported");
	}

	/**
	 * takePowerup() must be used to pick the powerup
	 *
	 * @param powerup the powerup to pick
	 */
	public void takePowerup(Powerup powerup)
	{
		powerup.pick(this);

		if (getCapabilities().isInvincible()) {
			Timer timer = new Timer(INVINCIBILITY_DURATION_IN_MILLIS);
			timer.start();
			invincibilityTimer = Maybe.just(timer);
		}
	}

	/**
	 * getSoundHandle() returns the soundHandle that manages the playing of
	 * BomberMan's sounds.
	 * It's meant to be used by the BomberManStates
	 *
	 * @return the soundHandle that manages the playing of BomberMan's sounds
	 */
	public Maybe<AudioHandle> getSoundHandle()
	{
		return soundHandle;
	}

	/**
	 * setSoundHandle() sets the soundHandle that manages the playing of
	 * BomberMan's sounds.
	 * It's meant to be used by the BomberManStates
	 *
	 * @param soundHandle the soundHandle that manages the playing of BomberMan's
	 *                    sounds to set
	 */
	public void setSoundHandle(Maybe<AudioHandle> soundHandle)
	{
		this.soundHandle = soundHandle;
	}

	/**
	 * getBombermanDeathSound() must be used to get the death sound from the
	 * BomberManStates
	 *
	 * @return the death sound
	 */
	public static String getBombermanDeathSound()
	{
		return BOMBERMAN_DEATH_SOUND;
	}

	/**
	 * getDirections() gets the Set of directions currently pressed
	 *
	 * @return the Set of directions currently pressed
	 */
	public Set<Direction> getDirections()
	{
		return directions;
	}

	/**
	 * bombHasExploded() notifies bomberman that one of his bombs has exploded and
	 * now it can spawn again new bombs
	 *
	 * @param bomb the exploded bomb
	 */
	public void bombHasExploded(Bomb bomb)
	{
		bombs.remove(bomb);
	}

	/**
	 * getCapabilities() overrides the getCapabilities() as defined in
	 * GameCharacter to return a GameCharacterCapabilities after a cast to
	 * BomberManCapabilities
	 *
	 * @return the capabilities bomberman has
	 */
	@Override
	public BomberManCapabilities getCapabilities()
	{
		return (BomberManCapabilities) super.getCapabilities();
	}

	/**
	 * caughtByEnemy() must be called when BomberMan gets caught
	 */
	public void caughtByEnemy()
	{
		if (getState() instanceof BomberManAliveState aliveState) {
			aliveState.notifyCaughtByEnemy();
		}
	}

	/**
	 * setCapabilities() must not be allowed on BomberManCapabilities on it throws
	 * an exception if called
	 *
	 * @param capabilities the new capabilities for the GameCharacter
	 * @throws JBomberManException is always thrown if this method is called
	 */
	@Override
	public void setCapabilities(GameCharacterCapabilities capabilities)
	{
		throw new JBomberManException("This must not be used. The capabilities bomberman has directly map to the ones of the account and must not be substituted");
	}

	/**
	 * stopListening() clear the directions and stops the BomberMan
	 */
	public void stopListening()
	{
		directions.clear();
	}

	/**
	 * keyCodeToDirection() translates a KeyBoardControllerKeyCode to a
	 * Maybe Direction
	 *
	 * @param keyCode the key code to convert to a direction
	 * @return the Direction after translation from KeyBoardControllerKeyCode
	 */
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

	/**
	 * steppedOnDoor() must be called by the Door to trigger the final movement
	 * of the BomberMan
	 *
	 * @param door the door on which the BomberMan has stepped on
	 */
	public void steppedOnDoor(Door door)
	{
		getCapabilities().addLife();
		stopListening();
		getValuesFrom(door); // go onto the door
		hasSteppedOnDoor = true;
		hasChanged();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
