package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public abstract class JsonArrayBuilder extends JsonBuilder {

    public abstract void add(Object e);

    public abstract JsonBuilder getComponentBuilder();

    @Override
    public final Object build(JsonReader reader) {
        return reader.readArray(this);
    }

}
