import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private Library library;
    private Scanner scanner;

    public LibraryManagementSystem() {
        this.library = new Library();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        int option;

        System.out.println("=== Biblioteka ===");

        do {
            System.out.println("1. Dodaj czytelnika");
            System.out.println("2. Dodaj książkę");
            System.out.println("3. Wypożycz książkę");
            System.out.println("4. Zwrot książki");
            System.out.println("5. Wyświetl listę książek");
            System.out.println("6. Wyświetl wypożyczone książki");
            System.out.println("7. Zapisz dane do pliku CSV");
            System.out.println("0. Zakończ");
            System.out.print("Wybierz opcję: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Pomija znak nowej linii

            switch (option) {
                case 1:
                    addReader();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    displayAvailableBooks();
                    break;
                case 6:
                    displayBorrowedBooks();
                    break;
                case 7:
                    saveDataToCSV();
                    break;
                case 0:
                    System.out.println("Zakończono program.");
                    break;
                default:
                    System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
                    break;
            }

            System.out.println();
        } while (option != 0);

        scanner.close();
    }

    private void addReader() {
        System.out.print("Podaj imię czytelnika: ");
        String name = scanner.nextLine();
        Reader reader = new Reader(name);
        library.addReader(reader);
        System.out.println("Dodano czytelnika: " + name);
    }

    private void addBook() {
        System.out.print("Podaj tytuł książki: ");
        String title = scanner.nextLine();
        System.out.print("Podaj autora książki: ");
        String author = scanner.nextLine();
        Book book = new Book(title, author);
        library.addBook(book);
        System.out.println("Dodano książkę: " + title + " (" + author + ")");
    }

    private void borrowBook() {
        System.out.print("Podaj imię czytelnika: ");
        String readerName = scanner.nextLine();
        Reader reader = findReader(readerName);

        if (reader == null) {
            System.out.println("Nie znaleziono czytelnika o podanym imieniu.");
            return;
        }

        System.out.print("Podaj tytuł książki: ");
        String bookTitle = scanner.nextLine();
        Book book = findBook(bookTitle);

        if (book == null) {
            System.out.println("Nie znaleziono książki o podanym tytule.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Książka " + bookTitle + " jest już wypożyczona.");
            return;
        }

        book.setAvailable(false); // Ustawia dostępność książki na false
        book.setBorrowDate(LocalDate.now()); // Ustawia datę wypożyczenia na aktualną

        reader.addBorrowedBook(book); // Dodaje wypożyczoną książkę do czytelnika
        System.out.println("Książka " + bookTitle + " została wypożyczona przez " + readerName);
    }

    private void returnBook() {
        System.out.print("Podaj imię czytelnika: ");
        String readerName = scanner.nextLine();
        Reader reader = findReader(readerName);

        if (reader == null) {
            System.out.println("Nie znaleziono czytelnika o podanym imieniu.");
            return;
        }

        System.out.print("Podaj tytuł książki: ");
        String bookTitle = scanner.nextLine();
        Book book = findBook(bookTitle);

        if (book == null) {
            System.out.println("Nie znaleziono książki o podanym tytule.");
            return;
        }

        if (book.isAvailable()) {
            System.out.println("Książka " + bookTitle + " nie jest wypożyczona.");
            return;
        }

        book.setAvailable(true); // Ustawia dostępność książki na true
        reader.removeBorrowedBook(book); // Usuwa zwroconą książkę z listy wypożyczonych

        System.out.println("Książka " + bookTitle + " została zwrócona przez " + readerName);

        LocalDate borrowDate = book.getBorrowDate();
        if (borrowDate != null) {
            System.out.println("Data wypożyczenia: " + borrowDate);
            calculatePenalty(reader, book);
        }
    }

    private void calculatePenalty(Reader reader, Book book) {
        LocalDate returnDate = LocalDate.now();
        LocalDate dueDate = book.getBorrowDate().plusDays(14); // Zakładamy 14 dni na zwrot

        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            double penalty = daysLate * 0.5; // Kary 0.5 jednostki waluty za każdy dzień zwłoki (można dostosować)
            System.out.println("Czytelnik " + reader.getName() + " ma karę w wysokości " + penalty);
        }
    }

    private void displayAvailableBooks() {
        System.out.println("Dostępne książki:");
        for (Book book : library.getAvailableBooks()) {
            System.out.println(book.getTitle() + " - " + book.getAuthor());
        }
    }

    private void displayBorrowedBooks() {
        System.out.println("Wypożyczone książki:");
        for (Reader reader : library.getReaders()) {
            System.out.println("Czytelnik: " + reader.getName());
            System.out.println("Książki:");
            List<Book> borrowedBooks = reader.getBorrowedBooks();
            if (borrowedBooks.isEmpty()) {
                System.out.println("Brak");
            } else {
                for (Book book : borrowedBooks) {
                    System.out.println(book.getTitle() + " - " + book.getAuthor());
                }
            }
            System.out.println();
        }
    }

    private Reader findReader(String readerName) {
        for (Reader reader : library.getReaders()) {
            if (reader.getName().equalsIgnoreCase(readerName)) {
                return reader;
            }
        }
        return null;
    }

    private Book findBook(String bookTitle) {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                return book;
            }
        }
        return null;
    }

    private void saveDataToCSV() {
        try (FileWriter writer = new FileWriter("library_data.csv")) {
            writer.write("Czytelnicy\n");
            writer.write("Imię\n");
            for (Reader reader : library.getReaders()) {
                writer.write(reader.getName() + "\n");
            }
            writer.write("\nKsiążki\n");
            writer.write("Tytuł,Autor,Dostępna\n");
            for (Book book : library.getBooks()) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.isAvailable() + "\n");
            }
            System.out.println("Dane zapisane do pliku library_data.csv");
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku.");
        }
    }
}