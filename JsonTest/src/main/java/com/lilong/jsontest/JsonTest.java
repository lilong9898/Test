package com.lilong.jsontest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 测试{@link Gson}
 */
public class JsonTest {

    private static String str1 = "{a: \"a data\", b: \"b data\"}";

    // 数据中缺少一项，
    // 如果数据类是static的，则得到的数据类中相应的项为原值
    // 如果不是，则得到的数据类中相应的项会变成null
    private static String str2 = "{b: \"b data\"}";

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();

        Data data1 = gson.fromJson(str1, Data.class);
        System.out.println(data1.toString());

        Data data2 = gson.fromJson(str2, Data.class);
        System.out.println(data2.toString());
    }

    static class Data {

        public String a = "";
        public String b = "";

        @Override
        public String toString() {
            return "a = " + a + ", b = " + b;
        }
    }
}
