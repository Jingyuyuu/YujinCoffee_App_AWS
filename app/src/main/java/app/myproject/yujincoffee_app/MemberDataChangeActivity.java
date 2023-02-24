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

import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityMemberDataChangeBinding;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MemberDataChangeActivity extends AppCompatActivity {
    ActivityMemberDataChangeBinding binding;
    ActionBar actionBar;
    ExecutorService executorService;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMemberDataChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("memberDataPre", MODE_PRIVATE);
        binding.nameChangeT.setText(sharedPreferences.getString("name","查無資料"));
        binding.phoneChangeT.setText(sharedPreferences.getString("phone","查無資料"));
        binding.memberChangeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder memberdataChangeBtn = new AlertDialog.Builder(MemberDataChangeActivity.this);
                memberdataChangeBtn.setTitle("修改會員資料");
                memberdataChangeBtn.setMessage("確定要修改會員資料?");
                memberdataChangeBtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //送出修改後的資料給server
                        String name=binding.nameChangeT.getText().toString();
                        String phone=binding.phoneChangeT.getText().toString();
                        String pwd=binding.pwdChangeT.getText().toString();
                        String pwd2=binding.pwd2ChangeT.getText().toString();
                        Boolean isName=binding.nameChangeT.getText().toString().isEmpty();
                        Boolean isPhone=binding.nameChangeT.getText().toString().isEmpty();
                        Boolean isPwd=binding.nameChangeT.getText().toString().isEmpty();
                        Boolean isPwd2=binding.nameChangeT.getText().toString().isEmpty();
                        if( name!=null && phone!=null  && !isName && !isPhone && ( !pwd.isEmpty() && pwd!=null && pwd.equals(pwd2) )){//pwd跟pwd2要一樣 就不用判斷pwd2是不是空白的了5
                            JSONObject packet=new JSONObject();
                            try {
                                JSONObject newMemberRegData=new JSONObject();
                                newMemberRegData.put("name",name);
                                newMemberRegData.put("pwd",pwd);
                                newMemberRegData.put("phone",phone);
                                newMemberRegData.put("email",sharedPreferences.getString("email","查無資料"));
                                packet.put("NewMemberData",newMemberRegData);
                                Log.e("JSON",packet.toString(4));
                                //把修改的會員姓名和電話寫入memberDataPre檔案
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("name",binding.nameChangeT.getText().toString());
                                editor.putString("phone",binding.phoneChangeT.getText().toString());
                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            MediaType mType=MediaType.parse("application/json");
                            RequestBody body=RequestBody.create(packet.toString(),mType);
                            //VM IP=20.187.101.131
                            //EC2 VM IP=13.114.140.218
                            Request request=new Request.Builder()
                                    .url("http://13.114.140.218:8216/api/member/reNewMemberData")
                                    .post(body)
                                    .build();
                            Toast.makeText(MemberDataChangeActivity.this, "已送出修改的會員資料", Toast.LENGTH_LONG).show();
                            SimpleeAPIWorker apiCaller=new SimpleeAPIWorker(request,memberChangeHandler);
                            //產生Task準備給executor執行
                            executorService.execute(apiCaller);

                        }else{
                            Toast.makeText(MemberDataChangeActivity.this, "請確認輸入相同密碼,且欄位不可空白", Toast.LENGTH_LONG).show();
                        }

                        //Intent intent = new Intent(MemberDataChangeActivity.this, memberdataaPageActivity.class);
                        //startActivity(intent);
                    }
                });
                memberdataChangeBtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = memberdataChangeBtn.create();
                dialog.show();

            }
        });

        binding.memberChangeCancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberDataChangeActivity.this, memberdataaPageActivity.class);
                startActivity(intent);
            }
        });
    }

    //Menu選單(右上角)
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
            Intent intent=new Intent(MemberDataChangeActivity.this, indextPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.membersetting){
            Intent intent=new Intent(MemberDataChangeActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(MemberDataChangeActivity.this, MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(MemberDataChangeActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(MemberDataChangeActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.storelists) {
            Intent intent = new Intent(MemberDataChangeActivity.this, storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(MemberDataChangeActivity.this, PointChangeActivity.class);
            startActivity(intent);
        }else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(MemberDataChangeActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sharedPreferences= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                    Intent intent = new Intent(MemberDataChangeActivity.this, logPageActivity.class);
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

    Handler memberChangeHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==123){
                Toast.makeText(MemberDataChangeActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MemberDataChangeActivity.this,memberdataaPageActivity.class);
                startActivity(intent);
            }
        }
    };
}