package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Adapter.HistoryOrderAdapter;
import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityHistoryOrderBinding;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HistoryOrderActivity extends AppCompatActivity {
    //從我的訂單頁面(MyOrderActivity)抓取資料放置於歷史訂單頁面(HistoryOrderActivity)
    //歷史訂單資料不會被清除
    ActivityHistoryOrderBinding binding;
    SharedPreferences memberDataPre;
    ArrayList<ProductModel> item;
    HistoryOrderAdapter Hadapter;
    ExecutorService executorService;
    SQLiteDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHistoryOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);

        executorService = Executors.newSingleThreadExecutor();
        //取得登入後儲存的會員EMAIL
        memberDataPre = getSharedPreferences("memberDataPre", MODE_PRIVATE);
        String memberEmail=memberDataPre.getString("email","查無資料");
        JSONObject packet=new JSONObject();
        JSONObject Email = new JSONObject();
        try {
            Email.put("email", memberEmail);
            packet.put("Email", Email);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        MediaType mType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(), mType);
        //VM IP=20.187.101.131
        Request request = new Request.Builder()
                .url("http://13.114.140.218:8216/api/product/getHistoryOrder")
                .post(body)
                .build();
        SimpleeAPIWorker apiCaller = new SimpleeAPIWorker(request,HistoryOrderHandler);
        //產生Task準備給executor執行
        executorService.execute(apiCaller);

        binding.backToIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HistoryOrderActivity.this,indextPageActivity.class);
                startActivity(intent);
            }
        });

        //還沒抓出雲端訂單資料放到item
        /*
        item=new ArrayList<>();
        ProductModel a = new ProductModel("name","sugar","ice",5,6);
        item.add(a);
         */
        /*
        Hadapter=new HistoryOrderAdapter(item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryOrderActivity.this);
        binding.historyorderlist.setLayoutManager(linearLayoutManager);
        //建立左滑能刪除的機制
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.historyorderlist);
        binding.historyorderlist.setAdapter(Hadapter);

         */
    }





    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            item.remove(viewHolder.getAdapterPosition());
            Hadapter.notifyDataSetChanged();
        }
    };



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.indext){
            Intent intent=new Intent(HistoryOrderActivity.this,indextPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.membersetting){
            Intent intent=new Intent(HistoryOrderActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(HistoryOrderActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(HistoryOrderActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(HistoryOrderActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.historyorder){
            Intent intent=new Intent(HistoryOrderActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(HistoryOrderActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(HistoryOrderActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(HistoryOrderActivity.this);
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
                    db.execSQL("delete from tempProductOrder;");
                    Intent intent = new Intent(HistoryOrderActivity.this, logPageActivity.class);
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

    Handler HistoryOrderHandler =new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==2000){
                Log.e("API",bundle.getString("orderList"));
                try {
                    JSONArray orderArray=new JSONArray(bundle.getString("orderList"));
                    item=new ArrayList<>();
                    for(int i=0;i<orderArray.length();i++){
                        //Log.e("API",orderArray.getJSONObject(i).getString("name"));
                        ProductModel a = new ProductModel(
                                orderArray.getJSONObject(i).getString("name"),
                                orderArray.getJSONObject(i).getString("sugar"),
                                orderArray.getJSONObject(i).getString("ice"),
                                orderArray.getJSONObject(i).getInt("amount"),
                                orderArray.getJSONObject(i).getInt("dollar")
                        );
                        item.add(a);
                    }

                    Hadapter=new HistoryOrderAdapter(item);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryOrderActivity.this);
                    binding.historyorderlist.setLayoutManager(linearLayoutManager);
                    //建立左滑能刪除的機制
                    //new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.historyorderlist);
                    binding.historyorderlist.setAdapter(Hadapter);





                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(HistoryOrderActivity.this, "成功接收回傳內容", Toast.LENGTH_SHORT).show();
            }

        }
    };

}