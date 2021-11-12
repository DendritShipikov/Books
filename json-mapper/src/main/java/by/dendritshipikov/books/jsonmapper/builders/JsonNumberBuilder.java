package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public abstract class JsonNumberBuilder extends JsonBuilder {

    public abstract void setValue(String rep);

    @Override
    public final Object build(JsonReader reader) {
        return reader.readNumber(this);
    }
}
