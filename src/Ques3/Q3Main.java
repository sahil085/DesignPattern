package Ques3;

import java.io.IOException;


public class Q3Main {
    public static void main(String[] args) {

        SingletonFileReader singletonFileReader = SingletonFileReader.getInstance();
        try {
            singletonFileReader.readingFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("---------Fetching values---------");
        singletonFileReader.fetchingValues();
        System.out.println("---------------------------------");
        Thread t = new Thread(new MyThread());
        Thread t1 = new Thread(new MyThread2());

        System.out.println("Thread execution (2 threads on same value) : ");
        t.start();
        t1.start();

    }
}
