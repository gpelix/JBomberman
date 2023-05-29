package app.jbomberman.model.game.entity.state;

public abstract class AliveState extends GameCharacterState
{
    private final GameCharacterState deathState;

    protected AliveState(StateFul character, GameCharacterState deathState)
    {
        super(character);

        this.deathState = deathState;
    }

    @Override
    public boolean isDisappearing()
    {
        return false;
    }

    protected void die()
    {
        getCharacter().setState(deathState);
    }
}
