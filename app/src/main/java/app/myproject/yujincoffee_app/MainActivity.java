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
    private String createTable2="create table if not exists tempProductOrder("+
            "_id integer"+" PRIMARY KEY AUTOINCREMENT,"+
            "shopName text,"+
            "shopTem integer,"+
            "shopSugar text,"+
            "shopIce text,"+
            "shopAmount integer,"+
            "shopPrice integer,"+
            "date text"+
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
                Log.d("data",jsonString);
            }else{
                Log.d("網路",bundle.getString("data"));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //從azure抓入資料庫到SQlite裡面
        //創建yujin資料庫
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        //創立資料表
        db.execSQL(createTable);
        db.execSQL(createTable2);
        //http:/20.187.101.131
        //連線springBoot交給SimpleAPIWork做處理並取得產品資料的json格式檔案
        //用Get API
        request=new Request.Builder().url("http://13.114.140.218:8216/api/product/data").build();
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
        //db.execSQL("delete from tempProductOrder;");
    }
}