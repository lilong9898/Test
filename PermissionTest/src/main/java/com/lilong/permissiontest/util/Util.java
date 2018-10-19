package com.lilong.permissiontest.util;

import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.lilong.permissiontest.application.TestApplication;

public class Util {

    /** 获取某个sdk_int的android版本的名字*/
    public static String getAndroidVersionName(int sdkInt){
        String androidVersionName = "unknown";
        switch (sdkInt){
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                androidVersionName = "4.0 ICE_CREAM_SANDWICH";
                break;
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                androidVersionName = "4.0.3 ICE_CREAM_SANDWICH_MR1";
                break;
            case Build.VERSION_CODES.JELLY_BEAN:
                androidVersionName = "4.1 JELLY_BEAN";
                break;
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
                androidVersionName = "4.2 JELLY_BEAN_MR1";
                break;
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                androidVersionName = "4.3 JELLY_BEAN_MR2";
                break;
            case Build.VERSION_CODES.KITKAT:
                androidVersionName = "4.4 KITKAT";
                break;
            case Build.VERSION_CODES.LOLLIPOP:
                androidVersionName = "5.0 LOLLIPOP";
                break;
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                androidVersionName = "5.1 LOLLIPOP_MR1";
                break;
            case Build.VERSION_CODES.M:
                androidVersionName = "6.0 MARSHMALLOW";
                break;
            case Build.VERSION_CODES.N:
                androidVersionName = "7.0 NOUGAT";
                break;
            case Build.VERSION_CODES.N_MR1:
                androidVersionName = "7.1 NOUGAT_MR1";
                break;
            case Build.VERSION_CODES.O:
                androidVersionName = "8.0 OREO";
                break;
            case 27:
                androidVersionName = "8.1 OREO";
                break;
            case 28:
                androidVersionName = "9.0 PIE";
                break;
            default:
                break;
        }
        return androidVersionName;
    }

    /** 生成当前rom的android版本信息*/
    public static SpannableString generateAndroidVersionInfo(){
        SpannableString spannableString = new SpannableString(String.format("当前系统Android版本是%s", getAndroidVersionName(Build.VERSION.SDK_INT)));
        RelativeSizeSpan span = new RelativeSizeSpan(1.2f);
        spannableString.setSpan(span, 14, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    /** 以及当前应用的targetSdkVersion信息*/
    public static SpannableString generateTargetSdkVersionInfo(){
        SpannableString spannableString = new SpannableString(String.format("当前应用targetSdkVersion是%s", getAndroidVersionName(TestApplication.getInstance().getApplicationInfo().targetSdkVersion)));
        RelativeSizeSpan span = new RelativeSizeSpan(1.2f);
        spannableString.setSpan(span, 21, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
