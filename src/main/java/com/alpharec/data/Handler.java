package com.alpharec.data;

import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;

/*主要用于进行数据导入的公共部分导入部分抽象
* */
public class Handler implements Runnable{
    private Queue<String> q;
    private FileLineReader fr;
    private LineParser lp;
    @Override
    public void run() {
        Thread producer = new Thread(fr, "read data file");
        producer.start();
        Thread consumer = new Thread(()->{
            while(producer.isAlive() ||!q.isEmpty()){
                String line = q.poll();
                lp.parse(line);
            }
        }, "parse data");
        try {
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
