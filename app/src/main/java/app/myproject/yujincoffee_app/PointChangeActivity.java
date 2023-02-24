package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Modle.Util.SimpleAPIWork;
import app.myproject.yujincoffee_app.Modle.Util.SimpleAPIWork2;
import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityPointChangeBinding;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PointChangeActivity extends AppCompatActivity {
//5塊獲得1點
    ActivityPointChangeBinding binding;
    ExecutorService executorService;
    SharedPreferences memberDataPre;
    int point=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPointChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        executorService= Executors.newSingleThreadExecutor();

        memberDataPre=getSharedPreferences("memberDataPre",MODE_PRIVATE);
        String email=memberDataPre.getString("email","查無資料");
        MediaType mType=MediaType.parse("application/json");
        //VM IP=20.187.101.131
        //EC2 VM IP=13.114.140.218
        Request request=new Request.Builder()
                .url("http://13.114.140.218:8216/api/member/point/"+email)
                .build();
        SimpleAPIWork apiCaller=new SimpleAPIWork(request,getPointsHandler);
        //產生Task準備給executor執行
        executorService.execute(apiCaller);


        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pointChange = new AlertDialog.Builder(PointChangeActivity.this);
                pointChange.setTitle("點數兌換");
                pointChange.setMessage("確定要兌換嗎?");
                pointChange.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(point>=500){
                            Toast.makeText(PointChangeActivity.this, "兌換成功啦!", Toast.LENGTH_LONG).show();
                            point-=500;
                            MediaType mType2=MediaType.parse("application/json");
                            //VM IP=20.187.101.131
                            Request request=new Request.Builder()
                                    .url("http://13.114.140.218/api/member/minuspoint/"+point+"/"+email)
                                    .build();
                            SimpleAPIWork2 si= new SimpleAPIWork2(request);
                            executorService.execute(si);

                        }else{
                            Toast.makeText(PointChangeActivity.this, "兌換失敗菜雞", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                pointChange.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                pointChange.show();

            }
        });
        binding.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pointChange = new AlertDialog.Builder(PointChangeActivity.this);
                pointChange.setTitle("點數兌換");
                pointChange.setMessage("確定要兌換嗎?");
                pointChange.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(point>=630){
                            Toast.makeText(PointChangeActivity.this, "兌換成功啦!", Toast.LENGTH_LONG).show();
                            point-=630;
                            MediaType mType2=MediaType.parse("application/json");
                            //VM IP=20.187.101.131
                            Request request=new Request.Builder()
                                    .url("http://13.114.140.218:8216/api/member/minuspoint/"+point+"/"+email)
                                    .build();
                            SimpleAPIWork2 si= new SimpleAPIWork2(request);
                            executorService.execute(si);

                        }else{
                            Toast.makeText(PointChangeActivity.this, "兌換失敗菜雞", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                pointChange.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                pointChange.show();

            }
        });



    }


    Handler getPointsHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==200){
                point= Integer.parseInt(bundle.getString("data"));
                Log.d("point",point+"");
                Toast.makeText(PointChangeActivity.this, "成功接收會員點數", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PointChangeActivity.this, "接收會員點數失敗", Toast.LENGTH_LONG).show();
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

        if(id == R.id.indext){
            Intent intent=new Intent(PointChangeActivity.this,indextPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.membersetting){
            Intent intent=new Intent(PointChangeActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(PointChangeActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(PointChangeActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(PointChangeActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(PointChangeActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(PointChangeActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(PointChangeActivity.this);
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
                    Intent intent = new Intent(PointChangeActivity.this, logPageActivity.class);
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