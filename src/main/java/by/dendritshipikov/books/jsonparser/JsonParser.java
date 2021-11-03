package by.dendritshipikov.books.jsonparser;

import java.util.Collection;
import java.util.List;

public class JsonParser {

    private String source;
    private int count = 0;

    public JsonParser(String source) {
        this.source = source;
    }

    private void ignoreSpace() {
        while (count < source.length() && Character.isWhitespace(source.charAt(count))) ++count;
    }

    private char peekChar() {
        if (count < source.length())
            return source.charAt(count);
        else
            return 0;
    }

    private char getChar() {
        if (count < source.length())
            return  source.charAt(count++);
        else
            return 0;
    }

    private JsonParserException parserError(String msg) {
        return new JsonParserException("JsonParser error " + msg);
    }

    public String parseString() {
        ignoreSpace();
        if (source.charAt(count) != '"') throw parserError("string should start with '\"'");
        getChar();
        StringBuilder builder = new StringBuilder();
        while (true) {
            char current = peekChar();
            if (current < '\u0020') throw parserError("wrong symbol \\0x" + Integer.toHexString((int)current) + " in string");
            switch (current) {
                case '\\':
                    getChar();
                    switch (peekChar()) {
                        case '\\':
                            builder.append('\\');
                            break;
                        case '/':
                            builder.append('/');
                            break;
                        case '"':
                            builder.append('"');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 'f':
                            builder.append('\f');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case 'u':
                            getChar();
                            if (count + 4 >= source.length()) throw parserError("wrong escape sequence format");
                            String hex = source.substring(count, count + 4);
                            count += 3;
                            int code;
                            try {
                                code = Integer.parseUnsignedInt(hex, 16);
                            }
                            catch (NumberFormatException e) {
                                throw parserError("wrong escape sequence format");
                            }
                            builder.append((char)code);
                            break;
                    }
                    getChar();
                    break;
                case '"':
                    getChar();
                    return builder.toString();
                default:
                    builder.append(current);
                    getChar();
                    break;
            }
        }
    }

    public int parseInteger() {
        return 0;
    }

    public double parseDouble() {
        return 0.0;
    }

    public List<Object> parseList() {
        return null;
    }
}
