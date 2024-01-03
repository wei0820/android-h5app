package com.xb.soft.ui;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xb.soft.R;

public class CommonDialog extends Dialog {


	private TextView titleView;
	private TextView contentView;
	private TextView okBtn;
	private TextView cancelBtn;
	private View verticalBtn;

	public CommonDialog(Context context) {
		super(context,R.style.Dialog_Fullscreen);
		setContentView(R.layout.common_dialog);
		titleView = (TextView)findViewById(R.id.title);
		contentView = (TextView)findViewById(R.id.content);
		okBtn = (TextView)findViewById(R.id.ok_btn);
		cancelBtn = (TextView)findViewById(R.id.cancel_btn);
		verticalBtn = findViewById(R.id.btn_vertical);
		okBtn.setOnClickListener(defaultOnclickListener);
		cancelBtn.setOnClickListener(defaultOnclickListener);
		setContent(null);
	}

	private View.OnClickListener defaultOnclickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public CommonDialog setDlgTitle(int resId){
		titleView.setText(resId);
		return  this;
	}

	public CommonDialog setDlgTitle(String title){
		titleView.setText(title);
		return  this;
	}

	public CommonDialog setContent(int resId){
		if(resId == 0){
			contentView.setVisibility(View.GONE);
		}else{
			contentView.setVisibility(View.VISIBLE);
			contentView.setText(resId);
		}
		return  this;
	}

	public CommonDialog setContent(String text){
		if(TextUtils.isEmpty(text)){
			contentView.setVisibility(View.GONE);
		}else{
			contentView.setVisibility(View.VISIBLE);
			contentView.setText(text);
		}

		return  this;
	}

	public CommonDialog setPositiveButton(int resId,final View.OnClickListener listener) {
		okBtn.setText(resId);
		okBtn.setOnClickListener(listener);
		return  this;
	}

	public CommonDialog setPositiveButtonColor(int color,float textsize) {
		okBtn.setTextColor(color);
		okBtn.setTextSize(textsize);
		return  this;
	}

	public CommonDialog setPositiveButtonColor(int color) {
		okBtn.setTextColor(color);
		return  this;
	}

	public CommonDialog setNegativeButtonColor(int color) {
		cancelBtn.setTextColor(color);
		return  this;
	}

	public CommonDialog setNegativeButtonColor(int color,float textsize) {
		cancelBtn.setTextColor(color);
		cancelBtn.setTextSize(textsize);
		return  this;
	}

	public CommonDialog setNegativeButton(int resId,final View.OnClickListener listener) {
		cancelBtn.setText(resId);
		cancelBtn.setOnClickListener(listener);
		return  this;
	}

	public CommonDialog setSinglePositiveButton(int resId,final View.OnClickListener listener) {
		okBtn.setText(resId);
		okBtn.setOnClickListener(listener);
		verticalBtn.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		return  this;
	}
}
