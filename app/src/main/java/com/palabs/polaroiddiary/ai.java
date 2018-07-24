package com.palabs.polaroiddiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ai extends AppCompatActivity {

    WebView aiWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        aiWebView = (WebView) findViewById(R.id.ai_webview);

        WebSettings webSettings = aiWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        aiWebView.loadUrl("https://bot.api.ai/9ad8d4bb-3ec1-41c2-8a24-c913b6443dc5");


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
