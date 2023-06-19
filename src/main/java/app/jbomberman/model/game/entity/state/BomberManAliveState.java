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

public class BomberManAliveState extends AliveState
{
	private final BomberMan bomberMan;

	private final MovingImageManager imageManager;

	private boolean drawn;

	public BomberManAliveState(BomberMan bomberMan,
														 MovingImageManager imageManager,
														 GameCharacterState nextState)
	{
		super(bomberMan, nextState);
		this.bomberMan = bomberMan;
		this.imageManager = imageManager;
		this.drawn = false;
	}

	@Override
	public boolean notifyExplosion(Explosion explosion)
	{
		BomberManCapabilities capabilities = bomberMan.getCapabilities();
		if (!capabilities.goesThroughFire() && !capabilities.isInvincible())
			die();

		return false;
	}

	@Override
	public void updateInModel()
	{
		bomberMan.move();
	}

	@Override
	public Drawing getDrawing()
	{
		//System.out.println("Bomberman is at " + bomberMan.getX() + " " + bomberMan.getY());
		Drawing drawing = bomberMan.getDrawing(imageManager.getNextImage(drawn ?
			CollectionUtils.pick(bomberMan.getDirections()) : Maybe.nothing()));

		drawn = true;

		return drawing;
	}

	public void notifyCaughtByEnemy()
	{
		BomberManCapabilities cap = bomberMan.getCapabilities();

		if (!cap.isInvincible())
			die();
	}

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
