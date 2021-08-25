package com.mike.base.http;

import io.reactivex.Observable;
import java.util.Map;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by ws on 2017/3/21 0021.
 */
public interface ApiService {

    /*
      Observable<T> 里面的泛型T 不能是  okhttp3.Response 。
      可以是 retrofit2.Response<T> ，但是T不能为okhttp3.Response
    */
    @POST()
    @Headers({ "Content-Type:application/json;charset=UTF-8" })
    Observable<Response<ResponseBody>> login(@Url String url, @Body RequestBody json);

    @POST()
    @Headers({ "Content-Type:application/json;charset=UTF-8" })
    Observable<String> post(@Url String url, @Body RequestBody json);

    @FormUrlEncoded
    @POST()
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @POST()
    Observable<String> post(@Url String url);

    @GET()
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @GET()
    Observable<String> get(@Url String url);

    @DELETE()
    Observable<String> delete(@Url String url);
}
