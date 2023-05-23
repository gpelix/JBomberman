
package app.jbomberman.model.game.entity;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.entity.gamecharacter.BomberMan;
import app.jbomberman.model.game.entity.gamecharacter.BomberManCapabilities;
import app.jbomberman.model.game.entity.gamecharacter.Enemy;
import app.jbomberman.util.Vector2D;

public class Powerup extends PickableCell
{
	private static final String POWERUP_TAKEN_MUSIC =
		"sounds/Bomberman SFX powerup picked.wav";

	private final BomberManCapabilities deltaCapabilities;

	/**
	 *
	 */
	public Powerup(Vector2D coordinate, GameGrid gameGrid,
								 AbstractImage image, Enemy enemyToSpawn,
								 BomberManCapabilities deltaCapabilities)
	{
		super(coordinate, gameGrid, image, () -> enemyToSpawn);

		this.deltaCapabilities = deltaCapabilities;
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		getUnderlyingFreeCell().remove(this);
		return super.notifyExplosion(explosion);
	}

	// Powerup has been picked by bomberman
	@Override
	public void pick(BomberMan bomberMan)
	{
		getUnderlyingFreeCell().remove(this);
		AudioPlayer.getInstance().playSound(POWERUP_TAKEN_MUSIC);
		getGameGrid().powerUpTaken(); // Then gameGrid shall forward it to
		// the scene
	}

	public BomberManCapabilities getDeltaCapabilities()
	{
		return deltaCapabilities;
	}
}
