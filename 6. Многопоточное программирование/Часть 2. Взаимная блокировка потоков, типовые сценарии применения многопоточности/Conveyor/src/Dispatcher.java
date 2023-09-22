import java.text.MessageFormat;
import java.util.LinkedList;

public class Dispatcher
{
    LinkedList<String> m_queue = new LinkedList<String>();

    public Dispatcher()
    {
        new WorkerThread(1, m_queue).start();
        new WorkerThread(2, m_queue).start();
        new WorkerThread(3, m_queue).start();
        new WorkerThread(4, m_queue).start();
        new WorkerThread(5, m_queue).start();
        new WorkerThread(6, m_queue).start();
//        new WorkerThread(7, m_queue).start();
//        new WorkerThread(8, m_queue).start();
//        new WorkerThread(9, m_queue).start();
//        new WorkerThread(10, m_queue).start();
    }
    
    public void addExpression(String strExpression)
    {
        synchronized(m_queue)
        {
            // добавляем выражение в очередь выражений для обработки
            m_queue.add(strExpression);
            // посылаем оповещение свободным рабочим потокам
            m_queue.notify();
        }
    }
    
    static class WorkerThread extends Thread
    {
        int m_iThreadId;
        LinkedList<String> m_queue;

        public WorkerThread(int iThreadID, LinkedList<String> queue)
        {
            m_iThreadId = iThreadID;
            m_queue = queue;
            setDaemon(true);
            setName("WorkerThread #"+m_iThreadId);
        }

        @Override
        public void run()
        {
            String strExpression = "";
            while (true)
            {
                try
                {
                    int queueSize;
                    synchronized (m_queue)
                    {
                        strExpression = m_queue.poll();
                        queueSize = m_queue.size();
                        if (strExpression == null)
                        {
                            Printer.println(MessageFormat.format("Поток {0} засыпает", new Object[]{getName()}));
                            m_queue.wait();
                            Printer.println(MessageFormat.format("Поток {0} проснулся", new Object[]{getName()}));
                        }
                    }
                    if (strExpression != null)
                    {
                        Thread.sleep(70);
                        Printer.println(MessageFormat.format("Поток {0} вычислил выражение {1}: {2} Queue size: {3}",
                            new Object[]{getName(), strExpression, Utils.evaluate(strExpression), queueSize}));
                    }
                }
                catch (Exception ex)
                {
                    System.out.println(MessageFormat.format("В потоке {0} возникла ошибка: {1}",
                        new Object[]{getName(), ex.getMessage()}));
                }
            }
        }
    }
}
