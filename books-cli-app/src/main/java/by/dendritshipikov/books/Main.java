package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonmapper.JsonParser;
import by.dendritshipikov.books.jsonmapper.TypeWrapper;
import by.dendritshipikov.books.jsonmapper.treemodel.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = "{ \"i\" : 12, \"name\" : \"Dendrit\", \"strings\" : null, \"integers\" : [1,2 ], \"b\" : true, \"integer\" : null  }";
        JsonParser parser = new JsonParser(source);
        JsonElement element = parser.parseElement();
        A a = (A) element.convert(A.class);
        System.out.println(a);
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
