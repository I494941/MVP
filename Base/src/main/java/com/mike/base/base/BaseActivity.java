package com.mike.base.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.mike.base.http.RxApiManager;
import com.mike.base.utils.ImmersionBarUtil;

/** created by  wjf  at 2021/7/31 11:25 */

public abstract class BaseActivity extends AppCompatActivity {


    public    RxApiManager mRxApiManager = new RxApiManager();


    private boolean mIsImmersionBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        initViewAndData();
        if (mIsImmersionBar) {
            initImmersionBar();
        }
    }




    @Override
    protected void onDestroy() {
        if (mRxApiManager != null) {
            mRxApiManager.clear();
        }
        super.onDestroy();
    }

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init all views and add events
     */
    protected abstract void initViewAndData();

    /**
     * 是否显示 mToolbar 返回按钮
     */
    protected boolean isShowBack() {
        return true;
    }

    /**
     * 设置是否沉浸式状态栏,默认 true
     */
    protected void setImmersionBar(boolean isImmersionBar) {
        mIsImmersionBar = isImmersionBar;
    }

    /**
     * 沉浸式状态栏,默认 activity_color
     */
    protected void initImmersionBar() {
        ImmersionBarUtil.activityBar(this);
    }

    /**
     * startActivity
     */
    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    /**
     * startActivity with bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivityForResult
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        startActivityForResult(clazz, requestCode, null);
    }

    /**
     * startActivityForResult with bundle
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
