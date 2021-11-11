package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public class JsonStringBuilder extends JsonBuilder {

    private StringBuilder builder = new StringBuilder();

    public void append(char c) {
        builder.append(c);
    }

    @Override
    public void reset() {
        builder = new StringBuilder();
    }

    @Override
    public Object getResult() {
        return builder.toString();
    }

    @Override
    final public Object build(JsonReader reader) {
        return reader.readString(this);
    }
}
