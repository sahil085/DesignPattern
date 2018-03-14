package Ques3;

public class MyThread implements Runnable {

    @Override
    public void run() {
        SingletonFileReader singletonFileReader = SingletonFileReader.getInstance();
        Integer val = singletonFileReader.getValue(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(val);
    }
}
