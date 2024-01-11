package callofproject.dev.authentication.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public abstract class CountDownScheduler {
    private final Timer m_timer;
    private final long m_millisInFuture;
    private final long m_interval;
    private final TimerTask m_timerTask;

    private TimerTask createTimerTask()
    {
        return new TimerTask() {
            private long m_value;

            public void run()
            {
                try {
                    var millisUntilFinished = m_millisInFuture - m_value;

                    onTick(millisUntilFinished);
                    m_value += m_interval;

                    if (m_value < m_millisInFuture)
                        return;

                    onFinish();
                    m_timer.cancel();
                }
                catch (Exception ignore) {

                }
            }
        };
    }

    protected CountDownScheduler(long millisInFuture, long interval)
    {
        this(millisInFuture, interval, TimeUnit.MILLISECONDS);
    }

    protected CountDownScheduler(long durationInFuture, long interval, TimeUnit timeUnit)
    {
        m_millisInFuture = timeUnit == MILLISECONDS ? durationInFuture : MILLISECONDS.convert(durationInFuture, timeUnit);
        m_interval = timeUnit == MILLISECONDS ? interval : MILLISECONDS.convert(interval, timeUnit);
        m_timer = new Timer();
        m_timerTask = createTimerTask();
    }

    protected abstract void onTick(long millisUntilFinished) throws Exception;
    protected abstract void onFinish() throws Exception;

    public final CountDownScheduler start()
    {
        m_timer.scheduleAtFixedRate(m_timerTask, 0, m_interval);

        return this;
    }

    public final void cancel()
    {
        m_timer.cancel();
    }
}