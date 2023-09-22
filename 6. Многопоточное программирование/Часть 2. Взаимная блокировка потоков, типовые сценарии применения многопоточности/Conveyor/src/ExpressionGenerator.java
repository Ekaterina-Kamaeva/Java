import java.util.Random;

public class ExpressionGenerator extends Thread
{
    private final static String [] OPERATIONS = new String[] {"+", "-", "*", "/"};
    private Dispatcher m_dispatcher;
    private int m_iDelay;
    
    public ExpressionGenerator(int iId, Dispatcher dispatcher, int iDelay)
    {
        super();
        m_dispatcher = dispatcher;
        m_iDelay = iDelay;
        setName("ExpressionGenerator #"+iId);
        setDaemon(true);
    }

    @Override
    public void run()
    {
        try
        {
            Random rnd = new Random();
            
            while (true)
            {
                StringBuffer strResult = new StringBuffer();
                strResult.append(String.valueOf(rnd.nextInt(1000)+1));
                strResult.append(" ");
                strResult.append(OPERATIONS[rnd.nextInt(OPERATIONS.length)]);
                strResult.append(" ");
                strResult.append(String.valueOf(rnd.nextInt(1000)+1));
                m_dispatcher.addExpression(strResult.toString());
                Thread.sleep(m_iDelay);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
}
