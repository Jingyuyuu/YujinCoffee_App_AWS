package app.myproject.yujincoffee_app.Part2;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.DrinkModel;

public interface OrderListListener {
    void onClick(int position, int series, String drinkName, int tem,
                 int drinkCalorie, int drinkPrice, int resID, ArrayList<DrinkModel> seriesData);
}
