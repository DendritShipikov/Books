package by.dendritshipikov.books.jsonparser;

import by.dendritshipikov.books.jsonparser.treemodel.*;

public class JsonParser {

    private final String source;
    private int count = 0;

    public JsonParser(String source) {
        this.source = source;
    }

    public void ignoreSpace() {
        while (count < source.length() && Character.isWhitespace(source.charAt(count))) ++count;
    }

    public char peekChar() {
        if (count < source.length())
            return source.charAt(count);
        else
            return 0;
    }

    public char getChar() {
        if (count < source.length())
            return  source.charAt(count++);
        else
            return 0;
    }

    public String getChars(int n) {
        int m = Integer.min(n, source.length() - count);
        String str = source.substring(count, count + m);
        count += m;
        return str;
    }

    public void match(char ch) {
        ignoreSpace();
        if (peekChar() != ch) throw new JsonParserException("Parsing error: '" + ch + "' expected");
        getChar();
    }

    public JsonElement parseElement() {
        ignoreSpace();
        JsonElement element;
        switch (peekChar()) {
            case '[':
                element = new JsonArray();
                break;
            case '"':
                element = new JsonString();
                break;
            case '{':
                element = new JsonObject();
                break;
            case '-':
                element = new JsonNumber();
                break;
            case 't':
            case 'f':
                element = new JsonBool();
                break;
            case 'n':
                element = new JsonNull();
                break;
            default:
                if (Character.isDigit(peekChar())) {
                    element = new JsonNumber();
                }
                else {
                    throw new JsonParserException("Parsing error");
                }
        }
        element.parse(this);
        return element;
    }
}
