package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public class JsonBoolBuilder extends JsonBuilder {
    private boolean value;

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void reset() {}

    @Override
    public Object getResult() {
        return value;
    }

    @Override
    public final Object build(JsonReader reader) {
        return reader.readBool(this);
    }
}
