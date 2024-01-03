package com.xb.soft.net;

import android.net.SSLCertificateSocketFactory;

import com.xb.soft.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetClient {
    private static NetClient netClient;
    private NetClient(){
        client = initOkHttpClient();
    }
    public final OkHttpClient client;
    private OkHttpClient initOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        return okHttpClient;
    }
    public static NetClient getNetClient(){
        if(netClient == null){
            netClient = new NetClient();
        }
        return netClient;
    }

    public void callGetNet(String url, final MyCallBack mCallback){
        Request request = new Request.Builder().url(url).get().build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure",e.getMessage());
                mCallback.onFailure(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mCallback.onResponse(response.body().string());
                }else{
                    mCallback.onFailure(response.code());
                }
            }
        });
    }

    public void callPingNet(String url, final MyCallBack mCallback){
        Request request = new Request.Builder().url(url).get().build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallback.onFailure(-1);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mCallback.onResponse(response.body().string());
                }else{
                    mCallback.onFailure(response.code());
                }
            }
        });
    }

}
