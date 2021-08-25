package com.mike.base.http;

import android.text.TextUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mike.base.constant.Config;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/** created by  wjf  at 2021/8/2 14:04 */
public class HttpManager {

    private int HTTP_READ_TIME_OUT    = 10;
    private int HTTP_CONNECT_TIME_OUT = 10;
    private int HTTP_WRITE_TIME_OUT   = 10;

    private Builder                 mBuilder;
    private OkHttpClient            mOkHttpClient;
    private Map<String, ApiService> mApiServiceMap = new HashMap<>();

    private HttpManager() {
        initOkHttpClient();
    }

    public static HttpManager getInstance() {
        return HttpManagerInternalClassHolder.instance;
    }

    public static class HttpManagerInternalClassHolder {

        private static final HttpManager instance = new HttpManager();
    }

    public void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (LogUtils.getConfig().isLogSwitch()) {//debug
            builder.addInterceptor(new BaseInterceptor<>(null))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        mOkHttpClient = builder.readTimeout(HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(HTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new TokenHeaderInterceptor())//添加token
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .hostnameVerifier(new AllowAllHostnameVerifier())
                .build();
        mApiServiceMap.put(Config.getInstance().getBaseUrl(),
                createRetrofitClient(Config.getInstance().getBaseUrl()).create(ApiService.class));
    }

    private Retrofit createRetrofitClient(String url) {
        return new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void post(DisposableObserver<String> observer, RxApiManager rxApiManager) {
        Observable<String> observable;
        if (!TextUtils.isEmpty(mBuilder.body)) {
            observable = getApiService().post(mBuilder.url, getBody(mBuilder.body));
        } else if (!mBuilder.params.isEmpty()) {
            observable = getApiService().post(mBuilder.url, mBuilder.params);
        } else {
            observable = getApiService().post(mBuilder.url);
        }
        request(observable, observer, rxApiManager);
    }

    public void login(Observer<Response<ResponseBody>> observer) {
        Observable<Response<ResponseBody>> observable = getApiService()
                .login(mBuilder.url, getBody(mBuilder.body));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }

    public void get(DisposableObserver<String> observer, RxApiManager rxApiManager) {
        Observable<String> observable;
        if (!mBuilder.params.isEmpty()) {
            observable = getApiService().get(mBuilder.url, mBuilder.params);
        } else {
            observable = getApiService().get(mBuilder.url);
        }
        request(observable, observer, rxApiManager);
    }

    public void delete(DisposableObserver<String> observer, RxApiManager rxApiManager) {
        Observable<String> observable = getApiService().delete(mBuilder.url);
        request(observable, observer, rxApiManager);
    }

    private ApiService getApiService() {
        if (mApiServiceMap.get(mBuilder.baseUrl) == null) {
            mApiServiceMap.put(mBuilder.baseUrl, createRetrofitClient(mBuilder.baseUrl).create(ApiService.class));
        }
        return mApiServiceMap.get(mBuilder.baseUrl);
    }

    /**
     * 将请求的json字符串转化为RequestBody
     */
    private RequestBody getBody(String jsonStr) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            return RequestBody.create(jsonStr, mediaType);
        } catch (Exception e) {
            LogUtils.e("RequestBody is null");
            e.printStackTrace();
        }
        return null;
    }

    private void request(Observable<String> observable, DisposableObserver<String> observer,
            RxApiManager rxApiManager) {
        if (rxApiManager != null) {
            rxApiManager.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer));
        }
    }

    public Builder initBuilder() {
        mBuilder = new Builder();
        return mBuilder;
    }

    public class Builder {

        private String              baseUrl = Config.getInstance().getBaseUrl();
        private String              url;
        private String              body;
        private Map<String, Object> params  = new HashMap<>();

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder params(String key, Object value) {
            this.params.put(key, value);
            return this;
        }

        public HttpManager build() {
            LogUtils.e("HttpManager",
                    "baseUrl=" + baseUrl + ",url=" + url + ",params.toString() = " + params.toString());
            return HttpManager.this;
        }
    }
}
