package com.alpharec.data;

import com.alpharec.pojo.Link;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileReaderTest {
    @Test
    public void testFileRead() {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(1024);
        String file = "/home/neovic/Work/RecommendSystem/DataSet/MovieLens/ml-latest-small/links.csv";

        FileLineReader fr = new FileLineReader(q, file);
        Thread producer = new Thread(fr, "read data");
        producer.start();
        Thread consumer = new Thread(() -> {
            while (producer.isAlive() || !q.isEmpty()) {
                String line = q.poll();
                if (line != null) {
                    Link link = new Link(line);
                    System.out.println(link);
                }
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
