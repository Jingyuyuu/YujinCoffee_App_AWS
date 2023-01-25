package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.databinding.ActivityLogPageBinding;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class logPageActivity extends AppCompatActivity {
    ActivityLogPageBinding binding;
    ExecutorService executorService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences =getSharedPreferences("User",MODE_PRIVATE);
        String account=sharedPreferences.getString("account","null");
        if(sharedPreferences.getBoolean("check",false) && !account.equals(null)){
            binding.rememberme.setChecked(true);
            binding.loginAccTV.setText(account);
        }

        executorService= Executors.newSingleThreadExecutor();
        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.loginAccTV.getText().toString();
                String pwd=binding.loginPwdTV.getText().toString();
                //判斷欄位不可空白才送出登入資料
                if(name!=null && pwd!=null && !name.isEmpty() && !pwd.isEmpty()){
                    JSONObject packet=new JSONObject();
                    try {
                        //把使用者資料 封裝成JSON格式 回傳給SpringBoot Controller進行驗證
                        JSONObject memberLogData=new JSONObject();
                        memberLogData.put("acc",binding.loginAccTV.getText().toString());
                        memberLogData.put("pwd",binding.loginPwdTV.getText().toString());
                        packet.put("logData",memberLogData);
                        Log.e("JSON",packet.toString(4));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType mType=MediaType.parse("application/json");
                    RequestBody body=RequestBody.create(packet.toString(),mType);
                    //VM IP=20.187.101.131
                    Request request=new Request.Builder()
                            .url("http://192.168.43.21:8216/api/member/login")
                            .post(body)
                            .build();
                    SimpleeAPIWorker apiCaller=new SimpleeAPIWorker(request,loginResultHandler);
                    //產生Task準備給executor執行
                    executorService.execute(apiCaller);

                }else{
                    Toast.makeText(logPageActivity.this, "欄位空白", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logPageActivity.this, registtPageActivity.class);
                Bundle datas = new Bundle();
                startActivity(intent);

                /*ShoppingCart 練習

                ShoppingCart a= ShoppingCart.newInstance();
                Drink coffee = new Drink("奶茶","123","冰","0.5");
                a.drinkItems.add(coffee);
                Log.e("購物車",a.drinkItems.get(0).getName()
                +" "+a.drinkItems.get(0).getIce()+
                        " "+a.drinkItems.get(0).getSugar()
                +""+a.drinkItems.get(0).getPrice());
                 */
            }
        });

        binding.rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //SharedPreferences sharedPreferences =getSharedPreferences("User",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                getSharedPreferences("User",MODE_PRIVATE);
                if(b){
                    edit.putString("account",binding.loginAccTV.getText().toString()).commit();
                    edit.putBoolean("check",b).commit();
                    binding.loginAccTV.setText(sharedPreferences.getString("account",null));
                }else{
                    edit.remove("account").apply();
                    edit.remove("check").apply();
                    edit.putString("account",null);
                    edit.putBoolean("check",false).commit();
                }
            }
        });

    }

    Handler loginResultHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==666){
                Toast.makeText(logPageActivity.this, "歡迎回來", Toast.LENGTH_SHORT).show();
                //接收後端回傳登入成功的訊息後 將Email存起來 供會員資料頁面使用
                sharedPreferences =getSharedPreferences("memberDataPre",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                String savedEmail=sharedPreferences.getString("email","查無資料");
                String loginEmail=binding.loginAccTV.getText().toString();
                //確認登入成功的帳號與儲存在手機內的會員為同一人 不同則移除儲存的會員資料
                if(savedEmail.equals(loginEmail)) {
                    edit.putString("email", loginEmail).commit();//存入登入帳號到memberDataPre檔案
                }else{
                    edit.remove("email");
                    edit.remove("name");
                    edit.remove("points");
                    edit.remove("phone");
                    edit.apply();
                    edit.putString("email",loginEmail).commit();
                }
                Intent intent = new Intent(logPageActivity.this, indextPageActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(logPageActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };

}