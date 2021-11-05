package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonparser.JsonParser;

public class Main {
    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser(" 12 ");
        System.out.println(jsonParser.parseInteger());
    }
}
