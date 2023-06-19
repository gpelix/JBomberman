package app.jbomberman.util;

/**
 * Timer is a class that models the behaviour of a timer
 *
 * @author gp
 */
public class Timer
{
	/**
	 * a maybe that represents the time of the start of the timer. It's a Maybe
	 * because if it hasn't started yet we have no startTime at all
	 */
	private Maybe<Long> startTime;

	/**
	 * duration is the amount of time for which the timer has to run
	 * in milliseconds
	 */
	private long duration;

	/**
	 * public constructor specifying the duration of the timer interval
	 *
	 * @param durationInMillis the duration of the timer interval
	 */
	public Timer(long durationInMillis)
	{
		reset();
		this.duration = durationInMillis;
	}

	/**
	 * start is used to start the timer
	 */
	public void start()
	{
		startTime = Maybe.just(System.currentTimeMillis());
	}

	/**
	 * timeIsUp() checks whether the timer is "ringing"
	 *
	 * @return true if time is up
	 * @throws RuntimeException if the timer hasn't started yet
	 */
	public boolean timeIsUp()
	{
		return elapsed() >= duration;
	}

	/**
	 * elapsed() must be called sfter start and it returns the milliseconds that
	 * have passed since the timer's start
	 *
	 * @return the milliseconds for which the timer has been running
	 * @throws RuntimeException if the timer hasn't started yet
	 */
	private long elapsed()
	{
		return startTime.computeOrThrow(
			start -> System.currentTimeMillis() - start,
			() -> new RuntimeException("Timer not started")
		);
	}

	/**
	 * hasStarted() checks whether the timer has started or not
	 *
	 * @return true if the timer has started
	 */
	public boolean hasStarted()
	{
		return startTime.isJust();
	}

	/**
	 * reset() must be used to reset the timer but its duration
	 */
	public void reset()
	{
		startTime = Maybe.nothing();
	}

	/**
	 * @return the number of milliseconds to the end of timer
	 */
	public long millisToEnd()
	{
		return duration - elapsed();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
