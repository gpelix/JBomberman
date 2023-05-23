
package app.jbomberman.util;

public interface Observer
{
	void update(Observable observable);

	Observer NULL_OBSERVER = observable -> {/*Do nothing*/};
}
