package by.dendritshipikov.books.jsonparser.treemodel;

import by.dendritshipikov.books.jsonparser.JsonParser;
import by.dendritshipikov.books.jsonparser.JsonParserException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonObject extends JsonElement {

    Map<JsonString, JsonElement> members = new HashMap<>();

    @Override
    public void parse(JsonParser parser) {
        members.clear();
        parser.ignoreSpace();
        parser.match('{');
        parser.ignoreSpace();
        if (parser.peekChar() == '}') {
            parser.getChar();
            return;
        }
        while (true) {
            JsonString key = new JsonString();
            key.parse(parser);
            parser.match(':');
            JsonElement value = parser.parseElement();
            parser.ignoreSpace();
            members.put(key, value);
            if (parser.peekChar() != ',') {
                parser.match('}');
                return;
            }
            parser.getChar();
        }
    }

    @Override
    public Object convert(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>)type;
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object obj = constructor.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                if (fields.length != members.size()) throw new JsonParserException("converting error: number of fields differs");
                for (Field field : fields) {
                    field.setAccessible(true);
                    JsonString key = new JsonString(field.getName());
                    if (!members.containsKey(key)) throw new JsonParserException("converting error: no such field");
                    JsonElement element = members.get(key);
                    Type fieldType = field.getGenericType();
                    Object value = element.convert(fieldType);
                    field.set(obj, value);
                }
                return obj;
            } catch (NoSuchMethodException e) {
                throw new JsonParserException("converting error: type has not default constructor");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new JsonParserException("converting error", e);
            }
        }
        throw new JsonParserException("converting error: type must be class");
    }
}
