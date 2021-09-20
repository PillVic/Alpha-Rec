package com.alpharec.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);
    public static void testLog(){
        logger.info("[INFO]:hello world");
    }
    public static void main(String []args){
        TestUtil.testLog();
    }
}
