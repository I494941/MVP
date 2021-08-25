package com.mike.base.utils;

import android.app.Activity;
import android.view.View;
import com.bossay.base.R;
import com.gyf.immersionbar.ImmersionBar;

/**
 * created by  wjf  at 2020/3/24 17:22
 */
public class ImmersionBarUtil {

    public static void activityBar(Activity activity) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.pink)     //状态栏颜色，不写默认透明色
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .init();  //必须调用方可应用以上所配置的参数
    }

    public static void activityImmersionBar(Activity activity) {
        ImmersionBar.with(activity).init();
    }

    public static void activityImmersionBar(Activity activity, View view) {
        ImmersionBar.with(activity)
                .statusBarView(view)
                .init();
    }
}
