package Ques1;

import Ques1.pizza.PeppyPaneer;
import Ques1.pizza.Pizza;
import Ques1.toppings.*;

import java.util.ArrayList;
import java.util.List;


public class Q1Main {
    public static void main(String[] args) {

//      Creating toppings
        PizzaToppings barbeque = new Barbeque();
        PizzaToppings jalapenos = new Jalapenos();
        PizzaToppings paneer = new Paneer();
        PizzaToppings tomato = new Tomato();

        List<PizzaToppings> pizzaToppings = new ArrayList<>();
        pizzaToppings.add(barbeque);
        pizzaToppings.add(jalapenos);
        pizzaToppings.add(paneer);
        pizzaToppings.add(tomato);

//      Setting price of each topping
        for (int i = 0; i < pizzaToppings.size(); i++) {
            PizzaToppings toppings = pizzaToppings.get(i);
            toppings.setPrice();
        }


//      Toppings menu
        System.out.println("------ T O P P I N G S ------");
        for (int i = 0; i < pizzaToppings.size(); i++) {
            PizzaToppings toppings = pizzaToppings.get(i);
            System.out.println(toppings);
        }


        System.out.println("********* B I L L *********");
        System.out.println("Ordering peppy paneer ...");
        Pizza pizza = new PeppyPaneer();
        pizza.setPrice(300);
        System.out.println("Base price : " + pizza.getPrice());

        System.out.println("Adding barbeque topping ...");
        barbeque.addTopping(pizza);
        System.out.println("Total price : " + pizza.getPrice());

        System.out.println("Adding paneer topping ...");
        paneer.addTopping(pizza);
        System.out.println("Total price : " + pizza.getPrice());


        System.out.println("Adding Tomato topping ...");
        tomato.addTopping(pizza);
        System.out.println("Total price : " + pizza.getPrice());

    }

}

