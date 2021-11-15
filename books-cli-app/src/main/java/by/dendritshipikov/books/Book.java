package by.dendritshipikov.books;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Book {

    String title;
    String[] authors;
    int pages;

    public Book() {}

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void inputFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input title:");
        title = scanner.nextLine();
        System.out.println("Input authors:");
        String auf = scanner.nextLine();
        authors = auf.split("(\\s*,\\s*)|(^\\s+)|(\\s+$)");
        while (true) {
            try {
                System.out.println("Input number of pages:");
                String p = scanner.nextLine();
                pages = Integer.parseUnsignedInt(p);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again.");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Title: " + title + "\nAuthors: ");
        boolean comma = false;
        for (String author : authors) {
            if (comma) stringBuilder.append(", ");
            comma = true;
            stringBuilder.append(author);
        }
        stringBuilder.append("\nNumber of pages: ").append(pages);
        return stringBuilder.toString();
    }
}
