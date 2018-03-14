package Ques4;


public class Fighter1 extends Fighter {

    public Fighter1() {
        setJumpBehaviour();
        setRollBehaviour();
    }

    @Override
    public void setJumpBehaviour() {
        this.jumpBehaviour = new LongJumps();
    }

    @Override
    public void setRollBehaviour() {
        this.rollBehaviour = new FastRoll();
    }
}
