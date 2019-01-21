package com.lilong.retrofittest.rxjava2;

import com.lilong.retrofittest.json.JSONEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * retrofit配合rxjava2使用
 * 这里网络访问的方法返回的不再是{@link Call}，而是RxJava中的{@link Observable}
 * */
public interface ObservableDataRequest {

    @GET("qqevaluate/qq")
    Observable<JSONEntity> request(@Query("key") String appKey, @Query("qq") String qq);
}
