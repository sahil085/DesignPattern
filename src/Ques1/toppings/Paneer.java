package Ques1.toppings;

public class Paneer extends PizzaToppings {

    public void setPrice() {
        setPrice(120);
    }

    @Override
    public String toString() {
        return "Paneer price= Rs." + price;
    }
}
