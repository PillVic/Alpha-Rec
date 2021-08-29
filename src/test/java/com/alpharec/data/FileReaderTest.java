package com.alpharec.data;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileReaderTest {
    @Test
    public void testFileRead() {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(1024);
        String file = "/home/neovic/daily.sh";
        FileLineReader fr = new FileLineReader(q, file);
        Thread producer = new Thread(fr, "read data");
        producer.start();
        Thread consumer = new Thread(()->{
            while(producer.isAlive() || !q.isEmpty()){
                String line = q.poll();
                System.out.println(line);
            }
        });
        consumer.start();
        try {
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
