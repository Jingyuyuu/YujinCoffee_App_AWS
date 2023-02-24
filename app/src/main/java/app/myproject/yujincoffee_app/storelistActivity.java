package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Adapter.StoreListAdapter;
import app.myproject.yujincoffee_app.Model.Product.StoreListModel;
import app.myproject.yujincoffee_app.Modle.Util.JsonToDb;
import app.myproject.yujincoffee_app.Modle.Util.JsonToStore;
import app.myproject.yujincoffee_app.Modle.Util.SimpleAPIWork;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityStorelistBinding;
import okhttp3.Request;

public class storelistActivity extends AppCompatActivity {

    ActivityStorelistBinding binding;
    SharedPreferences memberDataPre;
    StoreListAdapter adapter;
    ArrayList<StoreListModel> item;
    SQLiteDatabase db;

    ExecutorService executor;

    Request request;

    String createTable = "create table if not exists store(" +
            "storeName text,"+
            "storeAddress text,"+
            "storeTel text,"+
            "storeHour text"+
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
                JsonToStore j2db = new JsonToStore(db);
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
        binding=ActivityStorelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
        db.execSQL(createTable);
        executor= Executors.newSingleThreadExecutor();
        request = new Request.Builder().url("http://13.114.140.218:8216/api/store/allStore").build();
        SimpleAPIWork simpleAPIWork = new SimpleAPIWork(request,handler);
        executor.execute(simpleAPIWork);


        item=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from store;",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                StoreListModel a = new StoreListModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                item.add(a);
            }while(cursor.moveToNext());
        }


        adapter=new StoreListAdapter(item,db);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.storeListMenu.setLayoutManager(linearLayoutManager);
        binding.storeListMenu.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Menu選項
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.indext){
            Intent intent=new Intent(storelistActivity.this,indextPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.membersetting){
            Intent intent=new Intent(storelistActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(storelistActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(storelistActivity.this, MenuListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.historyorder){
            Intent intent=new Intent(storelistActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(storelistActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(storelistActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(storelistActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataPre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                    Intent intent = new Intent(storelistActivity.this, logPageActivity.class);
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