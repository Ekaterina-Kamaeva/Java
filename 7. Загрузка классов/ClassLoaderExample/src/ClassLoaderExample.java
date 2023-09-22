import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;

public class ClassLoaderExample {
    private ExpressionClassLoader m_exprLoader = new ExpressionClassLoader();

    public static void main(String[] args) {
        try {
            new ClassLoaderExample().run(args);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void run(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String strExpression = br.readLine();
            if ("exit".equals(strExpression)) {
                break;
            }
            System.out.println(
                    MessageFormat.format("{0} = {1}",
                            new Object[]{strExpression, evaluate(strExpression)}));
        }
    }

    private HashMap<String, IExpression> m_expressions = new HashMap<String, IExpression>();

    // Для каждого выражения создаётся и загружается свой класс
    private String evaluate(String strExpression) throws Exception {
        IExpression expr = m_expressions.get(strExpression);
        if (expr == null) {
            synchronized (m_expressions) {
                expr = m_expressions.get(strExpression);
                if (expr == null) {
                    String strClassName = m_exprLoader.generateClass(strExpression);
                    Class<IExpression> clazz = (Class<IExpression>) m_exprLoader.loadClass(strClassName);
                    System.out.println(clazz.getName());
                    // Создаётся экземпляр загруженного класса
                    expr = clazz.newInstance();
                    m_expressions.put(strExpression, expr);
                }
            }
        }

        return expr.evaluate();
    }

}