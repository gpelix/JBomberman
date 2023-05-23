
package app.jbomberman.util;

public class Timer
{
	private Maybe<Long> startTime;

	private long duration;

	public Timer(long durationInMillis)
	{
		reset();
		this.duration = durationInMillis;
	}

	public void start()
	{
		startTime = Maybe.just(System.currentTimeMillis());
	}

	public boolean timeIsUp()
	{
		return elapsed() >= duration;
	}

	private long elapsed()
	{
		return startTime.computeOrThrow(
			start -> System.currentTimeMillis() - start,
			() -> new RuntimeException("Timer not started")
		);
	}

	public boolean hasStarted()
	{
		return startTime.isJust();
	}

	public void reset()
	{
		startTime = Maybe.nothing();
	}

	public long millisToEnd()
	{
		return duration - elapsed();
	}
}
