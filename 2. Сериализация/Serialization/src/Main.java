import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Сериализация

        ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book("Сказки", 2005, "Пушкин"));
        books.add(new Book("Harry Potter", 2010, "Joanne Rowling"));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.dat"));
        oos.writeObject(books);
        oos.close();

        // Десериализация

        ArrayList<Book> readedBooks = new ArrayList<Book>();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.dat"));
        readedBooks = ((ArrayList<Book>)ois.readObject());
        for (Book b: readedBooks) {
            System.out.printf("%s\t%s\t%d\n", b.getName(), b.getAuthor(), b.getPublishingYear());
        }
        ois.close();
    }
}