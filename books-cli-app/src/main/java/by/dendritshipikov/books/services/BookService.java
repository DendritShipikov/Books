package by.dendritshipikov.books.services;

import by.dendritshipikov.books.model.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    boolean existsBook(String title);

    Book getBookByTitle(String title) throws IOException;

    boolean addBook(Book book) throws IOException;

    List<Book> findBook(String query) throws IOException;

    boolean editBook(Book book) throws IOException;

    boolean deleteBook(String title) throws IOException;

}
