package com.mike.base.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mike.base.R;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

/**
 * DialogFragment 实现沉浸式的基类
 *
 * @author geyifeng
 * @date 2017 /8/26
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public static final int SCREEN_TYPE_1 = 1;//1:宽:90%,高:WRAP_CONTENT,Gravity.CENTER
    public static final int SCREEN_TYPE_2 = 2;//2:宽:MATCH_PARENT,高:WRAP_CONTENT,Gravity.BOTTOM
    public static final int SCREEN_TYPE_3 = 3;//3:宽:50%,高:WRAP_CONTENT,Gravity.CENTER
    public static final int SCREEN_TYPE_4 = 4;//3:宽:50%,高:MATCH_PARENT,Gravity.RIGHT,右

    private   Unbinder mUnbinder;
    protected Toolbar  mToolbar;
    protected View     mStatusBarView;
    protected View     mRootView;
    protected Window   mWindow;
    protected SPUtils  sp;
    protected int      mScreenType;//0:默认全屏

    private   boolean mIsFirst = true;
    private   boolean mIsDialogShowing;//dialog 状态
    protected boolean mDialogReuse;//dialog 是否 重复使用

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        sp = SPUtils.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);

        LogUtils.e("BaseDialogFragment   : mIsFirst" + mIsFirst
                + ",mIsDialogShowing" + mIsDialogShowing);
        if (mDialogReuse) {
            if (mIsFirst) {
                mIsFirst = false;
            } else if (!mIsDialogShowing) {
                dialog.hide();
            } else {
                initImmersionBar();
            }
        }
        mWindow = dialog.getWindow();
        if (SCREEN_TYPE_1 == mScreenType) {
            mWindow.setGravity(Gravity.CENTER);
            mWindow.setLayout(ScreenUtils.getScreenWidth() * 9 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (SCREEN_TYPE_2 == mScreenType) {
            mWindow.setGravity(Gravity.BOTTOM);
            mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (SCREEN_TYPE_3 == mScreenType) {
            mWindow.setGravity(Gravity.CENTER);
            mWindow.setLayout(ScreenUtils.getScreenWidth() * 5 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (SCREEN_TYPE_4 == mScreenType) {
            mWindow.setGravity(Gravity.RIGHT);
            mWindow.setLayout(ScreenUtils.getScreenWidth() * 5 / 10, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        LogUtils.e(" BaseDialogFragment : onStart ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        LogUtils.e(" BaseDialogFragment : onCreateView ");
        return mRootView;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initData();
        initScreenType();
        initDialogReuse();
        LogUtils.e(" BaseDialogFragment : onViewCreated ");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (mDialogReuse) {
            hideDialog();
        } else {
            super.onDismiss(dialog);
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        if (mStatusBarView != null) {
            ImmersionBar.with(this)
                    .statusBarView(mStatusBarView)
                    .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                    .keyboardEnable(false)  //解决软键盘与底部输入框冲突问题
                    .init();
        } else if (mToolbar != null) {
            ImmersionBar.with(this)
                    .titleBar(mToolbar)
                    .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                    .keyboardEnable(false)  //解决软键盘与底部输入框冲突问题
                    .init();
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                    .keyboardEnable(false)  //解决软键盘与底部输入框冲突问题
                    .init();
        }
    }

    /**
     * view与数据绑定,初始化数据
     */
    protected void initData() {

    }

    /**
     * 不全屏时,设置类型
     */
    protected void initScreenType() {

    }

    /**
     * 设置dialog 是否重复使用 ,
     */
    protected void initDialogReuse() {

    }

    public void showDialog(FragmentManager manager, String tag) {
        mIsDialogShowing = true;
        if (!isAdded()) {
            showAllowingStateLoss(manager, tag);
        } else {
            initImmersionBar();
            getDialog().show();
        }
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public void hideDialog() {
        mIsDialogShowing = false;
        getDialog().hide();
        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .init();
    }

    public void dismiss() {
        dismissAllowingStateLoss();
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
        Intent intent = new Intent(getActivity(), clazz);
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
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
