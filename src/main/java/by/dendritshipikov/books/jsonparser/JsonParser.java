package by.dendritshipikov.books.jsonparser;

import java.util.Collection;

public class JsonParser {

    private String source;
    private int count = 0;

    public JsonParser(String source) {
        this.source = source;
    }

    private void ignoreSpace() {
        while (count < source.length() && Character.isWhitespace(source.charAt(count))) ++count;
    }

    public String parseString() {
        return null;
    }

    public int parseInteger() {
        return 0;
    }

    public double parseDouble() {
        return 0.0;
    }

    public Collection<Object> parseCollection() {
        return null;
    }
}
