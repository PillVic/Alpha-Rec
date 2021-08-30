package com.alpharec.data;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/*主要用于进行数据导入的公共部分导入部分抽象
 * */
public class Handler implements Runnable {
    private final int blockSize = 10240;

    private final BlockingQueue<String> queue;
    private final FileLineReader fr;
    private final Consumer<String> consumer;

    public Handler(String fileName, Consumer<String> consumer) {
        this.queue = new ArrayBlockingQueue<>(blockSize);
        fr = new FileLineReader(queue, fileName);
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Thread producer = new Thread(fr, "read data file");
        producer.start();
        Thread consumerThread = new Thread(() -> {
            while (producer.isAlive() || !queue.isEmpty()) {
                String line = queue.poll();
                consumer.accept(line);
            }
        }, "parse data");
        consumerThread.start();
        try {
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
