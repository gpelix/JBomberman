
package app.jbomberman.model.game.entity.state;

public abstract class AliveState<T> extends GameCharacterState<T>
{
    private GameCharacterState<T> deathState;

    protected AliveState(T character, GameCharacterState<T> deathState)
    {
        super(character);

        this.deathState = deathState;
    }

    @Override
    public boolean isDisappearing()
    {
        return false;
    }

    protected abstract void die();
}
