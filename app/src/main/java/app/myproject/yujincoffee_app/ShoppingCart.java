package app.myproject.yujincoffee_app;

import java.util.ArrayList;

public class ShoppingCart {
    private static ShoppingCart shoppingCart;
    ArrayList<Drink> drinkItems;
    private ShoppingCart(){
        drinkItems=new ArrayList();

    }
    public static ShoppingCart newInstance(){
        if(shoppingCart==null){
            shoppingCart=new ShoppingCart();

        }
        return shoppingCart;
    }




/*
    public class Drink{
        String name;
        String price;
        String ice;
        String sugar;

        public Drink(String name, String price, String ice, String sugar) {
            this.name = name;
            this.price = price;
            this.ice = ice;
            this.sugar = sugar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIce() {
            return ice;
        }

        public void setIce(String ice) {
            this.ice = ice;
        }

        public String getSugar() {
            return sugar;
        }

        public void setSugar(String sugar) {
            this.sugar = sugar;
        }
    }
    */
}
