package com.xb.soft.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xb.soft.Global;
import com.xb.soft.R;


public class BackBarView extends FrameLayout {
	private Context mContext;
	private TextView titleView;

	private ImageView backBtn;
	private OnClickListener backClickListener;
	private OnClickListener rightClickListener;
	private ImageView mRightImg;
	private TextView mRightTxt;
	public static final int RES_ID_BACK = R.id.back_btn;
	public static final int RES_ID_RIGHT_TEXT = R.id.right_text;
	public static final int RES_ID_RIGHT_IMG = R.id.right_img;


	private int mRightType = Global.BarRightType.NONE;

	public BackBarView(Context context) {
		this(context, null);
	}
	public BackBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BackBarView);
		initAttrs(array);
		array.recycle();
	}

	private void initAttrs(TypedArray array){
		int rightType = array.getInt(R.styleable.BackBarView_rightType, Global.BarRightType.NONE);
		setRightType(rightType);

		int rightIconId = array.getResourceId(R.styleable.BackBarView_rightIcon,0);
		setRightIconId(rightIconId);
		mRightImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rightClickListener != null) {
					rightClickListener.onClick(v);
				}
			}
		});

		int rightTextId = array.getResourceId(R.styleable.BackBarView_rightText,0);
		setRightTextResource(rightTextId);
		mRightTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rightClickListener != null) {
					rightClickListener.onClick(v);
				}
			}
		});

		int titleResId = array.getResourceId(R.styleable.BackBarView_titleText, 0);
		setTitle(titleResId);

		boolean isBackHide = array.getBoolean(R.styleable.BackBarView_backHide,false);
		setBackBtnVisible(!isBackHide);
	}

	public void setBackClickListener(OnClickListener backClickListener) {
		this.backClickListener = backClickListener;
	}

	public void setRightClickListener(OnClickListener rightClickListener) {
		this.rightClickListener = rightClickListener;
	}

	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.back_bar_with_state_bar, this);
		titleView = (TextView) findViewById(R.id.title);
		backBtn = (ImageView)findViewById(RES_ID_BACK);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (backClickListener != null) {
					backClickListener.onClick(view);
				}
			}
		});

		mRightImg = (ImageView) findViewById(RES_ID_RIGHT_IMG);
		mRightTxt = (TextView) findViewById(RES_ID_RIGHT_TEXT);
	}

	public void setBackBtnVisible(boolean isVisible){
		backBtn.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	}

	public void setTitle(int titleResId){
		if(titleResId > 0){
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(titleResId);
		}else{
			titleView.setVisibility(View.GONE);
		}
	}

	public void setTitle(String title){
		if(title != null){
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
		}else{
			titleView.setVisibility(View.GONE);
		}
	}

	public void setRightBtnEnable(boolean enable){
		if(mRightType == Global.BarRightType.ICON){
			mRightImg.setEnabled(enable);
		}else if(mRightType == Global.BarRightType.TEXT){
			mRightTxt.setEnabled(enable);
		}
	}

	public void setRightType(int rightType){
		this.mRightType = rightType;
		if(mRightType == Global.BarRightType.NONE){
			mRightTxt.setVisibility(View.GONE);
			mRightImg.setVisibility(View.GONE);
		}else if(mRightType == Global.BarRightType.TEXT){
			mRightTxt.setVisibility(View.VISIBLE);
			mRightImg.setVisibility(View.GONE);

		}else{
			mRightTxt.setVisibility(View.GONE);
			mRightImg.setVisibility(View.VISIBLE);
		}
	}

	public int getRightType() {
		return mRightType;
	}

	public void setRightIconId(int resId){
		if(resId > 0){
			mRightImg.setImageResource(resId);
		}else{
			mRightImg.setImageResource(R.drawable.btn_ok);
		}
	}

	public void setRightTextResource(int resId){
		if(resId > 0){
			mRightTxt.setText(getContext().getString(resId));
		}else{
			mRightTxt.setText(getContext().getString(R.string.ok));
		}
	}

	public String getTitle(){
		return titleView.getText() == null ? "" :titleView.getText().toString();
	}
}
