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

public class Enemy extends GameCharacter
{
	private Maybe<Direction> previousMoveDirection = Maybe.nothing();
	private int streak = 0;
	private static final int MEAN_STREAK = 75;
	private double stepSize = 0;
	private final int deathPoints;


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

	private int getNextStreak()
	{
		int pstreak;
		Random rng = RandomNumberGenerator.getInstance();

		do {
			pstreak = (int) rng.nextGaussian(MEAN_STREAK, 30);
		} while (pstreak <= 0);

		return pstreak;
	}

	public Maybe<Direction> getPreviousMoveDirection()
	{
		return previousMoveDirection;
	}

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
