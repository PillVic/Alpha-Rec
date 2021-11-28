package com.alpharec.util;

import com.alpharec.pojo.Link;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.alpharec.util.ObjectAnalyzer.SubList;
import static com.alpharec.util.ObjectAnalyzer.getTopN;

public class TestUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);

    @Test
    public void testLog() {
        logger.info("[INFO]:hello world");
    }

    @Test
    public void testToString() {
        Link link = new Link(1, 2, 3);
        System.out.println(link);
    }

    @Test
    public void testSubList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        for (var i : SubList(list, 14)) {
            logger.info("[INFO]:subList:{}, size:{}", i, i.size());
        }
    }

    @Test
    public void testTopN() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        //排序函数无意义，仅仅是为了检查是否可传入自定义排序函数
        System.out.println(getTopN(10, list, (a, b) -> {
            if (a * b > 10) {
                return Integer.compare(a, b);
            } else {
                return Integer.compare(b, a);
            }
        }));
    }
}
