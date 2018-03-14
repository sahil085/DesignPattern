package Ques2;


public class CurrentScoreDisplay implements Observer {

    Subject subject = new CricketData();

    public CurrentScoreDisplay(Subject subject) {
        this.subject = subject;
        subject.registerObserver(this);
    }

    @Override
    public void update(int runs, int bowls) {
        System.out.println("-------------- C U R R E N T   S C O R E -----------");
        System.out.println("Runs : " + runs);
        System.out.println("Bowls : " + bowls);
//        System.out.println("Score : "+score);
    }
}
