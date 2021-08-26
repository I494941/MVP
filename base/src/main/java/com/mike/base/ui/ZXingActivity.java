package com.mike.base.ui;

import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.mike.base.R;
import com.mike.base.R2;
import com.mike.base.base.BaseActivity;
import com.mike.base.utils.ImmersionBarUtil;
import com.mike.base.utils.PictureSelectorUtil;
import com.mike.base.utils.ViewSizeUtil;
import java.util.List;

/**
 * @创建者 wjf
 * @创建时间 2019/8/1 16:24
 * @描述 ${TODO}
 */
public class ZXingActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R2.id.view)           View         mView;
    @BindView(R2.id.app_bar_layout) AppBarLayout mAppBarLayout;
    @BindView(R2.id.zxingview)      ZXingView         mZXingView;
    @BindView(R2.id.tv_light)       AppCompatTextView mTvLight;

    private boolean mLightTurnOn = false;

    public static String SCAN_RESULT = "SCAN_RESULT";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void initViewAndData() {
        mTvTitle.setText(R.string.scan);
        mTvRight.setText(R.string.album);
        mAppBarLayout.setBackgroundColor(getColor(R.color.transparent));
        mZXingView.setDelegate(this);
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBarUtil.activityImmersionBar(this, mView);
    }

    @OnClick({ R2.id.tv_light, R2.id.tv_right })
    public void onClick(View view) {
        if (R.id.tv_light == view.getId()) {
            mLightTurnOn = !mLightTurnOn;
            mTvLight.setText(getString(
                    mLightTurnOn ? R.string.click_to_turn_off_the_light : R.string.click_to_turn_on_the_light));
            ViewSizeUtil.setDrawableTop(mTvLight,
                    mLightTurnOn ? R.drawable.ic_lighting_off : R.drawable.ic_lighting_on, 0);
            if (mLightTurnOn) {
                mZXingView.openFlashlight();
            } else {
                mZXingView.closeFlashlight();
            }
        } else if (R.id.tv_right == view.getId()) {
            PictureSelectorUtil.takePhotos(this, new OnResultCallbackListener<LocalMedia>() {
                @Override
                public void onResult(List<LocalMedia> result) {
                    mZXingView.decodeQRCode(PictureSelectorUtil.gethoto(result));
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mZXingView.startCamera(); // 打开后置摄像头开始预览,但是并未开始识别
        mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
        mZXingView.startSpotAndShowRect(); // 显示扫描框,并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览,并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.e("ZXingActivity****onScanQRCodeSuccess", "result = " + result);
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);

        Intent intent = new Intent();
        intent.putExtra(SCAN_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtils.e("ZXingActivity****onScanQRCodeOpenCameraError", "打开相机出错");
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态,接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText              = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗,请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
            mTvLight.setVisibility(View.VISIBLE);
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }
}
