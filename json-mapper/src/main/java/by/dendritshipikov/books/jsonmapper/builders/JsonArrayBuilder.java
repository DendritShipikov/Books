package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

abstract public class JsonArrayBuilder extends JsonBuilder {

    abstract public void add(Object e);

    abstract public JsonBuilder getComponentBuilder();

    @Override
    final public Object build(JsonReader reader) {
        return reader.readArray(this);
    }

}
