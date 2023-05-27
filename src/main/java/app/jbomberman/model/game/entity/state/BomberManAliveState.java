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

public class BomberManAliveState extends AliveState<BomberMan>
{
    private final BomberMan bomberMan;

    private final GameCharacterState<BomberMan> nextState;

    private final MovingImageManager imageManager;

    private boolean drawn;

    public BomberManAliveState(BomberMan bomberMan,
                               MovingImageManager imageManager,
                               GameCharacterState<BomberMan> nextState)
    {
        super(bomberMan, nextState);
        this.bomberMan = bomberMan;
        this.nextState = nextState;
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
        Drawing drawing = bomberMan.getDrawing(imageManager.getNextImage(drawn ?
            CollectionUtils.pick(bomberMan.getDirections()) : Maybe.nothing()));

        drawn = true;

        return drawing;
    }

    public void notifycaughtByenemy()
    {
        BomberManCapabilities cap = bomberMan.getCapabilities();

        if (!cap.isInvincible())
            die();
    }

    @Override
    protected void die()
    {
        bomberMan.setState(nextState);

        Model.getInstance().getAccount().getCapabilities().removeLife();

        bomberMan.setSoundHandle(
            Maybe.just(AudioPlayer.getInstance().playSound(BomberMan.getBombermanDeathSound())));
    }
}
