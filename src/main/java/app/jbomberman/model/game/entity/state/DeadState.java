
package app.jbomberman.model.game.entity.state;

public abstract class DeadState<T> extends GameCharacterState<T>
{
    protected DeadState(T character)
    {
        super(character);
    }

    @Override
    public boolean isDisappearing()
    {
        return true;
    }
}
