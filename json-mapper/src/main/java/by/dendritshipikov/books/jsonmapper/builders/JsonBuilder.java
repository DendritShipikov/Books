package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonReader;

abstract public class JsonBuilder {

    abstract public void reset();

    abstract public Object build(JsonReader reader);

    abstract public Object getResult();

}
