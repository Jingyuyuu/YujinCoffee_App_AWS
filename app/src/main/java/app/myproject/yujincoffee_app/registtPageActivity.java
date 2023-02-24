package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.myproject.yujincoffee_app.Modle.Util.SimpleeAPIWorker;
import app.myproject.yujincoffee_app.databinding.ActivityRegisttPageBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class registtPageActivity extends AppCompatActivity {
    ActivityRegisttPageBinding binding;
    ExecutorService executorService=Executors.newSingleThreadExecutor();
    SharedPreferences sharedPreferences;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        binding=ActivityRegisttPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService= Executors.newSingleThreadExecutor();

        binding.backLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.regSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //請將使用者資料 封裝成JSON格式 回傳給SpringBoot Controller進行驗證
                //下拉選單範例https://github.com/miscoder002/ReivewApphttps://github.com/miscoder002/ReivewApp
                String name=binding.regName.getText().toString();
                String pwd=binding.regPwd01.getText().toString();
                String pwd2=binding.regPwd02.getText().toString();
                String email=binding.regEmail.getText().toString();
                String phone=binding.regPhone.getText().toString();
                if(name!=null && pwd!=null && email!=null && phone!=null && !name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && ( !pwd.isEmpty() && pwd.equals(pwd2))){

                    JSONObject packet=new JSONObject();
                    try {

                        JSONObject memberRegData=new JSONObject();
                        memberRegData.put("name",binding.regName.getText().toString());
                        memberRegData.put("pwd",binding.regPwd01.getText().toString());
                        memberRegData.put("email",binding.regEmail.getText().toString());
                        memberRegData.put("phone",binding.regPhone.getText().toString());

                        packet.put("regData",memberRegData);
                        Log.e("JSON",packet.toString(4));
                        Toast.makeText(registtPageActivity.this, "送出註冊資訊", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType mType=MediaType.parse("application/json");
                    RequestBody body=RequestBody.create(packet.toString(),mType);
                    //VM IP=20.187.101.131
                    //EC2 VM IP=13.114.140.218
                    Request request=new Request.Builder()
                            .url("http://13.114.140.218:8216/api/member/register")
                            .post(body)
                            .build();
                    SimpleeAPIWorker apiCaller=new SimpleeAPIWorker(request,registerResultHandler);
                    //產生Task準備給executor執行
                    executorService.execute(apiCaller);
                }else{
                    Toast.makeText(registtPageActivity.this, "請注意欄位不可空白,且密碼需輸入相同", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    Handler registerResultHandler =new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==000){
                Toast.makeText(registtPageActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                sharedPreferences =getSharedPreferences("memberDataPre",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                String savedEmail=sharedPreferences.getString("email","查無資料");
                String loginEmail=binding.regEmail.getText().toString();
                //註冊成功代表信箱一定是新的 移除儲存在手機SharePreferance的會員資料
                edit.remove("email");
                edit.remove("name");
                edit.remove("points");
                edit.remove("phone");
                edit.apply();
                //存入註冊帳號到memberDataPre檔案(email)
                edit.putString("email", loginEmail).commit();
                Intent intent = new Intent(registtPageActivity.this, indextPageActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(registtPageActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }

        }
    };
    /*
    class SimpleAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;

        public SimpleAPIWorker(Request request){
            client=new OkHttpClient();
            this.request=request;
        }
        @Override
        public void run() {
            try {
                Response response=client.newCall(request).execute();
                String responseString=response.body().string();
                Log.e("API回應",responseString);
                //Response也應該是JASON格式回傳 由APP端確認登入結果

                JSONObject result=new JSONObject(responseString);
                Message m=registerResultHandler.obtainMessage();
                Bundle bundle=new Bundle();
                if(result.getInt("status")==000){
                    bundle.putString("mesg",result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status"));
                }else{
                    bundle.putString("mesg","email已經存在");
                    bundle.putInt("status",result.getInt("status"));
                }
                m.setData(bundle);
                registerResultHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
     */
}