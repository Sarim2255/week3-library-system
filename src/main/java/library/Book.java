package library;

public class Book {


    private String  isbn;
    private String  title;
    private String  author;
    private String  genre;
    private boolean isAvailable;

    public Book(String isbn, String title, String author, String genre) {
        this.isbn        = isbn;
        this.title       = title;
        this.author      = author;
        this.genre       = genre;
        this.isAvailable = true;
    }

    public String  getIsbn()     { return isbn; }
    public String  getTitle()    { return title; }
    public String  getAuthor()   { return author; }
    public String  getGenre()    { return genre; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setTitle(String title)           { this.title  = title; }
    public void setAuthor(String author)         { this.author = author; }
    public void setGenre(String genre)           { this.genre  = genre; }

    public String toFileString() {
        return isbn + "|" + title + "|" + author + "|" + genre + "|" + isAvailable;
    }

    public static Book fromFileString(String line) {
        String[] parts = line.split("\\|", 5);
        Book book = new Book(parts[0], parts[1], parts[2], parts[3]);
        book.setAvailable(Boolean.parseBoolean(parts[4]));
        return book;
    }

    @Override
    public String toString() {
        return String.format(
            "  ISBN    : %s%n  Title   : %s%n  Author  : %s%n  Genre   : %s%n  Status  : %s",
            isbn, title, author, genre,
            isAvailable ? "[Available]" : "[Borrowed] "
        );
    }
}
