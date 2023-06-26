package app.jbomberman.model.game.entity.state;

public abstract class AliveState extends AbstractState
{
	private final AbstractState deathState;

	protected AliveState(StateFul character, AbstractState deathState)
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

/* vim: set ts=2 sw=2 tw=0 noet :*/
