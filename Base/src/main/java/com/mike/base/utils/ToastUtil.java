package com.mike.base.utils;

import android.graphics.Color;
import android.text.TextUtils;
import com.blankj.utilcode.util.ToastUtils;

public class ToastUtil {

    public static void show(String msg) {

        //setGravity     : 设置吐司位置
        //setBgColor     : 设置背景颜色
        //setBgResource  : 设置背景资源
        //setMsgColor    : 设置消息颜色
        //setMsgTextSize : 设置消息字体大小
        //showShort      : 显示短时吐司
        //showLong       : 显示长时吐司
        //showCustomShort: 显示短时自定义吐司
        //showCustomLong : 显示长时自定义吐司
        //cancel         : 取消吐司显示

        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.make()
                    .setBgColor(Color.MAGENTA)
                    .setTextColor(Color.WHITE)
                    .setTextSize(18)
                    .show(msg);
        }
    }
}
