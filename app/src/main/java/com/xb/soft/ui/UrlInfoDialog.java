package com.xb.soft.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xb.soft.R;
import com.xb.soft.utils.Utils;

public class UrlInfoDialog extends Dialog {

    private static final String TAG = "UrlInfoDialog";
    private TextView tv_reset;
    private TextView tv_out;
    private TextView tv_net_type;
    private TextView tv_ip;
    private TextView tv_date_time;
    private ImageView iv_net_type;
    private ImageView iv_ip;
    private ListView lv;
    private final ListAdapter adapter;

    public UrlInfoDialog(@NonNull Context context, ListAdapter adapter) {
        super(context, R.style.MyDialogStyle);
        this.adapter = adapter;
        setContentView(R.layout.dialog_urlinfo);
        tv_net_type = findViewById(R.id.tv_net_type);
        tv_ip = findViewById(R.id.tv_ip);
        tv_date_time = findViewById(R.id.tv_date_time);
        iv_net_type = findViewById(R.id.iv_net_type);
        iv_ip = findViewById(R.id.iv_ip);
        getSystemInfo(context);
        tv_reset = findViewById(R.id.tv_reset);
        tv_out = findViewById(R.id.tv_out);
        lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
    }

    private void getSystemInfo(Context context) {
        String netState = Utils.getNetworkState(context);
        tv_net_type.setText(netState);
        if (netState.equals("当前无网络连接")) {
            iv_net_type.setBackgroundResource(R.drawable.close);
        }
        String ip = Utils.getNetIp();
        tv_ip.setText(ip);
        if (ip == null || ip.equals("当前无网络连接")) {
            iv_ip.setBackgroundResource(R.drawable.close);
        }
        String dateTime = Utils.getDateTime();
        tv_date_time.setText(dateTime);
    }

    public void setResetButton(final View.OnClickListener listener) {
        tv_reset.setOnClickListener(listener);
    }

    public void setOutButton(final View.OnClickListener listener) {
        tv_out.setOnClickListener(listener);
    }
}
