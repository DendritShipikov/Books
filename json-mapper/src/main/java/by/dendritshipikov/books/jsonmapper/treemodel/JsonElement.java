package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;

import java.lang.reflect.Type;

abstract public class JsonElement {
    abstract public Object convert(Type type);
    abstract public void parse(JsonParser parser);
    abstract public void write(StringBuilder builder);
}
