package app.jbomberman.model.game.entity.state;

/**
 * StateFul is an interface that provides a setState method that should set
 * the AbstractState provided in input, to the Object implementing the interface
 *
 * @author gp
 */
public interface StateFul
{
	/**
	 * setState() sets the state of the Stateful object
	 *
	 * @param state the AbstractState to set
	 */
	void setState(AbstractState state);
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
