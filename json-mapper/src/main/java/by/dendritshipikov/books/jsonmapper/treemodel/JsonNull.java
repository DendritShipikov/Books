package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.JsonParserException;

import java.lang.reflect.Type;

public class JsonNull extends JsonElement {
    @Override
    public void parse(JsonParser parser) {
        parser.ignoreSpace();
        StringBuilder builder = new StringBuilder();
        while (Character.isLetter(parser.peekChar())) builder.append(parser.getChar());
        String stringRep = builder.toString();
        if (!stringRep.equals("null")) throw new JsonParserException("parsing error");
    }

    @Override
    public Object convert(Type type) {
        return null;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append("null");
    }
}
