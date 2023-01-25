package app.myproject.yujincoffee_app.Modle.Util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleeAPIWorker implements Runnable{
    OkHttpClient client;
    android.os.Handler handler;
    Request request;

    public SimpleeAPIWorker(Request request, Handler handler){
        client=new OkHttpClient();
        this.handler = handler;
        this.request=request;
    }
    @Override
    public void run() {
        try {
            Response response=client.newCall(request).execute();
            String responseString=response.body().string();
            Log.e("API回應",responseString);
            //Response也應該是JSON格式回傳 由APP端確認登入結果

            JSONObject result=new JSONObject(responseString);


            Message m=handler.obtainMessage();
            Bundle bundle=new Bundle();

            if(result.getInt("status")==000){
                bundle.putString("mesg",result.getString("mesg"));
                bundle.putInt("status",result.getInt("status"));
            }else if(result.getInt("status")==111){
                bundle.putString("mesg","email已經存在");
                bundle.putInt("status",result.getInt("status"));

            }else if(result.getInt("status")==666){//確認登入狀態
                bundle.putString("mesg",result.getString("mesg"));
                bundle.putInt("status",result.getInt("status"));
            }else if(result.getInt("status")==444){
                bundle.putString("mesg","登入失敗 請確認帳號及密碼是否正確");
                bundle.putInt("status",result.getInt("status"));

            }else if(result.getInt("status")==123){//確認修改會員資料狀態
                bundle.putString("mesg",result.getString("mesg"));
                bundle.putInt("status",result.getInt("status"));

            }else if(result.getInt("status")==456){
                bundle.putString("mesg",result.getString("mesg"));
                bundle.putInt("status",result.getInt("status"));

            }else if(result.getInt("status")==999){//確認抓取會員資料狀態
                bundle.putString("email",result.getJSONObject("data").getString("email"));
                bundle.putString("name",result.getJSONObject("data").getString("name"));
                bundle.putString("points",result.getJSONObject("data").getString("points"));
                bundle.putString("phone",result.getJSONObject("data").getString("phone"));
                bundle.putInt("status",result.getInt("status"));
                Log.e("999",bundle.getString("email"));
            }else{
                bundle.putString("mesg","系統錯誤，請洽程式開發人員");
                bundle.putInt("status",result.getInt("status"));
            }

            m.setData(bundle);
            handler.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
