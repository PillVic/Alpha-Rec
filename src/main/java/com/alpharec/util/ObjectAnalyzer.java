package com.alpharec.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;

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

    public static <T> int HashCode(T t) {
        return ToString(t).hashCode();
    }

    public static <T> List<T> getTopN(int n, List<T> list, Comparator<T> comparator) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().sorted(comparator)
                .limit(n).collect(Collectors.toList());
    }

    public static <T> List<List<T>> SubList(List<T> list, int subSize) {
        List<List<T>> r = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return r;
        }
        for (int i = 0; i < list.size(); i += subSize) {
            r.add(list.subList(i, min(i + subSize, list.size())));
        }
        return r;
    }
}
