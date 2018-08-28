package com.lilong.databindingtest.data;

/** data binding的数据源*/
public class DataObj {

    /** public静态变量可在xml中使用*/
    public static String STR_STATIC = "str_static";

    /** public成员变量可在xml中使用*/
    public String str1 = "str1";

    /** private成员变量不可直接在xml中使用，必须有getter*/
    private String str2 = "str2";

    /** 有getter就行了*/
    public String getStr2(){
        return str2;
    }

}
