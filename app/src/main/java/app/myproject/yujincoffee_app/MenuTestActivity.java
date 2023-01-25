package app.myproject.yujincoffee_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);

        ShoppingCart a= ShoppingCart.newInstance();
        Drink coffee = new Drink("奶茶","123","冰","0.5");
        a.drinkItems.add(coffee);
        Log.e("購物車",a.drinkItems.get(0).getName()
                +" "+a.drinkItems.get(0).getIce()
                +" "+a.drinkItems.get(0).getSugar()
                +" "+a.drinkItems.get(0).getPrice());

        JSONObject packet=new JSONObject();
        JSONObject OrderMst=new JSONObject();
        JSONArray OrderDetail=new JSONArray();
        JSONObject drink001=new JSONObject();
        JSONObject drink002=new JSONObject();
        try {
            packet.put("OrderMst",OrderMst);
            OrderMst.put("member","黃曉明");
            OrderMst.put("date","20230119");
            drink001.put("飲料id","A001");
            drink001.put("冷熱","冷");
            drink001.put("數量","2");
            Log.e("JSON",drink001.toString());
            OrderDetail.put(drink001);
            drink002.put("飲料id","A002");
            drink002.put("冷熱","冷");
            drink002.put("數量","4");
            OrderDetail.put(drink002);
            OrderMst.put("OrderDetail",OrderDetail);
            Log.e("JSON",packet.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}