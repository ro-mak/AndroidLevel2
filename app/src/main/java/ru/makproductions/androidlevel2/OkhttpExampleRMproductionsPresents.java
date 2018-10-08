package ru.makproductions.androidlevel2;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpExampleRMproductionsPresents extends OkHttpClient {
    public interface OnResponseListener {
        void onNetResponse(String result);
    }

    private OnResponseListener listener;

    private OkHttpClient client;
    private Request request;

    public OkhttpExampleRMproductionsPresents(OnResponseListener listener) {
        this.listener = listener;
    }

    public String connect(String url) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GameOver", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onNetResponse(result);
                    }
                });
            }
        });
        return "";
    }

}