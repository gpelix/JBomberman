package app.jbomberman.model.game.entity.state;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.MovingImageManager;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.Explosion;
import app.jbomberman.util.CollectionUtils;
import app.jbomberman.util.Maybe;

/**
 * BomberManAliveState is the state of BomberMan when it's alive
 *
 * @author gp
 */
public class BomberManAliveState extends AliveState
{
	/**
	 * bomberMan is a reference to the bomberMan that has this as state
	 */
	private final BomberMan bomberMan;

	/**
	 * imageManager is bomberman's ImageManager
	 */
	private final MovingImageManager imageManager;

	/**
	 * public constructor of BomberManAliveState
	 *
	 * @param bomberMan    the bomberman whose state is this one
	 * @param imageManager bomberman's ImageManager
	 * @param nextState    the state to set on BomberMan's death
	 */
	public BomberManAliveState(BomberMan bomberMan,
														 MovingImageManager imageManager,
														 AbstractState nextState)
	{
		super(bomberMan, nextState);
		this.bomberMan = bomberMan;
		this.imageManager = imageManager;
	}

	/**
	 * notifyExplosion() handles an explosion.
	 * If bomberman is invincible or can go through fire, it does nothing; otherwise
	 * it kills bomberman
	 *
	 * @param explosion the Explosion object
	 * @return false as bomberman doesn't stop explosions
	 */
	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		BomberManCapabilities capabilities = bomberMan.getCapabilities();
		if (!capabilities.goesThroughFire() && !capabilities.isInvincible())
			die();

		return false;
	}

	/**
	 * updateInModel() moves bomberman
	 */
	@Override
	public void updateInModel()
	{
		bomberMan.move();
	}

	/**
	 * getDrawing() returns the current bomberman's image.
	 * It simply returns the Drawing bomberman's getDrawing(AbstractImage)
	 * returns
	 *
	 * @return BomberMan's Drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		return bomberMan.getDrawing(imageManager.getImage(
			CollectionUtils.pick(bomberMan.getDirections())));
	}

	/**
	 * notifyCaughtByEnemy() must be called when bomberman gets caught by
	 * an enemy. If he's not invincible, he dies
	 */
	public void notifyCaughtByEnemy()
	{
		if (!bomberMan.getCapabilities().isInvincible())
			die();
	}

	/**
	 * die() makes bomberman die, removes a life from his, and play the death
	 * sound
	 */
	@Override
	protected void die()
	{
		super.die();

		Model.getInstance().getAccount().getCapabilities().removeLife();

		bomberMan.setSoundHandle(
			Maybe.just(AudioPlayer.getInstance().playSound(BomberMan.getBombermanDeathSound())));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
