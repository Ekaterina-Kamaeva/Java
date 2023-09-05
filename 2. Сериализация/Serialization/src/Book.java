import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private int publishingYear;
    private String author;

    public Book(String name, int publishingYear, String author) {
        this.name = name;
        this.publishingYear = publishingYear;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public String getAuthor() {
        return author;
    }
}

