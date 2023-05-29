package app.jbomberman.model.game.entity.state;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.entity.Explosion;

public abstract class GameCharacterState
{
    private final StateFul character;

    protected GameCharacterState(StateFul character)
    {
        this.character = character;
    }

    protected StateFul getCharacter()
    {
        return character;
    }

    public abstract boolean notifyExplosion(Explosion explosion);

    public abstract void updateInModel();

    public abstract Drawing getDrawing();

    public abstract boolean isDisappearing();
}
