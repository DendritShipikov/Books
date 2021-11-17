package by.dendritshipikov.books.services;

import by.dendritshipikov.books.jsonmapper.JsonMapper;
import by.dendritshipikov.books.model.Book;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BookServiceImpl implements BookService {

    private final File folder;

    public BookServiceImpl(File folder) {
        this.folder = folder;
    }

    @Override
    public boolean existsBook(String title) {
        File file = new File(folder, title + ".json");
        return file.exists();
    }

    @Override
    public Book getBookByTitle(String title) throws IOException {
        File file = new File(folder, title + ".json");
        if (!file.exists()) return null;
        String json = new String(Files.readAllBytes(file.toPath()));
        return JsonMapper.fromJson(json, Book.class);
    }

    @Override
    public boolean addBook(Book book) throws IOException {
        File file = new File(folder, book.getTitle() + ".json");
        if (file.exists()) {
            return false;
        }
        FileWriter writer = new FileWriter(file);
        String json = JsonMapper.toJson(book);
        writer.append(json);
        writer.close();
        return true;
    }

    @Override
    public List<Book> findBook(String query) throws IOException {
        String[] fileTitles = folder.list();
        String regex = "^" + query + ".*$";
        List<Book> books = new ArrayList<>();
        for (String fileTitle : fileTitles) {
            if (Pattern.matches(regex, fileTitle)) {
                String json = new String(Files.readAllBytes(new File(folder, fileTitle).toPath()));
                Book book = JsonMapper.fromJson(json, Book.class);
                books.add(book);
            }
        }
        return books;
    }

    @Override
    public boolean editBook(Book book) throws IOException {
        File file = new File(folder, book.getTitle() + ".json");
        if (!file.exists()) {
            return false;
        }
        String json = JsonMapper.toJson(book);
        FileWriter writer = new FileWriter(file);
        writer.append(json);
        writer.close();
        return true;
    }

    @Override
    public boolean deleteBook(String title) throws IOException {
        File file = new File(folder, title + ".json");
        return file.delete();
    }
}
