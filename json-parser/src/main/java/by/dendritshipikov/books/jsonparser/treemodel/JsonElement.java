package by.dendritshipikov.books.jsonparser.treemodel;

import by.dendritshipikov.books.jsonparser.JsonParser;

import java.lang.reflect.Type;

abstract public class JsonElement {
    abstract public Object convert(Type type);
    abstract public void parse(JsonParser parser);
}
