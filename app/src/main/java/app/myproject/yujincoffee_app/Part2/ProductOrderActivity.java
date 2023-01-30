package app.myproject.yujincoffee_app.Part2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import app.myproject.yujincoffee_app.Adapter.OrderListAdapter;
import app.myproject.yujincoffee_app.HistoryOrderActivity;
import app.myproject.yujincoffee_app.Model.Product.DrinkModel;
import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.MyFavoriteActivity;
import app.myproject.yujincoffee_app.MyOrderActivity;
import app.myproject.yujincoffee_app.PointChangeActivity;
import app.myproject.yujincoffee_app.R;
import app.myproject.yujincoffee_app.databinding.ActivityProductOrderBinding;
import app.myproject.yujincoffee_app.logPageActivity;
import app.myproject.yujincoffee_app.memberdataaPageActivity;
import app.myproject.yujincoffee_app.storelistActivity;

public class ProductOrderActivity extends AppCompatActivity {
    ActivityProductOrderBinding binding;
    SharedPreferences memberDataSharePre;
    private SQLiteDatabase db;
    private ArrayList<DrinkModel> productSeries;

    private String createTable="create table if not exists tempProductOrder("+
            "_id integer"+" PRIMARY KEY AUTOINCREMENT,"+
            "shopName text,"+
            "shopTem integer,"+
            "shopSugar text,"+
            "shopIce text,"+
            "shopAmount integer,"+
            "shopPrice integer,"+
            "date text"+
            ");";

    private OrderListListener listener;
    private OrderListAdapter adapter;
    private CheckBox[] checkBoxesSugar = new CheckBox[4];
    private CheckBox [] checkBoxesIce= new CheckBox[5];



    //這裡設定傳輸給購物車的產品屬性
    private int shopPrice;
    private int shopCalorie;
    private String shopName;
    private String shopSugar=null;
    private String shopIce=null;
    private int shopAmount;
    private int shopTem;


    int resID=0;
    Resources resources=null;
    int [] resArray=new int[74];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //接收MenuListActivity傳過來的bundle
        Bundle bundle=getIntent().getBundleExtra("data");
        int position = bundle.getInt("position");
        int series=bundle.getInt("series");
        String drinkName=bundle.getString("drinkName");
        int tem = bundle.getInt("tem");
        int drinkCalorie=bundle.getInt("drinkCalorie");
        int drinkPrice=bundle.getInt("drinkPrice");
        int resID= bundle.getInt("ResID");
        //設定剛進去的第一次的圖片
        binding.intentPic.setImageResource(resID);
        binding.pName.setText(drinkName);
        binding.pPrice.setText(Integer.toString(drinkPrice));
        //把第一次進來的產品資料賦給shop
        shopName=drinkName;
        shopTem=tem;
        shopCalorie=drinkCalorie;
        shopPrice=drinkPrice;
        //打開資料庫
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        //創立暫時客戶的訂單資料表
        db.execSQL(createTable);
        productSeries=new ArrayList<>();
        //MenuList跳到產品介紹畫面先給定一個產品資料並用這筆資料找尋相同系列的產品
        productSeries.add( new DrinkModel(position,series,drinkName,tem,drinkCalorie,
                drinkPrice,resID));
        //從OrderListAdapter那邊看有沒有被點擊，有才會傳
        listener=new OrderListListener() {
            @Override
            public void onClick(int position, int series, String drinkName, int tem, int drinkCalorie, int drinkPrice, int resID, ArrayList<DrinkModel> seriesData) {
                seriesData.clear();
                //看是系列幾1、2、3....再從SQLite裡面抓資料
                selectSeries(series,seriesData);
                adapter.notifyDataSetChanged();
                binding.pName.setText(drinkName);
                shopName=drinkName;
                //drinkPrice是數字轉成字串
                shopTem=tem;
                shopPrice=drinkPrice;
                shopCalorie=drinkCalorie;
                binding.pPrice.setText(Integer.toString(drinkPrice));
                binding.intentPic.setImageResource(resID);

                binding.seriesDrinkMenuList.setAdapter(adapter);
                binding.bigCalorie.setText(Integer.toString(drinkCalorie));
                selectCheckBoxType(tem);
            }
        };
        //看是哪個系列來判斷要出現哪幾張圖片並加入資料再給adapter
        selectSeries(series,productSeries);
        adapter = new OrderListAdapter(db,listener,productSeries);
        //設定RecyclerView的呈現方式
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        binding.seriesDrinkMenuList.setLayoutManager(linearLayoutManager);
        binding.seriesDrinkMenuList.setAdapter(adapter);

