package Ques2;


public class AverageScoreDisplay implements Observer {

    Subject subject;

    public AverageScoreDisplay(Subject subject) {
        this.subject = subject;
        subject.registerObserver(this);
    }

    @Override
    public void update(int runs, int bowls) {
        System.out.println("-------- A V E R A G E    S C O R E    D I S P L A Y --------");
        float avg = (float) runs / bowls;
        System.out.println("Average runs : " + avg);
    }
}
