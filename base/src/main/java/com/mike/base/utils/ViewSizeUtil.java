package com.mike.base.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import com.blankj.utilcode.util.SizeUtils;

public class ViewSizeUtil {

    public static void setPadding(View view, int start, int top, int end, int bottom) {
        view.setPadding(SizeUtils.dp2px(start), SizeUtils.dp2px(top), SizeUtils.dp2px(end), SizeUtils.dp2px(bottom));
    }

    public static void setDrawableNull(TextView tv) {
        if (tv != null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public static void setDrawableStart(TextView tv, int drawable, int drawablePadding) {
        setTextViewDrawable(tv, 0, drawable, drawablePadding);
    }

    public static void setDrawableTop(TextView tv, int drawable, int drawablePadding) {
        setTextViewDrawable(tv, 1, drawable, drawablePadding);
    }

    public static void setDrawableEnd(TextView tv, int drawable, int drawablePadding) {
        setTextViewDrawable(tv, 2, drawable, drawablePadding);
    }

    public static void setTextViewDrawable(TextView tv, int type, int drawable, int drawablePadding) {
        if (tv != null) {
            Drawable drawable1 = null;
            if (0 != drawable) {
                try {
                    drawable1 = AppCompatResources.getDrawable(tv.getContext(), drawable);
                } catch (Exception e) {

                }
            }
            if (1 == type) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
            } else if (2 == type) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable1, null);
            } else if (3 == type) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable1);
            } else {
                tv.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null);
            }
            tv.setCompoundDrawablePadding(SizeUtils.dp2px(drawablePadding));
        }
    }
}