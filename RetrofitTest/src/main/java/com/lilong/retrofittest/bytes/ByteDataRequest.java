package com.lilong.retrofittest.bytes;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 访问百度首页，访问得到的数据以{@link ResponseBody}形式呈现
 * */
public interface ByteDataRequest {

    @GET("http://www.baidu.com")
    Call<ResponseBody> request();

}
