package zut.edu.cn.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class baidu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//使webview支持js脚本
        webView.setWebViewClient(new WebViewClient());//网页在Webview中显示而不是打开浏览器
        webView.loadUrl("https://www.baidu.com");
    }
}
