package com.mike.base.http;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonParseException;
import com.mike.base.utils.ToastUtil;
import io.reactivex.observers.DisposableObserver;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import org.json.JSONException;
import retrofit2.HttpException;

/** created by  wjf  at 2021/8/2 16:09 */
public abstract class BaseObserver<T> extends DisposableObserver<String> {

    /*========================= HttpException 异常 code ==========================*/
    private static final int UNAUTHORIZED          = 401;
    private static final int FORBIDDEN             = 403;
    private static final int NOT_FOUND             = 404;
    private static final int REQUEST_TIMEOUT       = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY           = 502;
    private static final int SERVICE_UNAVAILABLE   = 503;
    private static final int GATEWAY_TIMEOUT       = 504;

    private Type mType;

    protected BaseObserver(Class<T> classOfT) {
        mType = classOfT;
    }

    @Override
    public void onNext(String s) {
        LogUtils.e("Http BaseObserver onNext", s);
        onSuc(GsonUtils.fromJson(s, mType));
    }

    @Override
    public void onError(Throwable throwable) {
        //打印日志到控制台
        throwable.printStackTrace();
        //如果你某个地方不想使用全局错误处理,
        //则重写 onError(Throwable) 并将 super.onError(e); 删掉
        //如果你不仅想使用全局错误处理,还想加入自己的逻辑,
        //则重写 onError(Throwable) 并在 super.onError(e); 后面加入自己的逻辑
        onError(requestHandle(throwable));
    }

    /**
     * 统一处理Throwable
     *
     * @param e e
     * @return msg
     */
    private String requestHandle(Throwable e) {
        String msg;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    msg = "服务器错误";
                    break;
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException
                || e instanceof ParseException) {
            msg = "解析错误";
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            msg = "连接失败，请检查网络";
        } else if (e instanceof NumberFormatException) {
            msg = "数字格式化异常";
        } else {
            msg = "请求失败";
        }
        return msg;
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuc(T t);

    protected void onError(String msg) {
        ToastUtil.show(msg);
        LogUtils.e("Http BaseObserver onHandleError", msg);
    }
}
