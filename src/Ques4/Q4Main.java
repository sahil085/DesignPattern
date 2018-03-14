package Ques4;


public class Q4Main {
    public static void main(String[] args) {
        System.out.println("--------------- Fighter 1 moves ---------------- ");
        Fighter1 fighter1 = new Fighter1();
        fighter1.kick();
        fighter1.punch();
        fighter1.roll();
        fighter1.jump();

        System.out.println("--------------- Fighter 2 moves ---------------- ");
        Fighter2 fighter2 = new Fighter2();
        fighter2.kick();
        fighter2.punch();
        fighter2.roll();
        fighter2.jump();


    }
}
