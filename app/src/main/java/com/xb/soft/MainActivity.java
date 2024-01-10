package com.xb.soft;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.view.KeyEvent;

import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xb.soft.adapter.GuidePagerAdapter;
import com.xb.soft.adapter.UrlInfoAdapter;
import com.xb.soft.net.MyCallBack;
import com.xb.soft.net.NetClient;
import com.xb.soft.net.TestURLPingTask;
import com.xb.soft.net.TestURLTask;
import com.xb.soft.net.TestUrlCallBack;
import com.xb.soft.net.ThreadManage;
import com.xb.soft.net.mode.UpgradeResponse;
import com.xb.soft.ui.CommonDialog;
import com.xb.soft.ui.UrlInfoDialog;
import com.xb.soft.utils.DataCleanManagerUtil;
import com.xb.soft.utils.Log;
import com.xb.soft.utils.PreferenceUtils;
import com.xb.soft.utils.StringUtils;
import com.xb.soft.utils.ToastUtil;
import com.xb.soft.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BaseActivity {
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private static final String TAG = "MainActivity";
    private WebView webView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 202;
    private ProgressDialog progressDialog;
    private String url;
    private ImageView ivLaunch;
    private ViewPager vpGuide;
    private List<ImageView> imgs;
    private UpgradeResponse response;
    private boolean isBreak;
    private static final int WEB_URL_SUCCESS = 0X0001;
    private static final String JS_OBJ = "clientObj";
    private int urlTestErrorCount;
    private int chuckCount;
    private Lock versionCheckLock = new ReentrantLock();
    private Lock testUrlLock = new ReentrantLock();
    private Lock testUrlSuccessLock = new ReentrantLock();
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private String mMethod;
    private List<String> failureList = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEB_URL_SUCCESS:
                    startRunnable();
                    break;
                case APP_VERSION_DAILOG:
                    showUpdateDialog(response, MainActivity.this);
                    break;
                case APP_STATUSBAR:
                    Utils.setStatusBar(MainActivity.this);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    break;
            }
        }
    };

    private static final int APP_VERSION_DAILOG = 0X0002;
    private long firstTime;
    //是否已经关闭
    private boolean closeLoading;
    private static final int APP_STATUSBAR = 0X0003;
    private TextView tvVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        StatusBarCompat.setTranslucent(getWindow(), true);
        setContentView(R.layout.activity_main);
        initView();
        String networkState = Utils.getNetworkState(this);
        if (networkState.equals("当前无网络连接")) {
            CommonDialog commonDialog = new CommonDialog(this);
            commonDialog.setDlgTitle(networkState);
            commonDialog.setContent("请检查您的网络链接");
            commonDialog.setPositiveButton(R.string.app_cancel, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commonDialog.dismiss();
                    finish();
//                    restartApp();
                }
            });
            commonDialog.show();
        } else {
            init();
            setUrl();
        }
    }

    private void setUrl() {
        if (ConfigAPP.webUrlType == 2) {
            NetClient.getNetClient().callGetNet(ConfigAPP.webUrlTxt, new MyCallBack() {
                @Override
                public void onFailure(int code) {
                    ToastUtil.showToastLong(MainActivity.this, "获取地址失败:code" + code);
                }

                @Override
                public void onResponse(String json) {
                    ConfigAPP.webUrls = Arrays.asList(json.split("\\n"));
                    cacheUrlTest();
                }
            });
        } else if (ConfigAPP.webUrlType == 3) {
            if (!TextUtils.isEmpty(ConfigAPP.webUrlTxt)) {
                NetClient.getNetClient().callGetNet(ConfigAPP.webUrlTxt, new MyCallBack() {
                    @Override
                    public void onFailure(int code) {
                        ConfigAPP.webUrls = ConfigAPP.webSpareUrls;
                        cacheUrlTest();
                    }

                    @Override
                    public void onResponse(String json) {
                        ConfigAPP.webUrls = Arrays.asList(json.split("\\n"));
                        cacheUrlTest();
                    }
                });
            } else {
                ConfigAPP.webUrls = ConfigAPP.webSpareUrls;
                cacheUrlTest();
            }
        } else {
            cacheUrlTest();
        }

    }

    private void versionCheck() {
        long timeStamp = System.currentTimeMillis();
        List<String> checkUrls = ConfigAPP.checkUrls;
        for (int i = 0; i < checkUrls.size(); i++) {
            URL uRl = null;
            try {
                uRl = new URL(checkUrls.get(i));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String domain = uRl.getHost();
            String protocol = uRl.getProtocol();

            String checkUrl = protocol + "://" + domain + "?version=android" + "?v=" + timeStamp;
            NetClient.getNetClient().callGetNet(checkUrl, new MyCallBack() {
                @Override
                public void onFailure(int code) {
                    versionCheckLock.lock();
                    try {
                        chuckCount++;
                        if (chuckCount == checkUrls.size()) {
                            ToastUtil.showToastLong(MainActivity.this, "检测版本失败:code" + code);
                        }
                    } finally {
                        versionCheckLock.unlock();
                    }

                }

                @Override
                public void onResponse(String json) {
                    Log.d("Jack",json);
                    if (!isBreak) {
                        isBreak = true;
                        Gson gson = new Gson();
                        response = gson.fromJson(json, UpgradeResponse.class);
                        String version;
                        try {
                            version = Utils.getVersionName();
                            String[] versions = version.split("\\.");
                            String[] resVersions = response.getVersion().split("\\.");
                            int versionFirst = Integer.valueOf(versions[0]);
                            int versionSen = Integer.valueOf(versions[1]);
                            int versionThr = Integer.valueOf(versions[2]);
                            int resVersionFirst = Integer.valueOf(resVersions[0]);
                            int resVersionSen = Integer.valueOf(resVersions[1]);
                            int resVersionThr = Integer.valueOf(resVersions[2]);
                            if (versionFirst < resVersionFirst) {
                                mHandler.sendEmptyMessage(APP_VERSION_DAILOG);
                            }
                            if (versionFirst == resVersionFirst) {
                                if (versionSen < resVersionSen) {
                                    mHandler.sendEmptyMessage(APP_VERSION_DAILOG);
                                }
                                if (versionSen == resVersionSen) {
                                    if (versionThr < resVersionThr) {
                                        mHandler.sendEmptyMessage(APP_VERSION_DAILOG);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Exception", e);
                        }
                    }

                }
            });
        }
    }


    public void showUpdateDialog(UpgradeResponse response, Context mContext) {
        if (response.getUrl().contains(".apk")) {
            updateVersion(response);
        } else {
            showBrowserUpdateAppDialog(response, mContext);
        }

    }

    private void showBrowserUpdateAppDialog(UpgradeResponse response, Context mContext) {
        final CommonDialog dlg = new CommonDialog(mContext);
        dlg.setDlgTitle("提醒");
        if (response.getContent() != null) {
            dlg.setContent(response.getContent());
        }
        dlg.setCancelable(false);
        int btnRes1 = R.string.ok;
        int btnRes2 = R.string.cancel;

        if (response.isForceUpdate()) {
            dlg.setSinglePositiveButton(btnRes1, v -> {
                Uri uri = Uri.parse(response.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
        } else {
            dlg.setPositiveButton(btnRes1, v -> {
                Uri uri = Uri.parse(response.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });

            dlg.setNegativeButton(btnRes2, v -> dlg.dismiss());
        }
        dlg.show();
    }


    private void initView() {
        webView = findViewById(R.id.wv);
        ivLaunch = findViewById(R.id.iv_launch);
        tvVersion = findViewById(R.id.tv_version);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(TAG, "竖屏");
            ivLaunch.setBackgroundResource(R.drawable.vertical);
        } else {
            Log.i(TAG, "横屏");
            ivLaunch.setBackgroundResource(R.drawable.horizonta);
        }
        tvVersion.setText("version:" + Utils.getVersionName());
        initGuideVp();
    }

    private void initGuideVp() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        boolean firstLanuch = !PreferenceUtils.contains(PreferenceUtils.FIRST_LANUCH) || PreferenceUtils.getBoolean(PreferenceUtils.FIRST_LANUCH);
        if (firstLanuch && ConfigAPP.guideUrls.size() > 0) {
            initPagerAdapter();
            vpGuide.setVisibility(View.VISIBLE);
            PreferenceUtils.setBoolean(PreferenceUtils.FIRST_LANUCH, false);
        } else {
            vpGuide.setVisibility(View.GONE);
        }
    }

    private void initPagerAdapter() {
        imgs = new ArrayList<ImageView>();
        for (int i = 0; i < ConfigAPP.guideUrls.size(); i++) {
            ImageView img = new ImageView(this);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            Resources res = getResources();
            final String packageName = getPackageName();
            String imageName = "gd" + i;
            int imgId = res.getIdentifier(imageName, "drawable", packageName);
            img.setBackgroundResource(imgId);
            if (i == ConfigAPP.guideUrls.size() - 1) {
                img.setOnClickListener(v -> vpGuide.setVisibility(View.GONE));
            }
            imgs.add(img);
        }
        vpGuide.setAdapter(new GuidePagerAdapter(imgs));
    }

    private void testUrl() {
        List<String> list = ConfigAPP.webUrls;
        if (ConfigAPP.urlWebCheckType == 1) {
            for (int i = 0; i < list.size(); i++) {
                String testURL;
                testURL = list.get(i);
                TestURLPingTask task = new TestURLPingTask(testURL, new TestUrlCallBack() {
                    @Override
                    public void onSuccess(String url) {
                        processTestUrlSucdess(url);
                    }

                    @Override
                    public void onFailure(String url) {
                        failureList.add(url);
                        processTestUrlFailure();
                    }
                });
                ThreadManage.getInstance().ansyExeTask(task);
            }
        } else if (ConfigAPP.urlWebCheckType == 2) {
            for (int i = 0; i < list.size(); i++) {
                String testURL;
                testURL = list.get(i);
                TestURLTask task = new TestURLTask(testURL, new TestUrlCallBack() {
                    @Override
                    public void onSuccess(String url) {
                        processTestUrlSucdess(url);
                    }

                    @Override
                    public void onFailure(String url) {
                        failureList.add(url);
                        processTestUrlFailure();
                    }
                });
                ThreadManage.getInstance().ansyExeTask(task);
            }
        } else {
            UrlManage.getInstance().setUrl(ConfigAPP.webUrls.get(0));
            Log.i(TAG, UrlManage.getInstance().getUrl());
            if (!StringUtils.isEmpty(UrlManage.getInstance().getUrl())) {
                startRunnable();
            }
        }

    }

    private void processTestUrlSucdess(String url) {
        testUrlSuccessLock.lock();
        try {
            if (StringUtils.isEmpty(UrlManage.getInstance().getUrl())) {
                Log.i(TAG, "processTestUrlSucdess" + url);
                UrlManage.getInstance().setUrl(url);
                mHandler.sendEmptyMessage(WEB_URL_SUCCESS);
            }
        } finally {
            testUrlSuccessLock.unlock();
        }
    }

    private void processTestUrlFailure() {
        testUrlLock.lock();
        try {
            urlTestErrorCount++;
            if (urlTestErrorCount == ConfigAPP.webUrls.size()) {
                runOnUiThread(() -> {
                    showTestUrlDialog();
                });
            }
        } finally {
            testUrlLock.unlock();
        }
    }

    private void showTestUrlDialog() {
        UrlInfoAdapter adapter = new UrlInfoAdapter(this, failureList);
        UrlInfoDialog dialog = new UrlInfoDialog(this, adapter);
        dialog.setOutButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setResetButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlTestErrorCount = 0;
                testUrl();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void cacheUrlTest() {
        handlerTime.postDelayed(runnableTime, ConfigAPP.maxDisplayTime * 1000);
        if (!StringUtils.isEmpty(UrlManage.getInstance().getUrl())) {
            String testURL = UrlManage.getInstance().getUrl();
            if (ConfigAPP.urlWebCheckType == 2) {
                TestURLTask task = new TestURLTask(testURL, new TestUrlCallBack() {
                    @Override
                    public void onSuccess(String url) {
                        mHandler.sendEmptyMessage(WEB_URL_SUCCESS);
                    }

                    @Override
                    public void onFailure(String url) {
                        testUrl();
                    }
                });
                ThreadManage.getInstance().ansyExeTask(task);
            } else if (ConfigAPP.urlWebCheckType == 1) {
                TestURLPingTask task = new TestURLPingTask(testURL, new TestUrlCallBack() {
                    @Override
                    public void onSuccess(String url) {
                        mHandler.sendEmptyMessage(WEB_URL_SUCCESS);
                    }

                    @Override
                    public void onFailure(String url) {
                        testUrl();
                    }
                });
                ThreadManage.getInstance().ansyExeTask(task);
            } else {
                Log.i("loadUrl", testURL);
                webView.post(() -> webView.loadUrl(testURL));
            }
        } else {
            testUrl();
        }
    }

    private void startRunnable() {
        PreferenceUtils.setValue(PreferenceUtils.WEB_URL, UrlManage.getInstance().getUrl());
        url = UrlManage.getInstance().getUrl();
        webView.post(new Runnable() {
            @Override
            public void run() {
                Log.i("loadUrl", url);
                webView.loadUrl(url);
            }
        });
    }


    Handler handlerTime = new Handler();
    Runnable runnableTime = new Runnable() {
        @Override
        public void run() {
            if (ConfigAPP.autoClose) {
                ivLaunch.setVisibility(View.GONE);
                tvVersion.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(APP_STATUSBAR);
            } else {
                boolean testUrlSuccess = urlTestErrorCount == ConfigAPP.webUrls.size() ? false : true;
                if (!closeLoading && testUrlSuccess) {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage(getString(R.string.load));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    handler.postDelayed(runnable, 15 * 1000);
                }
            }
            versionTypeCheck();
        }
    };

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            CommonDialog dialog = new CommonDialog(MainActivity.this);
            dialog.show();
            dialog.setDlgTitle(R.string.is_out);
        }
    };

    private void versionTypeCheck() {
        if (ConfigAPP.versionCheckType == 1) {
            if (ConfigAPP.checkUrlType == 2) {
                NetClient.getNetClient().callGetNet(ConfigAPP.checkUrlTxt, new MyCallBack() {
                    @Override
                    public void onFailure(int code) {
                    }

                    @Override
                    public void onResponse(String json) {
                        ConfigAPP.checkUrls = Arrays.asList(json.split("\\n"));
                        versionCheck();
                        Log.i(TAG, "onResponse: " + json);
                    }
                });
            } else if (ConfigAPP.checkUrlType == 3) {
                if (!TextUtils.isEmpty(ConfigAPP.checkUrlTxt)) {
                    NetClient.getNetClient().callGetNet(ConfigAPP.checkUrlTxt, new MyCallBack() {
                        @Override
                        public void onFailure(int code) {
                            ConfigAPP.checkUrls = ConfigAPP.checkSpareUrls;
                            versionCheck();
                        }

                        @Override
                        public void onResponse(String json) {
                            ConfigAPP.checkUrls = Arrays.asList(json.split("\\n"));
                            versionCheck();
                            Log.i(TAG, "onResponse: " + json);
                        }
                    });
                } else {
                    ConfigAPP.checkUrls = ConfigAPP.checkSpareUrls;
                    versionCheck();
                }
            } else {
                versionCheck();
            }

        }
    }


    private void updateVersion(UpgradeResponse bean) {
        UIData uiData = UIData.create()
                .setTitle("有新版本下载")
                .setContent(bean.getContent())
                .setDownloadUrl(bean.getUrl());
        if (bean.isForceUpdate()) {
            AllenVersionChecker
                    .getInstance()
                    .downloadOnly(uiData)
                    .setForceUpdateListener(() -> {

                    })
                    .executeMission(this);
        } else {
            AllenVersionChecker
                    .getInstance()
                    .downloadOnly(uiData)
                    .executeMission(this);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//监听返回键，如果可以后退就后退
            if (webView.getUrl().contains("home")) {
                if (ConfigAPP.exit == 1) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;
                        return true;
                    } else {
                        finish();
                    }
                } else if (ConfigAPP.exit == 2) {
                    showExitDialog();
                    return true;
                } else {
                    finish();
                }
            }
            if (ConfigAPP.sideslip == 1 && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        CommonDialog dialog = new CommonDialog(MainActivity.this);
        dialog.setDlgTitle("是否退出").setNegativeButton(R.string.cancel, v -> {
            dialog.dismiss();
        }).setPositiveButton(R.string.ok, v -> {
            finish();
        });
        dialog.show();
    }

    private void init() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);

        webView.getSettings().setTextZoom(100);
        webView.getSettings().setDefaultFixedFontSize(webView.getWidth() / 10);
        webView.getSettings().setDefaultFontSize(webView.getWidth() / 10);

        //数据库相关
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//        设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
//        设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        webView.getSettings().setAppCacheEnabled(true);


        //视频播放相关
        webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        final ClientJSObject clientJSObject = new ClientJSObject(this, webView);
        webView.addJavascriptInterface(clientJSObject, JS_OBJ);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.i(TAG, "onReceivedSslError" + url);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading=" + url);


                if (url.contains("changeStatusColor.postMessage/")) {
                    String[] split = url.split("changeStatusColor.postMessage/");
                    StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(split[1]));
                    return true;
                }

                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showErrorMsg("请下载安装支付宝后再试");
                        }
                    });
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                evaluateJS();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted=" + url);
                if (ivLaunch.getVisibility() == View.VISIBLE) {
                    mHandler.postDelayed(() -> {
                        if (!closeLoading) {
                            ivLaunch.setVisibility(View.GONE);
                            tvVersion.setVisibility(View.GONE);
                            mHandler.sendEmptyMessage(APP_STATUSBAR);
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                handler.removeCallbacksAndMessages(null);
                            }
                        }
                    }, 10000);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished=" + url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
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
    }

    private void evaluateJS() {
        webView.post(() -> webView.evaluateJavascript("window.clientIsApp = true;", s -> {
            Log.i(TAG, "evaluateJavascript:window.clientIsApp =" + s);
        }));
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FILECHOOSER_RESULTCODE:
                if (null == mUploadMessage && null == mUploadCallbackAboveL) {
                    return;
                }
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                if (mUploadCallbackAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
                break;
            case 12310:
                if (data == null) {
                    webView.loadUrl("javascript:" + mMethod + "(false)");
                    return;
                }
                int captureDocResult = data.getIntExtra("result", 0);

                if (captureDocResult == 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("result", "data:image/png;base64," + MyApplication.captureDocResult);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String base64 = Utils.bitmapToBase64(MyApplication.captureDocResultBitmap);
                    webView.loadUrl("javascript:setlocalStorage" + "(" + mMethod + "," + jsonObject + ")");
                    webView.loadUrl("javascript:" + mMethod + "(" + jsonObject + ")");
                } else {
                    webView.loadUrl("javascript:" + mMethod + "(false)");
                }
                break;
            case 12311:
                if (data == null) {
                    webView.loadUrl("javascript:" + mMethod + "(false)");
                    return;
                }

                int captureFaceResult = data.getIntExtra("result", 0);

                if (captureFaceResult == 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("result", "data:image/png;base64," + MyApplication.captureFaceResult);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    webView.loadUrl("javascript:setlocalStorage" + "(" + mMethod + "," + jsonObject + ")");
                    webView.loadUrl("javascript:" + mMethod + "(" + jsonObject + ")");
                } else {
                    webView.loadUrl("javascript:" + mMethod + "(false)");
                }
                break;
            case 12312:
            case 12313:
                if (data == null) {
                    webView.loadUrl("javascript:" + mMethod + "(1)");
                    return;
                }
                int verifyResult = data.getIntExtra("result", 0);
                webView.loadUrl("javascript:" + mMethod + "(" + verifyResult + ")");
                break;
        }
    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private class ClientJSObject {
        private Context mContext;
        private WebView mWebView;
        private List<VisitObject> visitObjects = new ArrayList<>();

        public ClientJSObject(Context context, WebView webView) {
            mContext = context;
            mWebView = webView;
        }

        @JavascriptInterface
        public synchronized void goback() {
            mWebView.post(() -> {
                if (visitObjects.size() > 1 && mWebView.canGoBack()) {
                    visitObjects.remove(visitObjects.size() - 1);
                    VisitObject visitObject = visitObjects.remove(visitObjects.size() - 1);
                    mWebView.goBack();
                }
            });
        }

        @JavascriptInterface
        public void gotoUrl(final String url, int verticalScreen) {
            Log.i(TAG, "gotoUrl: " + url);
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("verticalScreen", verticalScreen);
            startActivity(intent);

        }

        @JavascriptInterface
        public void closeLoading() {
            Log.i(TAG, "closeLoading: ");
            runOnUiThread(() -> {
                closeLoading = true;
                ivLaunch.setVisibility(View.GONE);
                tvVersion.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(APP_STATUSBAR);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    handler.removeCallbacksAndMessages(null);
                }
            });

        }

        @JavascriptInterface
        public void clearWebJSCache() {
            Log.i(TAG, "clearWebJSCache: ");
            Utils.clearWebViewCache();
            DataCleanManagerUtil.cleanCustomCache(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
            runOnUiThread(() -> {
                webView.clearCache(true);
                CommonDialog commonDialog = new CommonDialog(MainActivity.this);
                commonDialog.setDlgTitle("提示");
                commonDialog.setContent("已清除缓存，请重新启动");
                commonDialog.setNegativeButton(R.string.wait, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.setPositiveButton(R.string.out, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                        finish();
                    }
                });
                commonDialog.show();
            });
        }

        @JavascriptInterface
        public void callThirdApp(String url) {
            Log.i(TAG, "callThirdApp: " + url);
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                gotoThirdApp(url);
            } else {
                downloadByBrowser(url);
            }
        }

        @JavascriptInterface
        public void setMode(int i) {
            mMode = i;
        }

        @JavascriptInterface
        public int getMode() {
            return mMode;
        }

        class VisitObject {
            private String url;

            public VisitObject(String url) {
                this.url = url;
            }
        }
    }

    private int mMode;

    private void gotoThirdApp(String url) {
        try {
            Uri data = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, data);
            //保证新启动的APP有单独的堆栈，如果希望新启动的APP和原有APP使用同一个堆栈则去掉该项
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, RESULT_OK);
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showErrorMsg("请下载安装应用后再试");
                }
            });
        }
    }

    private void showErrorMsg(String msg) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("好的", null)
                .show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 判断Android当前的屏幕是横屏还是竖屏。横竖屏判断
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(TAG, "竖屏");
            ivLaunch.setBackgroundResource(R.drawable.vertical);
        } else {
            Log.i(TAG, "横屏");
            ivLaunch.setBackgroundResource(R.drawable.horizonta);
        }
    }

    // 重启应用
    @SuppressWarnings("WrongConstant")
    public void restartApp() {
        Intent intent = new Intent();// 参数1：包名，参数2：程序入口的activity
        intent.setClassName(getPackageName(), getPackageName() + ".MainActivity");
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); //3秒钟后重启应用
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
        Log.i(TAG, "onResume");
        webView.loadUrl("javascript:comeBackFront()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            handler.removeCallbacksAndMessages(null);

        }
        if (ConfigAPP.cache != 0) {
            webView.clearCache(true);
            Utils.clearWebViewCache();
        }
        webView.destroy();
        webView = null;
        handlerTime.removeCallbacksAndMessages(null);
//        UrlManage.getInstance().setUrl(null);
        mCompositeDisposable.clear();
        Log.i(TAG, "onDestroy");
    }
}

