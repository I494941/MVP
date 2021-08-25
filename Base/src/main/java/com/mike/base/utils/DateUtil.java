package com.mike.base.utils;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** created by  wjf  at 2021/7/2 9:25 */
public class DateUtil {

    /**
     * 将 yyyyMMddHHmmss 格式转换为 yyyy-MM-dd HH:mm:ss 时间格式
     * 身份证信息识别用
     */
    public static String getTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        time = time.replace("T", "");
        time = time.replace("Z", "");

        SimpleDateFormat sdfBefore = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        SimpleDateFormat sdfAfter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date             date      = null;//日期的获取
        try {
            date = sdfBefore.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }

        //把当前得到的时间用date.getTime()的方法写成时间戳的形式，再加上8小时对应的毫秒数
        long rightTime = date.getTime() + 8 * 60 * 60 * 1000;
        return sdfAfter.format(rightTime);
    }
}
