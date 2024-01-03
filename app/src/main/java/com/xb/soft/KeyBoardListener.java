package com.xb.soft;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

import com.xb.soft.utils.DeviceInfo;
import com.xb.soft.utils.Utils;

public class KeyBoardListener {
    private Activity activity;


    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private static KeyBoardListener keyBoardListener;


    public static KeyBoardListener getInstance(Activity activity) {
        keyBoardListener = new KeyBoardListener(activity);
        return keyBoardListener;
    }


    public KeyBoardListener(Activity activity) {
        super();
        this.activity = activity;
    }


    public void init() {
        FrameLayout content = (FrameLayout) activity
                .findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> possiblyResizeChildOfContent());
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
                .getLayoutParams();


    }


    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView()
                    .getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                frameLayoutParams.height = usableHeightSansKeyboard
//                        - heightDifference;
//            } else {
//            }
            frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }


    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        int navigationBarHeight = Utils.getNavigationBarHeight(MyApplication.application);
        if(ConfigAPP.statusBarhowType == 2&& ConfigAPP.statusBarShow == 1){
            return (r.bottom - r.top)+navigationBarHeight;
        }else{
            return (r.bottom - r.top);
        }
    }

}
