package ru.makproductions.androidlevel2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OkhttpExampleRMproductionsPresents.OnResponseListener {
    private WebView webView;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        webView = findViewById(R.id.web_view);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //webView.loadUrl(query);
        OkhttpExampleRMproductionsPresents okhttp = new OkhttpExampleRMproductionsPresents(this);
        okhttp.connect(query);
        return true;
    }

    @Override
    public void onNetResponse(String result) {
        webView.loadData(result,"text/html","UTF-8");
    }




}
