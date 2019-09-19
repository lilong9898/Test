package com.lilong.jsontest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 测试{@link Gson}
 */
public class JsonTest {

    private static String str1 = "{a: \"a data\", b: \"b data\"}";

    // 数据中缺少一项，得到的数据类中相应的项会变成null
    private static String str2 = "{b: \"b data\"}";

    // 数据中有一项是null，得到的数据类中相应的项会变成null
    private static String str3 = "{a: null, b: \"b data\"}";

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();

        Data data1 = gson.fromJson(str1, Data.class);
        System.out.println(data1.toString());

        Data data2 = gson.fromJson(str2, Data.class);
        System.out.println(data2.toString());

        Data data3 = gson.fromJson(str3, Data.class);
        System.out.println(data3.toString());
    }

    class Data {

        public String a = "";
        public String b = "";

        @Override
        public String toString() {
            return "a = " + a + ", b = " + b;
        }
    }
}
