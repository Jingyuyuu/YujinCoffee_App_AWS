package app.myproject.yujincoffee_app.Part2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Adapter.DrinkMenuAdapter;
import app.myproject.yujincoffee_app.Model.Product.DrinkModel;


import app.myproject.yujincoffee_app.R;
import app.myproject.yujincoffee_app.databinding.ActivityMenuListBinding;

public class MenuListActivity extends AppCompatActivity {
    ActivityMenuListBinding binding;
    ActionBar actionBar;
    DrinkMenuClickListener drinkMenuClickListener;
    SQLiteDatabase db;
    ArrayList<DrinkModel> item;
    Resources resources;
    //用來接收點選spinner裡面是什麼系列字串
    String series=null;
    DrinkMenuAdapter adapter2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMenuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //ActionBar設定
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("返回");
        //spinner建立和設定spinner的adapter
        Spinner spinner = binding.seriesSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.series_array
                , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(spnOnItemSelected);
        //開啟資料庫
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        //清單被點選後的機制傳回來第幾個被點到
        drinkMenuClickListener= new DrinkMenuClickListener() {
            @Override
            public void onClick(int position, int series, String drinkName, int tem, int drinkCalorie, int drinkPrice, int resID) {
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putInt("series",series);
                bundle.putString("drinkName",drinkName);
                bundle.putInt("tem",tem);
                bundle.putInt("drinkCalorie",drinkCalorie);
                bundle.putInt("drinkPrice",drinkPrice);
                bundle.putInt("ResID",resID);
                //從sharePreference裡面撈出資料看是不是登入模式
                //登入才能點進去購物
                sharedPreferences =getSharedPreferences("memberDataPre",MODE_PRIVATE);
                String loginEmail=sharedPreferences.getString("email","查無資料");
                Intent i = new Intent(MenuListActivity.this, ProductOrderActivity.class);
                i.putExtra("data", bundle);
                startActivity(i);

                //從sharePreference裡面撈出資料看是不是登入模式
                //登入才能點進去購物

//                if(!loginEmail.equals("查無資料")&&!loginEmail.equals(null)) {
//                    Intent i = new Intent(MenuListActivity.this, ProductOrderActivity.class);
//                    i.putExtra("data", bundle);
//                    startActivity(i);
//                }


            }
        };
        //給Resources物件賦予位置值
        resources = getResources();
        //給RecycleView設定布局(水平和垂直)
        item=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.drinkmenulist.setLayoutManager(linearLayoutManager);
//        adapter2.getAllProductData();
        binding.drinkmenulist.setAdapter(adapter2);


    }
    private AdapterView.OnItemSelectedListener spnOnItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //parent.getItemAtPosition(position).toString是指例如新式茶飲系列的類別
            series =parent.getItemAtPosition(position).toString();
            adapter2 = new DrinkMenuAdapter(db,drinkMenuClickListener,item,resources);
            //一開始進去是ALL所以先從SQLite撈出全部產品資料，可有可無，因為下面也會撈
            adapter2.getAllProductData();
            if(series!=null) {
                adapter2.setSeries(series);
                adapter2.notifyDataSetChanged();
            }
            binding.drinkmenulist.setAdapter(adapter2);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}