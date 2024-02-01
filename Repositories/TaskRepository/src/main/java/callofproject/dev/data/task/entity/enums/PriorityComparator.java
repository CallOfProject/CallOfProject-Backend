package callofproject.dev.data.task.entity.enums;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Priority>
{
    @Override
    public int compare(Priority o1, Priority o2)
    {
        return Integer.compare(o1.ordinal(), o2.ordinal());
    }
}
