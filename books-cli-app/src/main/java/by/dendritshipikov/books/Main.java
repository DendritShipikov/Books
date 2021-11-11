package by.dendritshipikov.books;

import by.dendritshipikov.books.jsonmapper.JsonMapper;
import by.dendritshipikov.books.jsonmapper.TypeWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        String json = JsonMapper.toJson(a);
        System.out.println(json);
        //List<A> aa = JsonMapper.fromJson("[{\"i\": 0, \"author\":[]}, null]", new TypeWrapper<List<A>>(){}.getType());
        A aa = JsonMapper.fromJson(json, A.class);
        System.out.println(aa);
    }
}

class B {
    boolean b;
}

class A extends B {
    int i;
    String[] author = {"Chehov", "Gus"};

    public A() {}

    @Override
    public String toString() {
        return "A{" +
                "i=" + i +
                ", author=" + Arrays.toString(author) +
                '}';
    }
}
