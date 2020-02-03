package com.lilong.javatest;

/**
 * Created by lilong on 03/02/2020.
 * 父子类都存在时的构造顺序，见
 * https://yq.aliyun.com/articles/653204?utm_content=m_1000018740
 * 规则：
 * (1) 静态先于非静态
 * (2) 父类先于子类
 * (3) 成员变量初始化先于代码块先于构造函数
 */
public class ConstructorOrderTest {

    public static void main(String[] args) {
        ChildClass c = new ChildClass(1);
    }
}

class ParentClass {

    {
        System.out.println("ParentClass 1");
    }

    protected Object context;

    public ParentClass(Object context) {
        System.out.println("ParentClass 2");
        this.context = context;
        System.out.println("ParentClass 3");
    }
}

class ChildClass extends ParentClass {

    private String parentContextStr = context.toString();

    {
        System.out.println("ChildClass 1");
    }

    public ChildClass(Object context) {
        super(context);
        System.out.println("ChildClass 2");
    }
}
