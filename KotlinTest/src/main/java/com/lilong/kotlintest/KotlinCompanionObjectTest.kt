package com.lilong.kotlintest

fun main(args: Array<String>){
    /**
     * 调用伴生对象的方法，可直接用伴生对象所属类名.方法名
     * 或用伴生对象所属类名.Companion.方法名
     *
     * [
     *    !!!伴生对象在加载所在类的时候就会生成!!!
     *    这一点可用来做饿汉式的单例模式
     * ]
     * */
    KotlinCompanionObjectTest.methodInCompanionObject()
    KotlinCompanionObjectTest.Companion.methodInCompanionObject()
    /**
     * 下面打印出来的是companion object = com.lilong.kotlintest.KotlinCompanionObjectTest$Companion@2b193f2d
     * 说明KotlinCompanionObjectTest.Companion确实是个对象
     *
     * 从字节码可以看出，kotlin中的伴生对象，最终转换成java中的
     * (1) public static final class Companion {
     *      private Companion() {}
     * }这样一个静态内部类
     * 和(2)
     * public static final KotlinCompanionObjectTest.Companion Companion = new KotlinCompanionObjectTest.Companion((DefaultConstructorMarker)null);
     * 这样一个静态final引用
     *
     * 这其实就是java中预加载的单例模式的写法，所以kotlin中，伴生对象相对于其所属的类，是单例的，可用来实现kotlin中的等效静态方法/属性
     * */
    println("companion object = " + KotlinCompanionObjectTest.Companion)
}

class KotlinCompanionObjectTest {
    /**
     * 伴生对象
     * */
    companion object {
        fun methodInCompanionObject() {
            println("methodInCompanionObject")
        }
    }
}