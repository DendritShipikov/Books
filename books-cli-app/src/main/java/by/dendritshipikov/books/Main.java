package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonparser.JsonParser;

public class Main {
    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser(" \"abcd\\ne\\u0020fg\" ");
        System.out.println(jsonParser.parseString());
    }
}
