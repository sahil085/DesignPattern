package Ques4;


public abstract class Fighter {

    RollBehaviour rollBehaviour;
    JumpBehaviour jumpBehaviour;

    public void kick() {
        System.out.println("Fighter is kicking.");
    }

    public void punch() {
        System.out.println("Fighter punches.");
    }

    public void jump() {
        jumpBehaviour.jump();
    }

    public void roll() {
        rollBehaviour.roll();
    }

    public abstract void setJumpBehaviour();

    public abstract void setRollBehaviour();
}
