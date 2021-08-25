package com.mike.mvp;

import android.widget.TextView;
import butterknife.BindView;
import com.mike.base.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv) TextView mTv;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData() {
        mTv.setText("测试");
    }
}