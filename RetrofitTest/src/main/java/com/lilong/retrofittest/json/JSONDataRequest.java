package com.lilong.retrofittest.json;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 使用了url分段拼接
 * 完整url:
 * http://japi.juhe.cn/qqevaluate/qq?key=96efc220a4196fafdfade0c9d1e897ac&qq=295424589
 * */
public interface JSONDataRequest {

    /**
     * (1) 返回值类型{@link Call}上的泛型是数据要解析成的目标类
     * (2) GET注解的属性值是path
     * (3) 参数上的Query注解表示请求参数
     * 故最终url={@link Retrofit.Builder#baseUrl(String)}中设定的host + {@link GET}注解的属性里设置的path + 方法参数中用{@link Query}注解的属性设置的query params
     * */
    @GET("/qqevaluate/qq")
    Call<JSONEntity> request(@Query("key") String appKey, @Query("qq") String qq);
}
