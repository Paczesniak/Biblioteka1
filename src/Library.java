import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Reader> readers;
    private List<Book> books;

    public Library() {
        this.readers = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public void borrowBook(Reader reader, Book book) {
        if (reader != null && book != null && book.isAvailable()) {
            reader.addBorrowedBook(book);
            book.setAvailable(false);
        }
    }

    public void returnBook(Reader reader, Book book) {
        if (reader != null && book != null && !book.isAvailable()) {
            reader.removeBorrowedBook(book);
            book.setAvailable(true);
        }
    }
}
