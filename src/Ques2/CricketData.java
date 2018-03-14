package Ques2;

import java.util.ArrayList;
import java.util.List;


public class CricketData implements Subject {

    private int runs;
    private int bowls;

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int i = observers.indexOf(observer);
        if (i >= 0)
            observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(runs, bowls);
        }
    }

    public void dataChanged() {
        notifyObserver();
    }

    public void setData(int runs, int bowls) {
        setBowls(bowls);
        setRuns(runs);
        dataChanged();
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBowls() {
        return bowls;
    }

    public void setBowls(int bowls) {
        this.bowls = bowls;
    }


}
