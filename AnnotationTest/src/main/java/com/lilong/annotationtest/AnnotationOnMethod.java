package com.lilong.annotationtest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解的写法跟接口基本一样
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AnnotationOnMethod {
     // 给这个注解定义一个属性，方法名就是属性名
     String testAttribute();
}
