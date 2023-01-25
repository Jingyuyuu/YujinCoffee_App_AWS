package app.myproject.yujincoffee_app.Modle.Util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleAPIWork implements  Runnable {
    private Request request;
    private android.os.Handler handler;
    private OkHttpClient client;

    public SimpleAPIWork(Request request, Handler handler) {
        this.request = request;
        this.handler = handler;
        client = new OkHttpClient();
    }

    @Override
    public void run() {
        String jsonString=null;
        Message m = handler.obtainMessage();
        Bundle bundle = new Bundle();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                jsonString=response.body().string();
                bundle.putInt("status",200);
                bundle.putString("data",jsonString);
            }else{
                bundle.putInt("status",404);
                bundle.putString("data","下載失敗404");
            }
            m.setData(bundle);
            handler.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
