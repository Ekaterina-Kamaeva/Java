public class SharedBuffer
{
    public static void main(String [] args)
    {
        new Thread(new TestThread(1, "Some text")).start();
        new Thread(new TestThread(2, "Another text string")).start();
    }

    static private StringBuffer m_buffer = new StringBuffer();

    static public String reverse(String strValue)
    {
        // Синхронизируем потоки, чтобы в один момент времени к ресурсу имел доступ только один поток.
        synchronized (m_buffer) {
            m_buffer.setLength(0);
            for (int iIdx = strValue.length() - 1; iIdx >= 0; iIdx--) {
                m_buffer.append(strValue.charAt(iIdx));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
            }
            return m_buffer.toString();
        }
    }

}

