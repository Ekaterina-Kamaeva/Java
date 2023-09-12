public class Locker
{
    private volatile boolean m_bIsLocked;

    public Locker()
    {
        m_bIsLocked = false;
    }

    void obtainLock()
    {
        String strThreadName = Thread.currentThread().getName();
        System.err.println(strThreadName + " ожидание блокировки");
        boolean bLockObtained = false;
        while (!bLockObtained)
        {
            synchronized (this)
            {
                System.err.println(strThreadName + " поток синхронизирован");
                if (!m_bIsLocked)
                {
                    System.err.println(strThreadName + " произошла блокировка");
                    m_bIsLocked = true;
                    break;
                }
            }
            System.err.println(strThreadName + " поток рассинхронизирован, пауза");
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }

    void releaseLock()
    {
        String strThreadName = Thread.currentThread().getName();
        System.err.println(strThreadName + " освобождение блокировки");
        synchronized (this)
        {
            System.err.println(strThreadName + " блокировка снята");
            m_bIsLocked = false;
        }
    }

}