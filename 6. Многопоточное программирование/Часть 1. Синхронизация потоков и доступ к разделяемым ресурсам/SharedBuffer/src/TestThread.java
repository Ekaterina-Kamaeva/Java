class TestThread extends Thread {
    private int m_iId;
    private String m_strValue;

    TestThread(int iId, String strValue) {
        m_iId = iId;
        m_strValue = strValue;
    }

    public void run() {
        System.out.println("" + m_iId + ": value before: " + m_strValue);
        String strResult = SharedBuffer.reverse(m_strValue);
        System.out.println("" + m_iId + ": value after: " + strResult);
    }
}
