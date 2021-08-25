package com.mike.base.http;

import android.text.TextUtils;
import com.blankj.utilcode.util.SPUtils;
import com.mike.base.constant.Constants;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** created by  wjf  at 2021/6/7 17:14 */
//在请求头里添加token的拦截器处理
public class TokenHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SPUtils.getInstance().getString(Constants.TOKEN);
        if (TextUtils.isEmpty(token)) {
            Request originalRequest = chain.request();
            return chain.proceed(originalRequest);
        } else {
            Request originalRequest = chain.request();
            //key的话以后台给的为准，我这边是叫token
            Request updateRequest = originalRequest
                    .newBuilder()
                    .header("Content_Type", "application/json;charset:UTF-8")
                    .header("X-Auth-Token", token)
                    .build();
            //LogUtils.e("TokenHeaderInterceptor 请求头 = " + updateRequest.headers().toString());

            return chain.proceed(updateRequest);
        }
    }
}
