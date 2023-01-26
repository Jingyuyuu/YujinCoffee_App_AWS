package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Modle.Util.JsonToDb;
import app.myproject.yujincoffee_app.Modle.Util.SimpleAPIWork;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityMainBinding;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ActionBar actionBar;
    SharedPreferences memberDataPre;
    SQLiteDatabase db;
    Request request;
    ExecutorService executor;
    String createTable ="create table if not exists product(" +
            "_id integer," +
            "series integer,"+
            "name text," +
            "tem integer,"+
            "calorie integer,"+
            "price integer,"+
            "pic text" +
            ");";
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();
            int status = bundle.getInt("status");
            if(status==200){
                //再一次檢查有沒創建資料表可有可無
                db.execSQL(createTable);
                jsonString=bundle.getString("data");
                JsonToDb j2db = new JsonToDb(db);
                j2db.writeToDatabase(jsonString);
            }else{
                Log.d("網路",bundle.getString("data"));
            }
        }
    };


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
            Intent intent=new Intent(MainActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            //從sharePreference裡面撈出資料看是不是登入模式
            //登入才能點進去購物
            memberDataPre =getSharedPreferences("memberDataPre",MODE_PRIVATE);
            String loginEmail=memberDataPre.getString("email","查無資料");
            if(!loginEmail.equals("查無資料")&& loginEmail!=null&&!loginEmail.equals("")) {
                Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "請先登入", Toast.LENGTH_SHORT).show();
            }
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(MainActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(MainActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(MainActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(MainActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(MainActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(MainActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataPre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                }
            });
            logoutbtn.setPositiveButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = logoutbtn.create();
            dialog.show();
        }
        else if(id ==android.R.id.home){
            //返回鍵動作
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //我的訂單JSON Array測試
        JSONObject packet=new JSONObject();
        JSONObject OrderMst=new JSONObject();
        JSONArray OrderDetail=new JSONArray();
        try {
            packet.put("OrderMst",OrderMst);
            OrderMst.put("member","黃曉明");
            OrderMst.put("date","20230119");
            for(int i=0;i<3;i++){
                JSONObject drink=new JSONObject();
                drink.put("飲料id","A00"+(i+1));
                drink.put("冷熱","冷");
                drink.put("數量", (i+1)+"");
                Log.e("JSON",drink.toString());
                OrderDetail.put(drink);
                Log.e("JSON",OrderDetail.toString());
            }
            OrderMst.put("OrderDetail",OrderDetail);
            Log.e("JSON",packet.toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        //從azure抓入資料庫到SQlite裡面
        //創建yujin資料庫
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        //創立資料表
        db.execSQL(createTable);
        //連線springBoot交給SimpleAPIWork做處理並取得產品資料的json格式檔案
        request=new Request.Builder().url("http://192.168.255.104:8261/api/product/data").build();
        //設定執行續
        executor= Executors.newSingleThreadExecutor();
        SimpleAPIWork downLoadData=new SimpleAPIWork(request,handler);
        executor.execute(downLoadData);
        //檢查product資料表裡面有沒有資料沒有回傳log.d(沒有資料)
        checkA();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request;
                Intent intent = new Intent(MainActivity.this, logPageActivity.class);
                Bundle datas = new Bundle();
                startActivity(intent);
            }
        });

    }


    public void checkA(){
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        db.execSQL(createTable);
        Cursor cursor=db.rawQuery("select * from product",null);
        if(cursor==null || cursor.getCount()==0){
            Log.d("網路","沒有資料");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.execSQL("delete from tempProductOrder;");
    }
}