package com.example.bt3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class ViewDetailFoodActivity extends AppCompatActivity {
    WebView webView;
    ImageView imgLogo;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ẩn header mặc định
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_view_detail_food);

        //WEBVIEW
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            link = bundle.getString("LinkDetail");
            webView = (WebView) findViewById(R.id.webView);
            webView.loadUrl(link);
            webView.setWebViewClient(new WebViewClient());
        }

    }
}