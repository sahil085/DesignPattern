package Ques3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SingletonFileReader {

    private static SingletonFileReader onlyObject = new SingletonFileReader();
    private ArrayList<Integer> values = new ArrayList<>();

    private SingletonFileReader() {
    }

    public static SingletonFileReader getInstance() {
        return onlyObject;
    }

    public void readingFile() throws IOException {
        FileReader fileReader = new FileReader("a.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String sCurrentLine;
        bufferedReader = new BufferedReader(fileReader);
        while ((sCurrentLine = bufferedReader.readLine()) != null) {
//            System.onut.println(sCurrentLine);
            values.add(Integer.parseInt(sCurrentLine));
        }
    }

    public synchronized void fetchingValues() {
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i));
        }
    }

    public synchronized Integer getValue(int i) {
        if (i >= 0)
            return values.get(i);
        else
            return -1;
    }
}