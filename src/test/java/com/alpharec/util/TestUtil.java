package com.alpharec.util;

import com.alpharec.pojo.Link;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);
    public static void testLog(){
        logger.info("[INFO]:hello world");
    }
    @Test
    public  void testToString(){
        Link link = new Link(1, 2, 3);
        System.out.println(link);
    }
    public static void main(String []args){
        TestUtil.testLog();
    }
}
