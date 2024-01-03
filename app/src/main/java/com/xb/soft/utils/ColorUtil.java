package com.xb.soft.utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

public class ColorUtil {
    public static int getThemeColor(Context context, int attrId){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }
}
