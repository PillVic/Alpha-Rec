package com.alpharec.data;

import java.io.BufferedReader;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileLineReader implements  Runnable{
    private final BlockingQueue<String> queue;
    private BufferedReader bf;
    public FileLineReader(BlockingQueue<String > q, String path){
        this.queue = q;
        try {
            File f = new File(path);
            this.bf = new BufferedReader(new java.io.FileReader(f));
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void readlines(){
        String line = null;
        try{
        while((line=bf.readLine())!=null){
            queue.add(line);
        }}catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(1024);
        FileLineReader f = new FileLineReader(q, "/home/neovic/daily.sh");
        System.out.println("Hello world");
    }

    @Override
    public void run() {
        this.readlines();
    }
}
