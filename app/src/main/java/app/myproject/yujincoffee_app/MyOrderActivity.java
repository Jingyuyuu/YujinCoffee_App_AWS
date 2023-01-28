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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.SimpleFormatter;

import app.myproject.yujincoffee_app.Adapter.MyOrderAdapter;
import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.Part2.ProductOrderActivity;
import app.myproject.yujincoffee_app.databinding.ActivityMyOrderBinding;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyOrderActivity extends AppCompatActivity {
    //點擊加入購物車按鈕後 將資料放入我的訂單頁面(MyOrderActivity)
    ActivityMyOrderBinding binding;
    SharedPreferences memberDataPre;
    ArrayList<ProductModel> item;
    MyOrderAdapter adapter;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    ExecutorService executorService;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService= Executors.newSingleThreadExecutor();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //打開資料庫查看有無tempProductOrder資料表
        db=openOrCreateDatabase("yujin",MODE_PRIVATE,null);
//        db.execSQL(createTable);
        item=new ArrayList<>();
        //先抓出tem>0的資料
        Cursor cursor=db.rawQuery("select * from tempProductOrder where shopTem>0;",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                ProductModel a = new ProductModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6));
                item.add(a);
            }while(cursor.moveToNext());
        }
        //先抓出tem=0的資料
        Cursor cursor2=db.rawQuery("select * from tempProductOrder where shopTem==0;",null);
        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            do{
                ProductModel b = new ProductModel(cursor2.getInt(0)
                        ,cursor2.getString(1)
                        ,cursor2.getInt(2),
                        cursor2.getInt(5),
                        cursor2.getInt(6));
                item.add(b);
                Log.e("myorder,item",item.toString());
            }while(cursor2.moveToNext());
        }
        adapter=new MyOrderAdapter(item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrderActivity.this);
        binding.shoppingCart.setLayoutManager(linearLayoutManager);
        //建立左滑能刪除的機制
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.shoppingCart);
        binding.shoppingCart.setAdapter(adapter);

        //按下送出訂單按鈕 把訂單資料傳到server
        binding.orderSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //彈出確認是否送出訂單的按鈕
                AlertDialog.Builder orderSubmitBtn = new AlertDialog.Builder(MyOrderActivity.this);
                orderSubmitBtn.setTitle("送出訂單");
                orderSubmitBtn.setMessage("確定要送出訂單嗎? 送出即不可更改");
                orderSubmitBtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取得當前時間
                        SimpleDateFormat simpleFormatter =new SimpleDateFormat("yyyy-MM-DD HH:mm");
                        Date curDate=new Date(System.currentTimeMillis());
                        String time= simpleFormatter.format(curDate);
                        //取得會員Email
                        sharedPreferences =getSharedPreferences("memberDataPre",MODE_PRIVATE);
                        String email=sharedPreferences.getString("email","查無資料");

                        String name;
                        int amount;
                        String ice;
                        String sugar;
                        int dollar;

                        JSONObject packet=new JSONObject();
                        JSONObject OrderMst=new JSONObject();
                        JSONArray OrderDetail=new JSONArray();
                        try {
                            packet.put("OrderMst",OrderMst);
                            OrderMst.put("memberEmail",email);
                            OrderMst.put("date",time);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //抓出沒被滑掉的訂單資料
                        for(int x=0;x<item.size();x++){
                            ProductModel a=item.get(x);
                            name=a.getName();
                            ice=a.getIce();
                            sugar=a.getSugar();
                            dollar=a.getDollar();
                            amount=a.getAmount();
                            JSONObject drink=new JSONObject();
                            try {
                                drink.put("name",name);
                                //如果甜度跟冰量==null 則直接放入字串"null"
                                if (ice==null||a.getIce().equals(null)) {
                                    drink.put("ice","無");
                                    Log.e("JSON","ice="+a.getIce());
                                }else{
                                    drink.put("ice",ice);
                                }
                                if (sugar==null||a.getSugar().equals(null)) {
                                    drink.put("sugar", "無");
                                    Log.e("JSON","sugar="+a.getSugar());//
                                }else{
                                    drink.put("sugar", sugar);
                                }
                                drink.put("amount", amount);
                                drink.put("dollar", dollar);
                                OrderDetail.put(drink);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        try {
                            OrderMst.put("OrderDetail",OrderDetail);
                            Log.e("JSON",packet.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        MediaType mType=MediaType.parse("application/json");
                        RequestBody body=RequestBody.create(packet.toString(),mType);
                        //VM IP=20.187.101.131
                        Request request=new Request.Builder()
                                .url("http:/20.187.101.131:8216/api/product/orderSubmit")
                                .post(body)
                                .build();
                        SimpleeAPIWorker apiCaller=new SimpleeAPIWorker(request,orderSubmitHandler);
                        //產生Task準備給executor執行
                        executorService.execute(apiCaller);
                        db.execSQL("delete from tempProductOrder;");
                    }
                });
                orderSubmitBtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = orderSubmitBtn.create();
                dialog.show();

            }
        });

        binding.orderCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyOrderActivity.this,MenuListActivity.class);
                startActivity(intent);
            }
        });

    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDissmiss(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MyOrderActivity.this,R.color.dark_red001))
                    .addSwipeLeftActionIcon(R.drawable.ic_round_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
        if(id == R.id.membersetting){
            Intent intent=new Intent(MyOrderActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(MyOrderActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(MyOrderActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(MyOrderActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(MyOrderActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(MyOrderActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(MyOrderActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){

            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(MyOrderActivity.this);
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
                }
            });
            logoutbtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
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

    //送出訂單後接收Server回傳訊息的Handler
    Handler orderSubmitHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==1000){
                Toast.makeText(MyOrderActivity.this, "訂單送出成功", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MyOrderActivity.this, indextPageActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(MyOrderActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };

}