        binding.bigCalorie.setText(Integer.toString(shopCalorie));
        //處理checkBox的程式碼
        //選擇是否有冷熱度可以選擇
        selectCheckBoxType(tem);
        //甜度checkbok設置
        checkBoxesSugar[0]=binding.noSugar;
        checkBoxesSugar[1]=binding.microSugar;
        checkBoxesSugar[2]=binding.halfSugar;
        checkBoxesSugar[3]=binding.sugar;
        CompoundButton.OnCheckedChangeListener checkBoxSugarListener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopSugar=null;
                if(isChecked){
                    for(int i=0;i<checkBoxesSugar.length;i++){
                        if(checkBoxesSugar[i].getText().toString().equals(buttonView.getText().toString())){
                            checkBoxesSugar[i].setChecked(true);

                        }else{
                            checkBoxesSugar[i].setChecked(false);
                        }
                    }
                }
                for(int i=0;i<checkBoxesSugar.length;i++){
                    if(checkBoxesSugar[i].isChecked()){
                        shopSugar = checkBoxesSugar[i].getText().toString();
                    }
                }
            }
        };
        checkBoxesSugar[0].setOnCheckedChangeListener(checkBoxSugarListener);
        checkBoxesSugar[1].setOnCheckedChangeListener(checkBoxSugarListener);
        checkBoxesSugar[2].setOnCheckedChangeListener(checkBoxSugarListener);
        checkBoxesSugar[3].setOnCheckedChangeListener(checkBoxSugarListener);




        //冰量checkbokx設置
        checkBoxesIce[0]=binding.hot;
        checkBoxesIce[1]=binding.noIce;
        checkBoxesIce[2]=binding.microIce;
        checkBoxesIce[3]=binding.lessIce;
        checkBoxesIce[4]=binding.ice;
        //設定一個Listener給checkBoxesIce陣列使用
        CompoundButton.OnCheckedChangeListener checkBoxIceListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopIce=null;
                if(isChecked){
                    for(int i=0;i<checkBoxesIce.length;i++){
                        if(checkBoxesIce[i].getText().toString().equals(buttonView.getText().toString())){
                            checkBoxesIce[i].setChecked(true);
                        }else{
                            checkBoxesIce[i].setChecked(false);
                        }
                    }
                }
                for(int i=0;i<checkBoxesIce.length;i++){
                    if(checkBoxesIce[i].isChecked()){
                        shopIce = checkBoxesIce[i].getText().toString();
                    }
                }
            }
        };
        checkBoxesIce[0].setOnCheckedChangeListener(checkBoxIceListener);
        checkBoxesIce[1].setOnCheckedChangeListener(checkBoxIceListener);
        checkBoxesIce[2].setOnCheckedChangeListener(checkBoxIceListener);
        checkBoxesIce[3].setOnCheckedChangeListener(checkBoxIceListener);
        checkBoxesIce[4].setOnCheckedChangeListener(checkBoxIceListener);

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ProductOrderActivity.this);
                builder.setMessage("是否加入訂單?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //給一個日期並存入SQlite
                        Calendar calendar =Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DATE);
                        String today = year+"年"+month+"月"+day+"日";
                        shopAmount=0;
                        //判斷數量是否是空的
                        //如果訂單成功就插入到SQLite資料庫的暫存產品表temProductOrder裡面
                        if((binding.productQuantity.getText().toString()!=null) && !(binding.productQuantity.getText().toString()).equals("")) {
                            try {
                                shopAmount = Integer.parseInt(binding.productQuantity.getText().toString());
                                if(shopAmount>0) {
                                    if (shopTem > 0) {
                                        if (shopSugar != null && !shopSugar.equals("") && shopIce != null && !shopIce.equals("") && shopAmount > 0) {
                                            db.execSQL("insert into tempProductOrder (shopName,shopTem,shopSugar,shopIce,shopAmount,shopPrice,date) values (?,?,?,?,?,?,?);",
                                                    new Object[]{shopName, shopTem, shopSugar, shopIce, shopAmount, shopPrice, today});
                                            Toast.makeText(ProductOrderActivity.this, "已加入到訂單，可至<我的訂單>查看", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ProductOrderActivity.this, MenuListActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ProductOrderActivity.this, "請確認甜度冰塊是否勾選", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (shopAmount > 0) {
                                            db.execSQL("insert into tempProductOrder (shopName,shopTem,shopSugar,shopIce,shopAmount,shopPrice,date) values (?,?,?,?,?,?,?);",
                                                    new Object[]{shopName, shopTem, null, null, shopAmount, shopPrice, today});
                                            Toast.makeText(ProductOrderActivity.this, "已加入到訂單，可至<我的訂單>查看", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ProductOrderActivity.this, MenuListActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ProductOrderActivity.this, "數量不能是負數", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else{
                                    Toast.makeText(ProductOrderActivity.this, "請輸入正確數量", Toast.LENGTH_SHORT).show();
                                }
                            }catch(Exception e){
                                Toast.makeText(ProductOrderActivity.this, "請輸入正確數量", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ProductOrderActivity.this, "請輸入正確數量", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
               builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
                builder.show();
            }
        });


    }
    public void selectSeries(int series,ArrayList<DrinkModel> a){
        Cursor cursor=null;
        switch(series){
            case 1:
                cursor=db.rawQuery("select * from product where series=1;",null);
                break;
            case 2:
                cursor=db.rawQuery("select * from product where series=2;",null);
                break;
            case 3:
                cursor=db.rawQuery("select * from product where series=3;",null);
                break;
            case 4:
                cursor=db.rawQuery("select * from product where series=4;",null);
                break;
            case 5:
                cursor=db.rawQuery("select * from product where series=5;",null);
                break;
            case 6:
                cursor=db.rawQuery("select * from product where series=6;",null);
                break;
            case 8:
                cursor=db.rawQuery("select * from product where series=8;",null);
                break;
            case 9:
                cursor=db.rawQuery("select * from product where series=9;",null);
                break;
            case 10:
                cursor=db.rawQuery("select * from product where series=10;",null);
                break;
        }
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int i=0;
            resources=getResources();
            do{

                resID=resources.getIdentifier(cursor.getString(6),"drawable"
                        ,"app.myproject.yujincoffee_app");
                resArray[i]=resID;
                DrinkModel b = new DrinkModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        resID
                );
                a.add(b);
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
    //看產品系列有沒有冷熱、冰量可以選，沒有把沒有的選項關掉
    public void selectCheckBoxType(int tem){
        switch(tem){
            case 0:
                binding.noSugar.setEnabled(false);
                binding.microSugar.setEnabled(false);
                binding.halfSugar.setEnabled(false);
                binding.sugar.setEnabled(false);
                binding.hot.setEnabled(false);
                binding.noIce.setEnabled(false);
                binding.microIce.setEnabled(false);
                binding.lessIce.setEnabled(false);
                binding.ice.setEnabled(false);
                break;
            case 1:
                binding.hot.setEnabled(false);
                break;
            case 2:
                binding.noIce.setEnabled(false);
                binding.microIce.setEnabled(false);
                binding.lessIce.setEnabled(false);
                binding.ice.setEnabled(false);
                break;
            case 3:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //製作Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.membersetting){
            Intent intent=new Intent(ProductOrderActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(ProductOrderActivity.this, MyOrderActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(ProductOrderActivity.this, MenuListActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(ProductOrderActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(ProductOrderActivity.this, MyFavoriteActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.storelists) {
            Intent intent = new Intent(ProductOrderActivity.this, storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(ProductOrderActivity.this, PointChangeActivity.class);
            startActivity(intent);
        }else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(ProductOrderActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataSharePre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataSharePre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                    Intent intent = new Intent(ProductOrderActivity.this, logPageActivity.class);
                    startActivity(intent);
                }
            });
            logoutbtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = logoutbtn.create();
            dialog.show();
        }else if(id ==android.R.id.home){
            //返回鍵動作
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}