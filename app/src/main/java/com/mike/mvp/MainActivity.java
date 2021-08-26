package com.mike.mvp;

import android.view.View;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import butterknife.BindView;
import butterknife.OnClick;
import com.mike.base.base.BaseActivity;
import com.mike.base.ui.ZXingActivity;
import com.mike.base.utils.ToastUtil;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv) TextView mTv;

    private ActivityResultLauncher launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    mTv.setText(result.getData().getStringExtra(ZXingActivity.SCAN_RESULT));
                    ToastUtil.show(result.getData().getStringExtra(ZXingActivity.SCAN_RESULT));
                }
            });

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData() {
        mTv.setText("测试");
    }

    @OnClick({ R.id.tv })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                goScan(launcher);
                break;
        }
    }
}