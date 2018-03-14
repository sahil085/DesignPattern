package Ques1.toppings;


public class Tomato extends PizzaToppings {
    @Override
    public void setPrice() {
        setPrice(100);
    }

    @Override
    public String toString() {
        return "Tomato price= Rs." + price;
    }
}
