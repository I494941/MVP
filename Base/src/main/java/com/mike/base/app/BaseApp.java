package com.mike.base.app;

import android.app.Application;

/** created by  wjf  at 2021/6/9 14:28 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new MyLifeCycleCallBack());
    }
}
