package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.JsonParserException;

import java.lang.reflect.Type;

public class JsonNumber extends JsonElement {

    private String stringRep;

    @Override
    public Object convert(Type type) {
        if (type == Integer.class || type == int.class) return Integer.valueOf(stringRep);
        if (type == Double.class || type == double.class) return Double.valueOf(stringRep);
        throw new JsonParserException("Json number can not be converted to " + type.getTypeName());
    }

    @Override
    public void parse(JsonParser parser) {
        parser.ignoreSpace();
        StringBuilder builder = new StringBuilder();
        if (parser.peekChar() == '-') builder.append(parser.getChar());
        if (parser.peekChar() == '0') {
            builder.append(parser.getChar());
        }
        else if (Character.isDigit(parser.peekChar())) {
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        } else {
            throw new JsonParserException("Parsing error: wrong number format");
        }
        if (parser.peekChar() == '.') {
            builder.append(parser.getChar());
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        }
        if (parser.peekChar() == 'e' || parser.peekChar() == 'E') {
            builder.append(parser.getChar());
            if (parser.peekChar() == '+' || parser.peekChar() == '-') builder.append(parser.getChar());
            if (!Character.isDigit(parser.peekChar())) throw new JsonParserException("Parsing error: wrong number format");
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        }
        stringRep = builder.toString();
    }
}
