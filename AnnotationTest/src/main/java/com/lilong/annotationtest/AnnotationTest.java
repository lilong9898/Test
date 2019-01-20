package com.lilong.annotationtest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类/方法/成员变量上的注解对象和注解对象的属性值
 * 只能从类对象/方法对象/成员对象来获取
 * [无法]不通过反射来直接获取
 * */
public class AnnotationTest {

    public static void main(String[] args){

        TestClass test = new TestClass();
        boolean isAnnotationPresent = test.getClass().isAnnotationPresent(AnnotationOnClass.class);

        // 获取类上的注解（注意：这个方法只能获取在类上的注解，无法获取在成员变量或者方法上的注解）
        System.out.println("");
        System.out.println("isAnnotationPresent = " + isAnnotationPresent);
        System.out.println("test class has annotations of:");
        for (Annotation a: test.getClass().getAnnotations()) {
            System.out.println(a);
        }

        // 获取方法上的注解
        test.testMethod();
    }

    /**
     * 类的初始化过程中，按照静态初始块->非静态初始块->构造方法的顺序执行
     * */
    @AnnotationOnClass
    static class TestClass {

        static {
            System.out.println("static block 1");
        }

        {
            System.out.println("dynamic block 1");
        }

        public TestClass(){
            System.out.println("constructor");
        }

        static {
            System.out.println("static block 2");
        }

        {
            System.out.println("dynamic block 2");
        }

        {
            // 获取成员变量上的注解
            try{
                Field field = getClass().getDeclaredField("testField");
                Annotation[] annotations = field.getAnnotations();
                System.out.println();
                System.out.println("test field has annotations of:");
                for (Annotation a:annotations) {
                    System.out.println(a);
                }

                // 获取成员变量上的注解的属性的值，如果属性叫value，则在使用注解时可不写"value="部分
                Annotation a = field.getAnnotation(AnnotationOnField.class);
                String value = ((AnnotationOnField) a).value();
                System.out.println();
                System.out.println("test field's annotation of AnnotationOnField has value = " + value);

            }catch (Exception e){}
        }

        // 如果属性叫value，则在使用注解时可不写"value="部分
        @AnnotationOnField("b")
        private int testField = 0;

        @AnnotationOnMethod(testAttribute = "a")
        public void testMethod(){
            // 获取方法上的注解
            try{
                Method method = getClass().getDeclaredMethod("testMethod", null);
                Annotation[] annotations = method.getDeclaredAnnotations();
                System.out.println("");
                System.out.println("test method has annotations of:");
                for (Annotation a: annotations) {
                    System.out.println(a);
                }

                System.out.println("");
                // 获取方法上的注解的属性的值
                Annotation a = method.getAnnotation(AnnotationOnMethod.class);
                String attribute = ((AnnotationOnMethod) a).testAttribute();
                System.out.println("test method's annotation of AnnotationOnMethod has testAttribute = " + attribute);
            }catch (Exception e){

            }
        }
    }
}
