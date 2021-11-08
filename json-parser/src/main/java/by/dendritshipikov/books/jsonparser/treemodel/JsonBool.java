package by.dendritshipikov.books.jsonparser.treemodel;

import by.dendritshipikov.books.jsonparser.JsonParser;
import by.dendritshipikov.books.jsonparser.JsonParserException;

import java.lang.reflect.Type;

public class JsonBool extends JsonElement {
    private boolean value;

    @Override
    public Object convert(Type type) {
        if (type == Boolean.class || type == boolean.class) return value;
        throw new JsonParserException("converting error");
    }

    @Override
    public void parse(JsonParser parser) {
        parser.ignoreSpace();
        StringBuilder builder = new StringBuilder();
        while (Character.isLetter(parser.peekChar())) builder.append(parser.getChar());
        String stringRep = builder.toString();
        if (stringRep.equals("true")) value = true;
        else if (stringRep.equals("false")) value = false;
        else throw new JsonParserException("parsing error");
    }
}
