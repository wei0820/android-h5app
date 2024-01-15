package com.xb.soft;

import android.content.Context;

import com.xb.soft.net.mode.ConfigBean;
import com.xb.soft.utils.AESUtils;
import com.xb.soft.utils.LocalJsonResolutionUtils;

import java.util.List;

public class ConfigAPP {
    public static  String sequence;
    public static  int maxDisplayTime;
    public static  Boolean autoClose;
    public static  int verticalScreen;
    public static List<String> webUrls;
    public static int urlWebCheckType;
    public static  List<String> checkUrls;
    public static int versionCheckType;
    public static int sideslip;
    public static int statusBarShow;
    public static int statusBarhowType;
    public static int statusBarTitileColor;
    public static String statusBarBackground;
    public static List guideUrls;
    public static int loading;
    public static int cache;
    public static int exit;
    public static int share;
    public static int notification;
    public static int webUrlType;
    public static int checkUrlType;
    public static String webUrlTxt;
    public static String checkUrlTxt;
    private static String KEY="QWER1234ASDF5678";
    private static String IV="QWERASDF56781234";

    private static Context mContext;
    public static List<String> checkSpareUrls;
    public static List<String> webSpareUrls;

    public static void initConfigContext(Context context){
        mContext = context;
        initConfigInfo();
    }
    private static void initConfigInfo(){
        if(mContext == null)
            return;
        String fileName = "android.json";
//        String configJson = LocalJsonResolutionUtils.getJson(mContext,fileName);
        String configJson = "";
        try {
            String s = AESUtils.encrypt(KEY,IV,LocalJsonResolutionUtils.getJson(mContext,fileName));
            configJson = AESUtils.decrypt(KEY,IV,s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfigBean configBean = LocalJsonResolutionUtils.JsonToObject(configJson,ConfigBean.class);
        sequence = configBean.getCore().getSequence();
        maxDisplayTime = configBean.getCore().getStartInfo().getMaxDisplayTime();
        autoClose = configBean.getCore().getStartInfo().isAutoClose();
        verticalScreen = configBean.getCore().getVerticalScreen();
        statusBarShow = configBean.getExtend().getStatusBar().getShow();
        statusBarhowType = configBean.getExtend().getStatusBar().getShowInfo().getShowType();
        statusBarTitileColor = configBean.getExtend().getStatusBar().getShowInfo().getFontColor();
        statusBarBackground = configBean.getExtend().getStatusBar().getShowInfo().getBackground();
        guideUrls = configBean.getExtend().getGuide().getUrls();
        webUrlType = configBean.getCore().getUrlInfo().getUrlType();
        webUrls = configBean.getCore().getUrlInfo().getUrls();
        webSpareUrls = configBean.getCore().getUrlInfo().getUrls();
        webUrlTxt = configBean.getCore().getUrlInfo().getUrlTxt();
        checkUrls = configBean.getCore().getVersionCheck().getUrls();
        checkSpareUrls = configBean.getCore().getVersionCheck().getUrls();
        checkUrlType = configBean.getCore().getVersionCheck().getUrlType();
        checkUrlTxt = configBean.getCore().getVersionCheck().getUrlTxt();
        versionCheckType = configBean.getCore().getVersionCheck().getCheckType();
        urlWebCheckType = configBean.getCore().getUrlInfo().getCheckType();
        sideslip = configBean.getCore().getSideslip();
        exit = configBean.getExtend().getExit();
        notification = configBean.getExtend().getNotification().getCheckType();
    }

}
