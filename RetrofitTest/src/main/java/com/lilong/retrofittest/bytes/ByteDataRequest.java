package com.lilong.retrofittest.bytes;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 访问百度首页，访问得到的数据以{@link ResponseBody}形式呈现，可以直接通过{@link ResponseBody#bytes()}获取到字节数据
 * 这也是下载文件的方式
 * */
public interface ByteDataRequest {

    @GET("http://www.baidu.com")
    Call<ResponseBody> request();

}
