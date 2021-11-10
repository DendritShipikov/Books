package by.dendritshipikov.books.jsonmapper.treemodel;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.JsonParserException;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonElement {

    private List<JsonElement> elements = new ArrayList<>();

    public boolean add(JsonElement element) {
        return elements.add(element);
    }

    @Override
    public Object convert(Type type) {
        if (type instanceof Class){
            Class<?> clazz = (Class<?>)type;
            if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                Object array = Array.newInstance(componentType, elements.size());
                for (int i = 0; i < elements.size(); ++i) {
                    Object value = elements.get(i).convert(componentType);
                    Array.set(array, i, value);
                }
                return array;
            }
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Class<?> rawType = (Class<?>)parameterizedType.getRawType();
            if (List.class.isAssignableFrom(rawType)) {
                List<Object> list;
                if (rawType == List.class) {
                    list = new ArrayList<>();
                }
                else {
                    try {
                        Constructor<?> constructor = rawType.getConstructor();
                        list = (List<Object>) constructor.newInstance();
                    }
                    catch (NoSuchMethodException e) {
                        throw new JsonParserException("Converting error: type has not default constructor");
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new JsonParserException("Converting error", e);
                    }
                }
                for (JsonElement element : elements) {
                    Object value = element.convert(parameterizedType.getActualTypeArguments()[0]);
                    list.add(value);
                }
                return list;
            }
        }
        throw new JsonParserException("Converting error");
    }

    @Override
    public void parse(JsonParser parser) {
        elements.clear();
        parser.ignoreSpace();
        parser.match('[');
        parser.ignoreSpace();
        if (parser.peekChar() == ']') {
            parser.getChar();
            return;
        }
        while (true) {
            JsonElement element = parser.parseElement();
            parser.ignoreSpace();
            elements.add(element);
            if (parser.peekChar() != ',') {
                parser.match(']');
                return;
            }
            parser.getChar();
        }
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append('[');
        boolean comma = false;
        for (JsonElement element : elements) {
            if (comma) builder.append(',');
            comma = true;
            element.write(builder);
        }
        builder.append(']');
    }
}
