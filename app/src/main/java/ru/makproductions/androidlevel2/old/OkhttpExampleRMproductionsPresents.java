package ru.makproductions.androidlevel2.old;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class OkhttpExampleRMproductionsPresents extends OkHttpClient {

    private static final String TAG = "OkhttpExample";

    public interface OnResponseListener {
        void onNetResponse(String result);
    }

    private OnResponseListener listener;

    private OkHttpClient client;
    private Request request;

    OkhttpExampleRMproductionsPresents(OnResponseListener listener) {
        this.listener = listener;
    }

    String connect(String url) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null)return;
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