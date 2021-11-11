package by.dendritshipikov.books.jsonmapper.builders;

import by.dendritshipikov.books.jsonmapper.JsonException;
import by.dendritshipikov.books.jsonmapper.JsonMapper;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBuilders {

    public static class IntegerBuilder extends JsonNumberBuilder {

        private int value;

        @Override
        public void setValue(String rep) {
            try {
                value = Integer.parseInt(rep);
            } catch (NumberFormatException e) {
                throw new JsonException("Number " + rep + " can not be converted to integer");
            }
        }

        @Override
        public void reset() {}

        @Override
        public Object getResult() {
            return value;
        }
    }

    public static class DoubleBuilder extends JsonNumberBuilder {

        private double value;

        @Override
        public void setValue(String rep) {
            try {
                value = Double.parseDouble(rep);
            } catch (NumberFormatException e) {
                throw new JsonException("Number " + rep + " can not be converted to double");
            }
        }

        @Override
        public void reset() {}

        @Override
        public Object getResult() {
            return value;
        }
    }

    public static class ArrayBuilder extends JsonArrayBuilder {

        private final List<Object> list = new ArrayList<>();
        private final Class<?> componentType;

        public ArrayBuilder(Class<?> componentType) {
            this.componentType = componentType;
        }

        @Override
        public void add(Object e) {
            list.add(e);
        }

        @Override
        public JsonBuilder getComponentBuilder() {
            return JsonMapper.getBuilder(componentType);
        }

        @Override
        public void reset() {
            list.clear();
        }

        @Override
        public Object getResult() {
            Object array = Array.newInstance(componentType, list.size());
            int i = 0;
            for (Object e : list) {
                Array.set(array, i, e);
                ++i;
            }
            return array;
        }
    }

    public static class ListBuilder extends JsonArrayBuilder {

        private List<Object> list;
        private final Class<? extends List<?>> clazz;
        private final Type componentType;

        public ListBuilder(Class<? extends List<?>> clazz, Type componentType) {
            this.clazz = clazz;
            this.componentType = componentType;
        }

        @Override
        public void add(Object e) {
            list.add(e);
        }

        @Override
        public JsonBuilder getComponentBuilder() {
            return JsonMapper.getBuilder(componentType);
        }

        @Override
        public void reset() {
            if ((Class<?>)clazz == List.class) {
                list = new ArrayList<>();
                return;
            }
            try {
                Constructor<? extends List<?>> constructor = clazz.getConstructor();
                list = (List<Object>) constructor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new JsonException("Object of class " + clazz.getName() + " can not be created", e);
            } catch (NoSuchMethodException e) {
                throw new JsonException("Class " + clazz.getName() + " has no default constructor");
            }
        }

        @Override
        public Object getResult() {
            return list;
        }
    }

    public static class ObjectBuilder extends JsonObjectBuilder {

        private final Class<?> clazz;
        private final Map<String, Field> fieldMap = new HashMap<>();
        private final Map<String, Object> map = new HashMap<>();

        public ObjectBuilder(Class<?> clazz) {
            this.clazz = clazz;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                fieldMap.put(field.getName(), field);
            }
        }

        @Override
        public boolean containsField(String name) {
            return map.containsKey(name);
        }

        @Override
        public void put(String name, Object value) {
            map.put(name, value);
        }

        @Override
        public JsonBuilder getFieldBuilder(String name) {
            if (!fieldMap.containsKey(name)) throw new JsonException("Class " + clazz.getName() + " hasn't field '" + name + "'");
            return JsonMapper.getBuilder(fieldMap.get(name).getGenericType());
        }

        @Override
        public void reset() {
            map.clear();
        }

        @Override
        public Object getResult() {
            if (fieldMap.size() != map.size()) throw new JsonException("Class " + clazz.getName() + " contains more fields than proposed");
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object obj = constructor.newInstance();
                for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    field.set(obj, map.get(entry.getKey()));
                }
                return obj;
            } catch (NoSuchMethodException e) {
                throw new JsonException("Class " + clazz.getName() + " has no default constructor");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new JsonException("Object of class " + clazz.getName() + " can not be created", e);
            }
        }
    }

}
