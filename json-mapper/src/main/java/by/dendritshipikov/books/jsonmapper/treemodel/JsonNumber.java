package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.JsonParserException;

import java.lang.reflect.Type;

public class JsonNumber extends JsonElement {

    private Number value = 0;

    public JsonNumber() {}

    public JsonNumber(Number value) {
        this.value = value;
    }

    @Override
    public Object convert(Type type) {
        if (type == Integer.class || type == int.class) {
            if (value instanceof Integer) return (Integer)value;
        }
        if (type == Double.class || type == double.class) {
            if (value instanceof Double) return (Double)value;
        }
        throw new JsonParserException("Json number can not be converted to " + type.getTypeName());
    }

    @Override
    public void parse(JsonParser parser) {
        parser.ignoreSpace();
        StringBuilder builder = new StringBuilder();
        if (parser.peekChar() == '-') builder.append(parser.getChar());
        if (parser.peekChar() == '0') {
            builder.append(parser.getChar());
        } else if (Character.isDigit(parser.peekChar())) {
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        } else {
            throw new JsonParserException("Parsing error: wrong number format");
        }
        boolean decimal = false;
        if (parser.peekChar() == '.') {
            decimal = true;
            builder.append(parser.getChar());
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        }
        if (parser.peekChar() == 'e' || parser.peekChar() == 'E') {
            decimal = true;
            builder.append(parser.getChar());
            if (parser.peekChar() == '+' || parser.peekChar() == '-') builder.append(parser.getChar());
            if (!Character.isDigit(parser.peekChar())) throw new JsonParserException("Parsing error: wrong number format");
            while (Character.isDigit(parser.peekChar())) builder.append(parser.getChar());
        }
        String stringRep = builder.toString();
        if (decimal) value = Double.valueOf(stringRep);
        else value = Integer.valueOf(stringRep);
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value.toString());
    }
}
