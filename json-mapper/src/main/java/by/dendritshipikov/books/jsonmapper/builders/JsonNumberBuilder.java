package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

abstract public class JsonNumberBuilder extends JsonBuilder {

    abstract public void setValue(String rep);

    @Override
    public Object build(JsonReader reader) {
        return reader.readNumber(this);
    }
}
