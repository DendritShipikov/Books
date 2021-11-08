package by.dendritshipikov.books.jsonparser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeWrapper<T> {

    private Type type;

    protected TypeWrapper() {
        type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type getType() { return type; }

}
