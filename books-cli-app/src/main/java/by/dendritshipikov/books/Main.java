package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonmapper.JsonMapper;
import by.dendritshipikov.books.jsonmapper.TypeWrapper;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        a.a = new A();
        List<A> list = new ArrayList<>();
        list.add(null);
        list.add(a);
        String source = JsonMapper.toJson(list);//"{ \"i\" : 0, \"name\" : \"Dendrit\", \"strings\" : null, \"integers\" : [1,2 ], \"b\" : true, \"integer\" : null  }";
        System.out.println(source);
        list = JsonMapper.convert(source, new TypeWrapper<List<A>>(){}.getType());
        System.out.println(list);
    }
}

class A {
    A a;
    private int i;
    List<String> strings = new ArrayList<>();
    boolean b;

    public A() {}

    @Override
    public String toString() {
        return "A{" +
                "a=" + a +
                ", i=" + i +
                ", strings=" + strings +
                ", b=" + b +
                '}';
    }
}
