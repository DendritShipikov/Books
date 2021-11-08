package by.dendritshipikov.books.jsonparser.treemodel;

import by.dendritshipikov.books.jsonparser.JsonParser;
import by.dendritshipikov.books.jsonparser.JsonParserException;

import java.lang.reflect.Type;

public class JsonString extends JsonElement {

    private String string;

    public JsonString() {}

    public JsonString(String string) {
        this.string = string;
    }

    @Override
    public Object convert(Type type) {
        if (type != String.class) throw new JsonParserException("Json string can not be converted to " + type.getTypeName());
        return string;
    }

    @Override
    public void parse(JsonParser parser) {
        parser.match('"');
        StringBuilder builder = new StringBuilder();
        while (true) {
            char current = parser.peekChar();
            if (current < '\u0020') throw new JsonParserException("Parsing error: wrong symbol \\0x" + Integer.toHexString((int)current) + " in string");
            switch (current) {
                case '\\':
                    parser.getChar();
                    switch (parser.peekChar()) {
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
                            parser.getChar();
                            String hex = parser.getChars(4);
                            if (hex.length() != 4) throw new JsonParserException("Parsing error: wrong escape sequence format");
                            int code;
                            try {
                                code = Integer.parseUnsignedInt(hex, 16);
                            }
                            catch (NumberFormatException e) {
                                throw new JsonParserException("Parsing error: wrong escape sequence format");
                            }
                            builder.append((char)code);
                            break;
                    }
                    parser.getChar();
                    break;
                case '"':
                    parser.getChar();
                    string = builder.toString();
                    return;
                default:
                    builder.append(current);
                    parser.getChar();
                    break;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonString) {
            JsonString other = (JsonString)obj;
            return string.equals(other.string);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
