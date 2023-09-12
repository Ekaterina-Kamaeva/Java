import java.util.concurrent.locks.ReentrantLock;

public class SharedBufferUsingReentrantLock
{
    public static void main(String [] args)
    {
        new TestThreadSync(1, "Some text").start();
        new TestThreadSync(2, "Another text string").start();
        new TestThreadSync(3, "One more string").start();
    }

    static private StringBuffer m_buffer = new StringBuffer();
    static ReentrantLock locker = new ReentrantLock();

    static public String reverse(String strValue)
    {
        locker.lock();
        try
        {
            m_buffer.setLength(0);
            for (int iIdx = strValue.length() - 1; iIdx >= 0; iIdx--)
            {
                m_buffer.append(strValue.charAt(iIdx));
                Thread.sleep(100);
            }
            return m_buffer.toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally
        {
            locker.unlock();
        }
    }

    static class TestThreadSync extends Thread
    {
        private int m_iId;
        private String m_strValue;

        TestThreadSync(int iId, String strValue)
        {
            m_iId = iId;
            m_strValue = strValue;
            setName("Thread #"+m_iId);
        }

        public void run()
        {
            System.out.println(""+m_iId+": value before: "+m_strValue);
            String strResult = SharedBufferUsingReentrantLock.reverse(m_strValue);
            System.out.println(""+m_iId+": value after: "+strResult);
        }
    }

}