package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public abstract class JsonObjectBuilder extends JsonBuilder {

    public abstract void put(String name, Object value);

    public abstract boolean containsField(String name);

    public abstract JsonBuilder getFieldBuilder(String name);

    @Override
    final public Object build(JsonReader reader) {
        return reader.readObject(this);
    }

}
