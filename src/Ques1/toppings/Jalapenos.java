package Ques1.toppings;

public class Jalapenos extends PizzaToppings {

    public void setPrice() {
        setPrice(50);
    }

    @Override
    public String toString() {
        return "Jalapenos price= Rs." + price;
    }
}
