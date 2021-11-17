package by.dendritshipikov.books.model;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Book {

    private String title;
    private String[] authors;
    private String description;
    private int pages;

    public Book() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(String prefix) {
        StringBuilder stringBuilder = new StringBuilder(prefix + "Title: " + title + "\n" + prefix + "Authors: ");
        boolean comma = false;
        for (String author : authors) {
            if (comma) stringBuilder.append(", ");
            comma = true;
            stringBuilder.append(author);
        }
        stringBuilder.append("\n" + prefix + "Number of pages: ").append(pages);
        if (description != null) {
            stringBuilder.append("\n" + prefix + "Description: " + description);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return toString("");
    }
}
