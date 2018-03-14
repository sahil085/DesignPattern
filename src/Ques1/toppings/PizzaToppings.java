package Ques1.toppings;

import Ques1.pizza.Pizza;

public abstract class PizzaToppings {

    int price;

    public Pizza addTopping(Pizza pizza) {
        pizza.setPrice(pizza.getPrice() + this.price);
        return pizza;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public abstract void setPrice();
}
