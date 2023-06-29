package app.jbomberman.model.game.entity;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

/**
 * Powerup is a PickableCell that enhances the abilities bomberman has
 *
 * @author gp
 */
public class Powerup extends PickableCell
{
	/**
	 * POWERUP_TAKEN_MUSIC is the sound that must be played when the powerup gets
	 * taken
	 */
	private static final String POWERUP_TAKEN_MUSIC =
		Properties.getInstance().getSoundPath("Bomberman SFX powerup picked.wav");

	/**
	 * deltaCapabilities are the added capabilities that taking this Powerup will
	 * give to BomberMan
	 */
	private final BomberManCapabilities deltaCapabilities;

	/**
	 * public constructor of Powerup
	 *
	 * @param coordinate        the model-coordinate of the Powerup
	 * @param gameGrid          the GameGrid to which the Powerup is attached
	 * @param image             the image of the PowerUp
	 * @param enemyToSpawn      the enemy to spawn on request
	 * @param deltaCapabilities the extra capabilities the Bomberman will receive
	 *                          when this PowerUp gets taken
	 */
	public Powerup(Vector2D coordinate, GameGrid gameGrid,
								 AbstractImage image, Enemy enemyToSpawn,
								 BomberManCapabilities deltaCapabilities)
	{
		super(coordinate, gameGrid, image, () -> enemyToSpawn);

		this.deltaCapabilities = deltaCapabilities;
	}

	/**
	 * notifyExplosion removes the powerup from the grid and lets the
	 * PickableCell.notifyExplosion() handle the remaining things
	 *
	 * @param explosion an Explosion that contains information relative to the
	 *                  source of the explosion and to the strength of it
	 * @return true as PickableCells always block explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getUnderlyingFreeCell().removePowerup(this);
		return super.notifyExplosion(explosion);
	}

	/**
	 * pick() gets the PowerUp removing it from the grid and updating the
	 * bomberman's capabilities
	 *
	 * @param bomberMan the instance of Bomberman that picked the PickableCell
	 */
	@Override
	public void pick(BomberMan bomberMan)
	{
		getUnderlyingFreeCell().removePowerup(this);
		AudioPlayer.getInstance().playSound(POWERUP_TAKEN_MUSIC);
		Model.getInstance().getGame().powerUpTaken();
		bomberMan.getCapabilities().addDelta(deltaCapabilities);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
