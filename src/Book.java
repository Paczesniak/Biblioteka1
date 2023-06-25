import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private boolean available;
    private LocalDate borrowDate; // Nowe pole borrowDate

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
        this.borrowDate = null; // Inicjalizacja borrowDate jako null
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
}
