package com.lilong.retrofittest.json;

import com.google.gson.Gson;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * JSON数据bean
 * {@link GsonConverterFactory#create()}会使用{@link Gson}对字节数据进行反序列化得到这个JSONEntity
 * 这个JSONEntity的成员变量可以是private的
 */
public class JSONEntity {

    private int error_code;
    private String reason;
    private Result result;

    static class Result {
        private Data data;

        static class Data {
            public String conclusion;
            public String analysis;
        }
    }
}
