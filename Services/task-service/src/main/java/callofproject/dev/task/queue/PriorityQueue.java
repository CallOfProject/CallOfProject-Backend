package callofproject.dev.task.queue;

import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.Priority;

import java.util.LinkedList;

import static java.time.LocalDate.now;

public class PriorityQueue<T extends Task>
{
    private final LinkedList<T> m_queue;
    private int m_size;

    public PriorityQueue()
    {
        m_queue = new LinkedList<>();
        m_size = 0;
    }

    public void enqueue(T task)
    {
        if (m_size == 0)
        {
            m_queue.add(task);
            m_size++;
            return;
        }

        int i = 0;
        for (T t : m_queue)
        {
            if (task.getPriority().ordinal() > t.getPriority().ordinal())
            {
                m_queue.add(i, task);
                m_size++;
                return;
            }
            i++;
        }
        m_queue.addLast(task);
        m_size++;
    }

    public T dequeue()
    {
        if (m_size == 0)
            return null;
        m_size--;
        return m_queue.removeFirst();
    }

    public T peek()
    {
        if (m_size == 0)
            return null;
        return m_queue.getFirst();
    }


    public int size()
    {
        return m_size;
    }

    public boolean isEmpty()
    {
        return m_size == 0;
    }

    public void clear()
    {
        m_queue.clear();
        m_size = 0;
    }

    public void print()
    {
        for (T t : m_queue)
        {
            System.out.println(t.getPriority());
        }
    }

    public static void main(String[] args)
    {
        var queue = new PriorityQueue<Task>();

        var task1 = new Task(null, "Title-1", "Description-1-", Priority.HIGH, now(), now());
        var task2 = new Task(null, "Title-2", "Description-2-", Priority.LOW, now(), now());
        var task3 = new Task(null, "Title-3", "Description-3-", Priority.NORMAL, now(), now());
        var task4 = new Task(null, "Title-4", "Description-4-", Priority.CRITICAL, now(), now());

        queue.enqueue(task3);
        queue.enqueue(task3);
        queue.enqueue(task1);
        queue.enqueue(task4);
        queue.enqueue(task1);
        queue.enqueue(task1);
        queue.enqueue(task3);
        queue.enqueue(task1);
        queue.enqueue(task1);
        queue.enqueue(task2);
        queue.enqueue(task4);
        queue.enqueue(task2);
        queue.enqueue(task4);

        queue.print();
    }
}
