package fine.koacaiia.mnfwms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebList extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_list);

        webView=findViewById(R.id.web_list);
        Intent intent=getIntent();
        String bl=intent.getStringExtra("bl");
        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.tradlinx.com/unipass?type=2&blNo="+bl+"&blYr=2021");

    }
}