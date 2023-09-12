public class Locker
{
    private volatile boolean m_bIsLocked;
    // Объект синхронизации потоков !!!!!!!!!!!
    private final Object m_sync;

    public Locker()
    {
        m_bIsLocked = false;
        m_sync = new Object();
    }

    void obtainLock()
    {
        String strThreadName = Thread.currentThread().getName();
        System.err.println(strThreadName + " ожидание блокировки");
        boolean bLockObtained = false;
        while (!bLockObtained)
        {
            synchronized (m_sync)
            {
                System.err.println(strThreadName + " начало синхронизации");
                if (!m_bIsLocked)
                {
                    System.err.println(strThreadName + " получение блокировки");
                    m_bIsLocked = true;
                    bLockObtained = true;
                }
                else
                {
                    System.err.println(strThreadName + " ожидание сообщения об окончании процесса");
                    try
                    {
                        m_sync.wait();
                    }
                    catch (InterruptedException ex)
                    {
                    }
                    System.err.println(strThreadName + " сообщение получено");
                }
            }
        }
    }

    void releaseLock()
    {
        String strThreadName = Thread.currentThread().getName();
        System.err.println(strThreadName + " снятие блокировки");
        synchronized (m_sync)
        {
            System.err.println(strThreadName + " блокировка снята");
            m_bIsLocked = false;
            m_sync.notify();
        }
    }

}