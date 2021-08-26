package com.mike.base.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.mike.base.R;
import com.mike.base.http.LoadingDialogFragment;
import com.mike.base.http.RxApiManager;
import com.mike.base.ui.ZXingActivity;
import com.mike.base.utils.ImmersionBarUtil;

/** created by  wjf  at 2021/7/31 11:25 */

public abstract class BaseActivity extends AppCompatActivity {

    protected SPUtils      sp            = SPUtils.getInstance();
    public    RxApiManager mRxApiManager = new RxApiManager();

    private   Unbinder          mUnbinder;
    protected Toolbar           mToolbar;
    protected AppCompatTextView mTvLeft, mTvTitle, mTvRight;
    private LoadingDialogFragment mLoadingDialogFragment;

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
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
        mToolbar = findViewById(R.id.toolbar);
        mTvLeft = findViewById(R.id.tv_left);
        mTvTitle = findViewById(R.id.tv_title);
        mTvRight = findViewById(R.id.tv_right);

        initToolbar();
    }

    public void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(isShowBack());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back() {
        finish();
    }

    public void showDialog() {
        if (!(mLoadingDialogFragment != null && mLoadingDialogFragment.getDialog() != null
                && mLoadingDialogFragment.getDialog().isShowing())) {
            mLoadingDialogFragment = new LoadingDialogFragment.Builder().create();
            mLoadingDialogFragment.showDialog(getSupportFragmentManager(), "");
        }
    }

    public void dismissDialog() {
        if (mLoadingDialogFragment != null && mLoadingDialogFragment.getDialog() != null
                && mLoadingDialogFragment.getDialog().isShowing()) {
            mLoadingDialogFragment.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (mRxApiManager != null) {
            mRxApiManager.clear();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
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
    protected void startActivityForResult(Class<?> clazz, ActivityResultLauncher launcher) {
        startActivityForResult(clazz, launcher, null);
    }

    /**
     * startActivityForResult with bundle
     */
    protected void startActivityForResult(Class<?> clazz, ActivityResultLauncher launcher, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        launcher.launch(intent);
    }

    /**
     * startActivityForResult ZXingActivity.class
     */
    protected void goScan(ActivityResultLauncher launcher) {
        if (PermissionUtils.isGranted(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startActivityForResult(ZXingActivity.class, launcher);
        } else {
            PermissionUtils.permissionGroup(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                    .callback((isAllGranted, granted, deniedForever, denied) -> {
                        if (isAllGranted) {
                            startActivityForResult(ZXingActivity.class, launcher);
                        }
                    }).request();
        }
    }
}
