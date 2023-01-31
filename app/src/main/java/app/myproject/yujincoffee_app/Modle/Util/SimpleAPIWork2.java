package app.myproject.yujincoffee_app.Modle.Util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleAPIWork2 implements Runnable{

    private Request request;
    private OkHttpClient client;

    public SimpleAPIWork2(Request request) {
        this.request = request;
        client = new OkHttpClient();
    }

    @Override
    public void run() {
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
