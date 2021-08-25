package com.mike.base.utils;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.mike.base.R;
import java.util.List;

/** created by  wjf  at 2021/7/6 18:43 */
public class PictureSelectorUtil {

    /**
     * 选择图片或视频
     */
    public static void takePhotos(Activity activity, OnResultCallbackListener resultCallbackListener) {
        if (PermissionUtils.isGranted(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            goForPhotos(activity, 1, false, resultCallbackListener);
        } else {
            PermissionUtils.permissionGroup(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                    .callback((isAllGranted, granted, deniedForever, denied) -> {
                        if (isAllGranted) {
                            goForPhotos(activity, 1, false, resultCallbackListener);
                        }
                    }).request();
        }
    }

    /**
     * 选择图片
     */
    private static void goForPhotos(Activity activity, int max, boolean isCompress,
            OnResultCallbackListener resultCallbackListener) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .maxSelectNum(max)
                .imageSpanCount(4)
                .isPreviewImage(true)
                .isCamera(true)
                .isGif(true)//gif 取原路径
                .isCompress(isCompress)// 是否压缩
                .isEnableCrop(false)
                .queryMaxFileSize(20)//只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(FileUtil.getImageDir())//压缩图片保存地址
                .imageEngine(GlideEngine.createGlideEngine())
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(resultCallbackListener);
    }

    public static String gethoto(List<LocalMedia> list) {
        String path = "";
        if (list.size() > 0) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                if (!list.get(0).isCompressed() || list.get(0).getAndroidQToPath().toLowerCase()
                        .endsWith(".gif")) {
                    path = list.get(0).getAndroidQToPath();
                } else {
                    path = list.get(0).getCompressPath();
                }
            } else {
                if (!list.get(0).isCompressed() || list.get(0).getPath().toLowerCase()
                        .endsWith(".gif")) {
                    path = list.get(0).getPath();
                } else {
                    path = list.get(0).getCompressPath();
                }
            }
        }
        return path;
    }
}
