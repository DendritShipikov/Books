package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

public abstract class JsonBuilder {

    public abstract void reset();

    public abstract Object build(JsonReader reader);

    public abstract Object getResult();

}
