package com.lilong.databindingtest.data;

/**
 *　DataBinding的数据源（固定型）
 * (1) 在inflate时，其中数据被设置到UI上
 * (2) 此后数据改变，不会再被设置到UI上
 * */
public class FixedDataObj {

    /** public静态变量可在xml中使用*/
    public static String STR_STATIC = "str_static";

    /** public成员变量可在xml中使用*/
    public String str1 = "str1";

    /** private成员变量不可直接在xml中使用，必须有getter*/
    private String str2 = "str2";

    private String str3 = "str3";

    public String str4 = "str4";

    /** 有getter就行了*/
    public String getStr2(){
        return str2;
    }

    public void setStr2(String str2){
        this.str2 = str2;
    }
}
