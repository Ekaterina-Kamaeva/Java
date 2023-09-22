import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import com.sun.tools.javac.Main;

public class ExpressionClassLoader extends ClassLoader {
    private final static String TEMPLATE =
            "public class {0} implements IExpression \n" +
                    "'{'\n" +
                    "  public String evaluate() \n" +
                    "  '{'\n" +
                    "    return String.valueOf({1}); \n" +
                    "  '}'\n" +
                    "'}'\n";

    File fileWorkDir = new File("./tmp");

    public String generateClass(String strExpression) throws Exception {
        fileWorkDir.mkdirs();
        final File file = File.createTempFile("Class", ".java", fileWorkDir);
        String strFileName = file.getName();
        String strClassName = strFileName.substring(0, strFileName.length() - 5);
        FileOutputStream out = new FileOutputStream(file);
        try {
            OutputStreamWriter osw = new OutputStreamWriter(out, "Cp1251");
            String strClassContent = MessageFormat.format(
                    TEMPLATE, new Object[]{strClassName, strExpression});
            osw.write(strClassContent);
            osw.flush();
            osw.close();
        } finally {
            out.close();
        }
        Thread compiler = new Thread(new Runnable() {
            public void run() {
                // Компиляция происходит в процессе работы программы
                // Генерируется файл *.class
                try {
                    Main.compile(new String[]{file.toString()});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        compiler.setDaemon(true);
        compiler.start();
        compiler.join();
        return strClassName;
    }

    // Переопределённый метод, ищет и создаёт класс
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassNotFoundException savedEx = null;
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException ex) {
            savedEx = ex;
        }
        if (name.indexOf(".") >= 0) {
            throw savedEx;
        }
        File file = new File(fileWorkDir, name + ".class");
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                BufferedInputStream bin = new BufferedInputStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                while (true) {
                    int iCount = bin.read(buffer);
                    if (iCount >= 0) {
                        out.write(buffer, 0, iCount);
                    } else {
                        break;
                    }
                }
                byte[] classCode = out.toByteArray();
                // Метод, создающий класс из файла байт-кода
                Class clazz = super.defineClass(name, classCode, 0, classCode.length);
                return clazz;
            } finally {
                in.close();
            }
        } catch (Exception ex) {
            throw savedEx;
        }
    }
}