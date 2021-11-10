package by.dendritshipikov.books.jsonmapper;

import by.dendritshipikov.books.jsonmapper.treemodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class JsonMapper {
    public static <T> T convert(String source, Type type) {
        JsonParser parser = new JsonParser(source);
        JsonElement element = parser.parseElement();
        return (T)element.convert(type);
    }
    public static JsonElement toJsonElement(Object obj) {
        if (obj == null) return new JsonNull();
        if (obj instanceof Number) {
            return new JsonNumber((Number) obj);
        } else if (obj instanceof String) {
            return new JsonString((String) obj);
        } else if (obj instanceof Boolean) {
            return new JsonBool((Boolean) obj);
        } else if (obj instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) obj;
            JsonArray array = new JsonArray();
            for (Object item : iterable) {
                array.add(toJsonElement(item));
            }
            return array;
        }
        Class<?> clazz = obj.getClass();
        JsonObject jsonObject = new JsonObject();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldObj = field.get(obj);
                JsonElement fieldElement = toJsonElement(fieldObj);
                jsonObject.put(new JsonString(field.getName()), fieldElement);
            }
        } catch (IllegalAccessException e) {
            throw new JsonParserException("Converting error", e);
        }
        return jsonObject;
    }
    public static String toJson(Object obj) {
        JsonElement element = toJsonElement(obj);
        StringBuilder builder = new StringBuilder();
        element.write(builder);
        return builder.toString();
    }
}
