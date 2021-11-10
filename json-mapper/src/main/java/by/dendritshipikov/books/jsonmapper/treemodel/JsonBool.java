package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.JsonParserException;

import java.lang.reflect.Type;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonBool) {
            JsonBool other = (JsonBool) obj;
            return other.value == this.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}
