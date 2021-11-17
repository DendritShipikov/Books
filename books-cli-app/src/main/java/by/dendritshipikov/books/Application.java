package by.dendritshipikov.books;

import by.dendritshipikov.books.model.Book;
import by.dendritshipikov.books.services.BookService;
import by.dendritshipikov.books.services.BookServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Application {

    private final BookService bookService;
    Scanner scanner;

    public Application(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        String folderName = "info";
        if (args.length > 1) {
            System.out.println("Wrong command line params.");
            return;
        } else if (args.length == 1) {
            folderName = args[0];
        }
        File folder = new File(folderName);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                System.out.println("Error: directory can not be created");
                return;
            }
        }
        BookService bookService = new BookServiceImpl(folder);
        Application application = new Application(bookService);
        try {
            application.mainLoop();
        } catch (IOException e) {
            System.out.println("Some input/output error occurs. Program ended.");
        }
    }

    private void onAdd() throws IOException {
        Book book = new Book();
        System.out.println("Input title:");
        book.setTitle(scanner.nextLine());
        if (bookService.existsBook(book.getTitle())) {
            System.out.println("Book can not be added, because book with the same title already exists.");
            return;
        }
        book.setAuthors(inputAuthors());
        book.setPages(inputPages());
        System.out.println("Do you want to add description? (yes/no)");
        while (true) {
            String yesno = scanner.nextLine();
            if (yesno.equals("yes")) {
                System.out.println("Input description:");
                book.setDescription(scanner.nextLine());
                break;
            } else if (yesno.equals("no")) {
                break;
            } else {
                System.out.println("Please, write 'yes' or 'no'");
            }
        }
        bookService.addBook(book);
        System.out.println("Book was successfully added.");
    }

    private void onFind() throws IOException {
        System.out.println("Input search query:");
        String query = scanner.nextLine();
        List<Book> books = bookService.findBook(query);
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        int i = 1;
        for (Book book : books) {
            System.out.println(i);
            System.out.println(book.toString("\t"));
            ++i;
        }
    }

    private void onEdit() throws IOException {
        System.out.println("Input title:");
        String title = scanner.nextLine();
        if (!bookService.existsBook(title)) {
            System.out.println("There isn't book with such title");
            return;
        }
        Book book = bookService.getBookByTitle(title);
        System.out.println("What do you want to edit?\n1. Authors\n2. Number of pages\n3. Description");
        while (true) {
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                book.setAuthors(inputAuthors());
                break;
            } else if (choice.equals("2")) {
                book.setPages(inputPages());
                break;
            } else if (choice.equals("3")) {
                System.out.println("Input description:");
                book.setDescription(scanner.nextLine());
                break;
            } else {
                System.out.println("Wrong input. Please, write '1', '2' or '3'");
            }
        }
        bookService.editBook(book);
        System.out.println("Book successfully edited.");
    }

    private void onDelete() throws IOException {
        System.out.println("Input title:");
        String title = scanner.nextLine();
        if (!bookService.existsBook(title)) {
            System.out.println("There isn't such book.");
            return;
        }
        if (!bookService.deleteBook(title)) {
            System.out.println("Book can not be deleted");
            return;
        }
        System.out.println("Book was successfully deleted.");
    }

    private int inputPages() {
        while (true) {
            try {
                System.out.println("Input number of pages:");
                String p = scanner.nextLine();
                return Integer.parseUnsignedInt(p);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again.");
            }
        }
    }

    private String[] inputAuthors() {
        System.out.println("Input authors:");
        String auf = scanner.nextLine();
        return auf.split("(\\s*,\\s*)|(^\\s+)|(\\s+$)");
    }

    public void mainLoop() throws IOException {
        scanner = new Scanner(System.in);
        loop: while (true) {
            System.out.println("What do you want?:");
            String command = scanner.nextLine();
            switch (command) {
                case "add":
                    onAdd();
                    break;
                case "find":
                    onFind();
                    break;
                case "delete":
                    onDelete();
                    break;
                case "edit":
                    onEdit();
                    break;
                case "help":
                    System.out.println("HELP HERE");
                    break;
                case "exit":
                    break loop;
                default:
                    System.out.println("Wrong command. Write 'help' for more information.");
                    break;
            }
        }
        scanner.close();
    }
}
