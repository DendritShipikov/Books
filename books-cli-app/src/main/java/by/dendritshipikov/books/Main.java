package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonparser.JsonParser;
import by.dendritshipikov.books.jsonparser.TypeWrapper;
import by.dendritshipikov.books.jsonparser.treemodel.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = " [{\"b\" : false, \"integers\" : null, \"strings\":[\"a\", \"b\"]  , \"i\":1,\"integer\":2, \"name\": \"lox\"}, null]";
        JsonParser parser = new JsonParser(source);
        JsonElement element = parser.parseElement();
        System.out.println(element.convert(new TypeWrapper<List<A>>(){}.getType()));
    }
}

class A {
    private int i;
    Integer integer;
    String name;
    List<String> strings;
    ArrayList<Integer> integers;
    boolean b;

    public A() {}

    @Override
    public String toString() {
        return "A{" +
                "i=" + i +
                ", integer=" + integer +
                ", name='" + name + '\'' +
                ", strings=" + strings +
                ", integers=" + integers +
                ", b=" + b +
                '}';
    }
}
