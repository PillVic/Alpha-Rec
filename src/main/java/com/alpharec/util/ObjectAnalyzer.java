package com.alpharec.util;

import com.alpharec.pojo.Link;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectAnalyzer {
    public static <T> String ToString(T t) {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%s{", t.getClass().getName()));
        for (var field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object obj = null;
            try {
                obj = field.get(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                s.append(String.format("%s=%s;", field.getName(), obj));
            }
        }
        s.trimToSize();
        s.append("}");
        return s.toString();
    }

    public static void main(String[] args) {
        Link link = new Link(1, 2, 3);
        System.out.println(link);
    }
}
