package com.lilong.retrofittest.string;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 访问百度首页
 *
 * 接口，每个方法代表一种网络请求
 * 具体实现不由用户负责，由{@link Retrofit#create(Class)}负责
 * 其内部会使用动态代理，返回的实际是代理类的实例
 * 同时会在其内部生成委托类，实际执行网络请求动作的是委托类
 */
public interface StringDataRequest {

    // 这个GET注解可写属性值[可写可不写]

    /**
     * 必须包含{@link retrofit2.http}包里面的某一种注解
     * 因为生成委托类的过程中
     * {@link ServiceMethod.Builder#build()}内会检测这个方法是否有这种注解
     * 如果没有，说明这不是个用于retrofit框架的方法，就会报异常
     *
     * 下面两个操作
     * (1) 写入注解的属性值，是后段url
     * (2) 方法至少有一个参数，且第一个参数的意思是后段url，并且用{@link Url}注解
     * 两个操作里必须有一个，否则抛异常：
     * java.lang.IllegalArgumentException: Missing either @GET URL or @Url parameter.for method ....
     * 因为必须指定后段url，跟{@link Retrofit.Builder#baseUrl(String)}设置的前段url配合起来才能构成完整的url
     */
    @GET("http://www.baidu.com")
    Call<String> request();

}
