package com.xb.soft;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import com.xb.soft.ui.BackBarView;
import com.xb.soft.utils.Log;
import com.xb.soft.utils.Utils;
import com.xb.soft.web.ClientJSObject;

import java.util.ArrayList;
import java.util.List;

public class WebActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "WebActivity";
    private WebView web_pay;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 202;
    private BackBarView backView;
    public static final String SCREEN_TYPE = "verticalScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_pay);
        Utils.setStatusBar(this);
        int screenType = getIntent().getIntExtra(SCREEN_TYPE, 1);
        backView = findViewById(R.id.back_bar);
        backView.setBackground(null);
        backView.setRightIconId(R.drawable.btn_close_shape);
        backView.setRightType(Global.BarRightType.ICON);
        backView.setRightClickListener(this);
        if (screenType == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (screenType == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        initWeb();
    }

    private void initWeb() {
        web_pay = this.<WebView>findViewById(R.id.web_pay);
        web_pay.getSettings().setJavaScriptEnabled(true);
        web_pay.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_pay.getSettings().setSupportZoom(false);
        web_pay.getSettings().setBuiltInZoomControls(false);
        web_pay.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_pay.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //数据库相关
        web_pay.getSettings().setDomStorageEnabled(true);
        web_pay.getSettings().setDatabaseEnabled(true);
        web_pay.getSettings().setAppCacheEnabled(true);

        //视频播放相关
        web_pay.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        web_pay.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web_pay.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        web_pay.getSettings().setUseWideViewPort(true);
        web_pay.getSettings().setLoadWithOverviewMode(true);
        web_pay.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "url=" + url);
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    Log.i(TAG, "Exception=" + url);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Exception");
//                            showErrorMsg("请下载安装支付宝后再试");
                        }
                    });
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                if (url.contains(".apk")) {
                    downloadByBrowser(url);
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished=" + url);
                super.onPageFinished(view, url);

            }
        });


        web_pay.setWebChromeClient(new WebChromeClient() {
            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }


            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
                return true;
            }
        });

        web_pay.loadUrl(getIntent().getStringExtra("url"));

    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }


    private void showErrorMsg(String msg) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("好的", null)
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        web_pay.onResume();
        web_pay.resumeTimers();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        web_pay.onPause();
        web_pay.pauseTimers();
        Log.i(TAG, "onPause");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        web_pay.destroy();
        web_pay = null;

        Log.i(TAG, "onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//监听返回键，如果可以后退就后退
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
