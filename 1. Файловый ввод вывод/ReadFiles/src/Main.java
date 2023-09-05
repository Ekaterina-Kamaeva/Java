import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    /**
     *  Чтение файлов
     *
     *  Абстрактные классы
     *  InputStream         OutputStream            Reader          Writer
     *
     *  FileInputStream     FileOutputStream        FileReader      FileWriter      <- обычное потоковое чтение
     *  BufferedInputStream BufferedOutputStream    BufferedReader  BufferedWriter  <- для построчного чтения файла
     *  ObjectInputStream   ObjectOutputStream                                      <- работа с бинарными файлами (сериализация)
     *
     *
     *  Классы для работы с символами (чтение файлов в определённой кодировке)
     *  InputStreamReader
     *  OutputStreamReader
     */

    public static void ReadFileUsingFileInputStream(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        int code;
        while ((code = fis.read()) != -1) {
            System.out.print((char)code);
        }
        fis.close();
    }

    public static void ReadFileUsingBufferedReader(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }

    public static void ReadFileUsingInputStreamReader(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis, "Windows-1251");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }

    public static void main(String[] args) throws IOException {
        ReadFileUsingInputStreamReader("1. Файловый ввод вывод\\Files\\src\\input(rus).txt");
    }
}