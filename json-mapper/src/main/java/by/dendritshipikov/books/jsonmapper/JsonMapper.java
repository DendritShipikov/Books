package by.dendritshipikov.books.jsonmapper;

import by.dendritshipikov.books.jsonmapper.builders.JsonBoolBuilder;
import by.dendritshipikov.books.jsonmapper.builders.JsonBuilder;
import by.dendritshipikov.books.jsonmapper.builders.JsonBuilders;
import by.dendritshipikov.books.jsonmapper.builders.JsonStringBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonMapper {

    private static void toJson(Object obj, StringBuilder builder) {
        if (obj == null) {
            builder.append("null");
        } else if (Number.class.isAssignableFrom(obj.getClass())) {
            builder.append(obj.toString());
        } else if (obj.getClass() == String.class) {
            String string = (String) obj;
            builder.append('"');
            for (int i = 0; i < string.length(); ++i) {
                switch (string.charAt(i)) {
                    case '\n':
                        builder.append("\\n");
                        break;
                    case '\r':
                        builder.append("\\r");
                        break;
                    case '\t':
                        builder.append("\\t");
                        break;
                    case '\f':
                        builder.append("\\f");
                        break;
                    default:
                        builder.append(string.charAt(i));
                        break;
                }
            }
            builder.append('"');
        } else if (obj instanceof Iterable<?>) {
            Iterable<Object> iterable = (Iterable<Object>) obj;
            builder.append('[');
            boolean comma = false;
            for (Object item : iterable) {
                if (comma) builder.append(',');
                comma = true;
                toJson(item, builder);
            }
            builder.append(']');
        } else if (obj.getClass().isArray()) {
            builder.append('[');
            int length = Array.getLength(obj);
            for (int i = 0; i < length; ++i) {
                if (i != 0) builder.append(',');
                Object value = Array.get(obj, i);
                toJson(value, builder);
            }
            builder.append(']');
        } else {
            builder.append('{');
            Field[] fields = obj.getClass().getDeclaredFields();
            boolean comma = false;
            for (Field field : fields) {
                field.setAccessible(true);
                if (comma) builder.append(',');
                comma = true;
                toJson(field.getName(), builder);
                builder.append(':');
                try {
                    Object value = field.get(obj);
                    toJson(value, builder);
                } catch (IllegalAccessException e) {
                    throw new JsonException("Error in serialization", e);
                }
            }
            builder.append('}');
        }
    }

    public static String toJson(Object obj) {
        StringBuilder builder = new StringBuilder();
        toJson(obj, builder);
        return builder.toString();
    }

    public static JsonBuilder getBuilder(Type type) {
        if (type == Integer.TYPE || type == Integer.class) return new JsonBuilders.IntegerBuilder();
        if (type == Double.TYPE || type == Double.class) return new JsonBuilders.DoubleBuilder();
        if (type == Boolean.TYPE || type == Boolean.class) return new JsonBoolBuilder();
        if (type == String.class) return new JsonStringBuilder();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) parameterizedType.getRawType();
            if (List.class.isAssignableFrom(clazz)) {
                return new JsonBuilders.ListBuilder((Class<? extends List<?>>) clazz, parameterizedType.getActualTypeArguments()[0]);
            }
        } else if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                return new JsonBuilders.ArrayBuilder(clazz.getComponentType());
            }
            return new JsonBuilders.ObjectBuilder(clazz);
        }
        throw new JsonException("No suitable builder for type " + type.getTypeName());
    }

    public static <T> T fromJson(String source, Type type) {
        JsonBuilder builder = getBuilder(type);
        JsonReader reader = new JsonReader(source);
        Object obj = builder.build(reader);
        return (T)obj;
    }
}
