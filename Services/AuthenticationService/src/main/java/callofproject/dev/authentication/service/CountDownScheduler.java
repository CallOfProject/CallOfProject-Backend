package callofproject.dev.authentication.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * An abstract class for implementing a countdown scheduler with a specified interval.
 * Subclasses should extend this class and provide implementations for the 'onTick' and 'onFinish' methods.
 */
public abstract class CountDownScheduler
{
    private final Timer m_timer;
    private final long m_millisInFuture;
    private final long m_interval;
    private final TimerTask m_timerTask;


    /**
     * Creates and returns a TimerTask that handles the countdown logic.
     *
     * @return A TimerTask for countdown operations.
     */
    private TimerTask createTimerTask()
    {
        return new TimerTask()
        {
            private long m_value;

            public void run()
            {
                try
                {
                    var millisUntilFinished = m_millisInFuture - m_value;

                    onTick(millisUntilFinished);
                    m_value += m_interval;

                    if (m_value < m_millisInFuture)
                        return;

                    onFinish();
                    m_timer.cancel();
                } catch (Exception ignore)
                {

                }
            }
        };
    }

    /**
     * Creates a new CountDownScheduler with the given duration in milliseconds and interval in milliseconds.
     *
     * @param millisInFuture The total duration of the countdown in milliseconds.
     * @param interval       The interval between 'onTick' callbacks in milliseconds.
     */
    protected CountDownScheduler(long millisInFuture, long interval)
    {
        this(millisInFuture, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a new CountDownScheduler with the given duration, interval, and time unit.
     *
     * @param durationInFuture The total duration of the countdown.
     * @param interval         The interval between 'onTick' callbacks.
     * @param timeUnit         The time unit for 'durationInFuture' and 'interval'.
     */
    protected CountDownScheduler(long durationInFuture, long interval, TimeUnit timeUnit)
    {
        m_millisInFuture = timeUnit == MILLISECONDS ? durationInFuture : MILLISECONDS.convert(durationInFuture, timeUnit);
        m_interval = timeUnit == MILLISECONDS ? interval : MILLISECONDS.convert(interval, timeUnit);
        m_timer = new Timer();
        m_timerTask = createTimerTask();
    }

    /**
     * Callback method called on each tick of the countdown.
     *
     * @param millisUntilFinished The remaining time in milliseconds until the countdown finishes.
     * @throws Exception Any exception that may occur during processing.
     */
    protected abstract void onTick(long millisUntilFinished) throws Exception;

    /**
     * Callback method called when the countdown finishes.
     *
     * @throws Exception Any exception that may occur during processing.
     */
    protected abstract void onFinish() throws Exception;

    /**
     * Starts the countdown scheduler.
     *
     * @return The CountDownScheduler instance for method chaining.
     */
    public final CountDownScheduler start()
    {
        m_timer.scheduleAtFixedRate(m_timerTask, 0, m_interval);

        return this;
    }

    /**
     * Cancels the countdown scheduler.
     */
    public final void cancel()
    {
        m_timer.cancel();
    }
}