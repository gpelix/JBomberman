package app.jbomberman.model.game.entity;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.util.Vector2D;

// Observer of B key events and observable by bomberman TODO:
public abstract class Bomb extends Entity
{
	@FunctionalInterface
	public interface BombConstructor<T>
	{
		T construct(Vector2D coordinate, int bombRange, BomberMan bomberMan,
								ImageManager imageManager, GameGrid gameGrid);
	}

	private static final String BOMB_DETONATION_SOUND =
		Properties.getInstance().getSoundPath("explosion.wav");
	private final int bombRange;

	private final ImageManager imageManager;

	private final BomberMan bomberman;


	public Bomb(Vector2D coordinate, int bombRange,
							BomberMan bomberMan, ImageManager imageManager, GameGrid gameGrid)
	{
		super(coordinate, gameGrid);

		this.bombRange = bombRange;
		this.bomberman = bomberMan;

		this.imageManager = imageManager;
	}

	@Override
	public Drawing getDrawing()
	{
		if (imageManager.hasChanged())
			hasChanged();
		return getDrawing(imageManager.getImage());
	}

	public void commonUpdateInModel()
	{
		FreeCell freeCell = getUnderlyingFreeCell();

		AudioPlayer.getInstance().playSound(BOMB_DETONATION_SOUND);
		bomberman.bombHasExploded(this);
		freeCell.removeBomb(this);
		freeCell.notifyExplosion(new Explosion(bombRange, getCenterInGridCoord()));
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		return true;
	}

	@Override
	public String toString()
	{
		return "Bomb(" + super.toString() + ")";
	}

	public void makeExplode()
	{
		// Do nothing by default ControlledBombs should detonate here
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
