package callofproject.dev.data.common.dsa;

public class Pair<T, R>
{
    private T m_first;
    private R m_second;

    public Pair(final T first, final R second)
    {
        this.m_first = first;
        this.m_second = second;
    }

    public T getFirst()
    {
        return this.m_first;
    }

    public void setFirst(final T first)
    {
        this.m_first = first;
    }

    public R getSecond()
    {
        return this.m_second;
    }

    public void setSecond(final R second)
    {
        this.m_second = second;
    }

    @Override
    public String toString()
    {
        return "Pair [m_first=" + this.m_first + ", m_second=" + this.m_second + "]";
    }
}
