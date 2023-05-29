package app.jbomberman.model.game.entity.state;

public abstract class DeadState extends GameCharacterState
{
    protected DeadState(StateFul character)
    {
        super(character);
    }

    @Override
    public boolean isDisappearing()
    {
        return true;
    }
}
