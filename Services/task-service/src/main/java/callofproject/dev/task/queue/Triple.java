package callofproject.dev.task.queue;

import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.Priority;


import java.util.LinkedList;

import static java.time.LocalDate.now;

public class Triple<T extends Task>
{
    private final LinkedList<T> m_critical;
    private final LinkedList<T> m_high;
    private final LinkedList<T> m_normal;
    private final LinkedList<T> m_low;

    public Triple()
    {
        m_critical = new LinkedList<>();
        m_high = new LinkedList<>();
        m_normal = new LinkedList<>();
        m_low = new LinkedList<>();
    }

    public void insert(T task, Priority priority)
    {
        switch (priority)
        {
            case CRITICAL -> m_critical.add(task);
            case HIGH -> m_high.add(task);
            case NORMAL -> m_normal.add(task);
            default -> m_low.add(task);
        }
    }

    public LinkedList<T> mergeAll()
    {
        var ll = new LinkedList<T>();

        ll.addAll(m_critical);
        ll.addAll(m_high);
        ll.addAll(m_normal);
        ll.addAll(m_low);

        return ll;
    }


    public static void main(String[] args)
    {
        var t = new Triple<Task>();

        var task1 = new callofproject.dev.data.task.entity.Task(null, "Title-1", "Description-1-", Priority.HIGH, now(), now());
        var task2 = new callofproject.dev.data.task.entity.Task(null, "Title-2", "Description-2-", Priority.LOW, now(), now());
        var task3 = new callofproject.dev.data.task.entity.Task(null, "Title-3", "Description-3-", Priority.NORMAL, now(), now());
        var task4 = new callofproject.dev.data.task.entity.Task(null, "Title-4", "Description-4-", Priority.CRITICAL, now(), now());

        t.insert(task1, task1.getPriority());
        t.insert(task1, task1.getPriority());
        t.insert(task3, task3.getPriority());
        t.insert(task2, task2.getPriority());
        t.insert(task4, task4.getPriority());
        t.insert(task2, task2.getPriority());
        t.insert(task2, task2.getPriority());
        t.insert(task3, task3.getPriority());
        t.insert(task4, task4.getPriority());


        var ll = t.mergeAll();
        for (var task : ll)
        {
            System.out.println(task.getPriority());
        }
    }
}
