package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

abstract public class JsonObjectBuilder extends JsonBuilder {

    abstract public void put(String name, Object value);

    abstract public boolean containsField(String name);

    abstract public JsonBuilder getFieldBuilder(String name);

    @Override
    final public Object build(JsonReader reader) {
        return reader.readObject(this);
    }

}
