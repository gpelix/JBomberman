package app.jbomberman.model.game.entity;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.util.Vector2D;

public class Powerup extends PickableCell
{
    private static final String POWERUP_TAKEN_MUSIC =
        Properties.getInstance().getSoundPath("Bomberman SFX powerup picked.wav");

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
        getUnderlyingFreeCell().removePowerup(this);
        return super.notifyExplosion(explosion);
    }

    @Override
    public boolean allowsBombs()
    {
        return false;
    }

    // Powerup has been picked by bomberman
    @Override
    public void pick(BomberMan bomberMan)
    {
        getUnderlyingFreeCell().removePowerup(this);
        AudioPlayer.getInstance().playSound(POWERUP_TAKEN_MUSIC);
        getGameGrid().powerUpTaken(); // Then gameGrid shall forward it to
        // the scene
    }

    public BomberManCapabilities getDeltaCapabilities()
    {
        return deltaCapabilities;
    }
}
