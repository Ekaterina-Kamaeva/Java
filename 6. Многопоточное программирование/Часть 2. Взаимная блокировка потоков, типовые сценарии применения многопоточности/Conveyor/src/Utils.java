import java.text.MessageFormat;
import java.util.StringTokenizer;

public class Utils
{
    public static String evaluate(String expr)
    {
        try
        {
            StringTokenizer strtok = new StringTokenizer(expr);
            int iVal1 = Integer.parseInt(strtok.nextToken());
            String strOp = strtok.nextToken();
            int iVal2 = Integer.parseInt(strtok.nextToken());
            int iResult = 0;
            if (strOp.equals("+"))
                iResult = iVal1 + iVal2;
            else if (strOp.equals("-"))
                iResult = iVal1 - iVal2;
            else if (strOp.equals("*"))
                iResult = iVal1 * iVal2;
            else if (strOp.equals("/"))
                iResult = iVal1 / iVal2;
            else 
                throw new Exception(
                    MessageFormat.format("Wrong operation: {0}", new Object[] {strOp}));
            
            return MessageFormat.format(
                "{0} {1} {2} = {3}", new Object[]{
                    String.valueOf(iVal1), strOp, String.valueOf(iVal2), String.valueOf(iResult)});
        }
        catch (Exception ex)
        {
            return MessageFormat.format(
                "Can not evaluate expression: {0}", new Object[]{ex});
        }
    }
}
