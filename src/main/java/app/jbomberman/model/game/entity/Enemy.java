package app.jbomberman.model.game.entity;

import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.MoveManager;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.state.EnemyAliveState;
import app.jbomberman.model.game.entity.state.EnemyDeadState;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Direction;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.RandomNumberGenerator;
import app.jbomberman.util.Vector2D;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Enemy is a class modeling an enemy extending a GameCharacter
 *
 * @author gp
 */
public class Enemy extends GameCharacter
{
	/**
	 * previousModeDirection is a maybe containing a reference to the previous
	 * move direction of the Enemy if any
	 */
	private Maybe<Direction> previousMoveDirection = Maybe.nothing();

	/**
	 * streak is the number of model-coordinates that the Enemy must walk before
	 * changing a direction
	 */
	private int streak = 0;

	/**
	 * MEAN_STREAK is the mean streak that will be set to streak when there is a
	 * change in direction.
	 */
	private static final int MEAN_STREAK = 75;

	/**
	 * stepSize is a buffer of model-coordinate still to go
	 */
	private double stepSize = 0;

	/**
	 * deathPoints are the points that will be awarded to bomberman if the Enemy
	 * gets killed
	 */
	private final int deathPoints;

	/**
	 * public constructor of the Enemy
	 *
	 * @param coordinate         the model-coordinate to which the Enemy must
	 *                           be placed
	 * @param speed              the speed of the enemy
	 * @param goesThroughWalls   boolean that is set to true if the Enemy goes
	 *                           through walls
	 * @param gameGrid           the GameGrid to which the Enemy is attached
	 * @param movingImageManager the ImageManager that will be used when the Enemy
	 *                           is Alive
	 * @param deathImages        the ImagerManager that will be used during when
	 *                           the Enemy goes into death state
	 * @param points             the points that will be awarded to bomberman if
	 *                           the enemy gets killed
	 */
	public Enemy(Vector2D coordinate, double speed,
							 boolean goesThroughWalls,
							 GameGrid gameGrid, MovingImageManager movingImageManager,
							 ImageManager deathImages, int points)
	{
		super(coordinate, new GameCharacterCapabilities(goesThroughWalls, speed), gameGrid);

		setState(new EnemyAliveState(this, movingImageManager,
			new EnemyDeadState(this, deathImages)));

		deathPoints = points;
	}

	/**
	 * move() moves that Enemy if possible
	 */
	@Override
	public void move()
	{
		Vector2D finalPlace = null; // Java say that it might be not initialized
		GameGrid gameGrid = getGameGrid();
		MoveManager moveManager = gameGrid.getMoveManager();

		double speed = getCapabilities().getSpeedInstance();
		Vector2D gridCoordCenter = getCenterInGridCoord();

		stepSize += speed;

		if (stepSize < 1)
			return;

		int integerStepSize = (int) stepSize;

		// Current cell doesn't contain this is this fails
		assert !gameGrid.get(gameGrid.modelCoord2GridCoord(gameGrid.getCenter(this))).contains(this);

		if ((streak <= 0 && moveManager.isAtCrossroad(this)) || previousMoveDirection.isNothing()) {
			List<Direction> possibleMoves =
				Arrays.stream(Direction.values()).collect(Collectors.toList());

			do {
				// Thing cannot be moved
				if (possibleMoves.size() == 0) {
					stepSize -= speed;
					previousMoveDirection = Maybe.nothing();
					return;
				}
				previousMoveDirection = Maybe.just(
					CollectionUtils.randomPickAndRemove(possibleMoves));
				finalPlace =
					this.sum(previousMoveDirection.fromJust().getVersor().multiply(integerStepSize));
			} while (!moveManager.canAccessForCommonSizeEntity(
				getCapabilities(),
				finalPlace,
				gridCoordCenter,
				previousMoveDirection));

			streak = getNextStreak();
		} else {
			int i;
			for (i = 0; i < 2; i++) {
				finalPlace =
					this.sum(previousMoveDirection.fromJust().getVersor().multiply(integerStepSize));
				// Is this move ok?
				if (moveManager.canAccessForCommonSizeEntity(this.getCapabilities(), finalPlace,
					gridCoordCenter, previousMoveDirection))
				{
					break;
				} else {
					// if not let's try the other direction
					previousMoveDirection =
						previousMoveDirection.maybeCompute(Direction::opposite);
					streak = 0; // reset the streak as it might do spinning
				}
			}
			if (i == 2) {
				stepSize -= speed;
				return;
			}
			streak--;
		}

		stepSize -= integerStepSize;

		gameGrid.moveCharacter(this,
			this,
			finalPlace
		);

		getValuesFrom(finalPlace);
	}

	/**
	 * getNextStreak() gets the next streak, randomly built, centered in MEAN_STREAK
	 *
	 * @return the next streak, randomly built, centered in MEAN_STREAK
	 */
	private int getNextStreak()
	{
		int pstreak;
		Random rng = RandomNumberGenerator.getInstance();

		do {
			pstreak = (int) rng.nextGaussian(MEAN_STREAK, 30);
		} while (pstreak <= 0);

		return pstreak;
	}

	/**
	 * getPreviousMoveDirection() gets the previous move of the Enemy if any
	 *
	 * @return the previous move of the Enemy if any
	 */
	public Maybe<Direction> getPreviousMoveDirection()
	{
		return previousMoveDirection;
	}

	/**
	 * getDeathPoints() gets the number of points that will be awarded to
	 * bomberman when killing this enemy
	 *
	 * @return the number of points that will be awarded to bomberman when killing
	 * this enemy
	 */
	public int getDeathPoints()
	{
		return deathPoints;
	}

	@Override
	public boolean allowsBombs()
	{
		return true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
