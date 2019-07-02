package com.lilong.kotlintest

/**
 * main方法可以写在class之外
 * 执行时会生成KotlinElementariesKt.class的类，包含这个main方法
 * 生成的类位置在build/tmp/kotlin-classes/debug/com/lilong/kotlintest/KotlinElementariesKt.class
 * 如果没有class之外的main方法，就不生成KotlinElementariesKt.class
 *
 *
 * 注意：main方法的运行键要多点几次，前两次必失败，找不到类，第三次就可以了，是工具的bug
 * */
//fun main(args: Array<String>) {
//    println("b")
//    packageLevelFunction()
//}

/**
 * 包级函数
 * 上面的main方法就是包级函数的例子
 * 所有包级函数实际上都属于{类名Kt.class}这个类（由构建工具生成）
 * */
fun packageLevelFunction() {
    println("Package level function print $packageLevelVariable")
}

/**
 * 包级变量，原理同包级函数
 * */
var packageLevelVariable = 1

/**
 * kotlin文件最终编译为java class字节码(jvm1.6)，所以
 * (1) kotlin也是静态类型的面向对象的语言
 * (2) kotlin的语法元素与java的一一对应
 * (3)　字节码最多拥有java 1.6的能力
 * (4) 与java一样，kotlin中的变量，也要求在声明时，必须通过
 *     (4.1) 类型声明
 *     (4.2) 初始值
 *     (4.3) 委托
 *     中的一种方式确定类型
 * (5) kt中的类的成员变量叫属性(property)
 * */
class KotlinElementaries {

    /**
     * kotlin中所有属性默认为public，因为构建工具会给它生成getter setter的字节码
     * 从最终的字节码执行情况看：
     * (1) 在类内部访问，自然是可以直接访问的
     * (2) 在类外部访问，会调用生成的getter/setter
     *
     * 指定为private的属性，构建工具不会给其生成getter/setter
     */

    //-----------------以下为var关键字标记的变量，表示可变的-------------------
    // 属性都需要设定初始值，带lateinit关键字的除外
    var variableA: String = "a";

    // kotlin是静态类型的，但属性在声明时可以不指定类型，由编译器根据初始值去推断
    var variableB = "b"

    // lateinit关键字表示该变量在声明时不写初始值，到后面再写初始值
    lateinit var lateinitVariable: String

    // 未指定类型的变量，可以赋值为null
    var variableC = null

    // 指定类型的变量，不可赋值为null，否则编译不过
//    var variableD:String = null

    //
    //
    /**
     * ?号表示变量，参数或方法的返回值可能为null
     * 后续出现这个变量的时候，可以选择两种处理方式：
     * (1) variableD?.xxxx 表示如果variableD是null的话，就什么都不做，一定不会抛空指针异常
     * (2) variableD!!.xxx 表示如果variableD是null的话，就抛出空指针异常，这种写法通常用在无法使用?符号的地方（比如访问variableE::class）
     * */
    var variableD: String? = null

    fun printNullableVariable() {
        // 下面一句不加?编译器会报错，相当于对可空性做强制检查，对于可空对象，必须用?.或!!.来调用其方法
        println(variableD?.chars())
    }

    /**
     * kotlin中没有基本类型，全部使用包装类，数组是Array类
     *
     * java中的基本类型对应为kt中的Primitives.kt中的类
     * 而这些类对应到java中的基本类型的包装类
     * */
    var variableE: Int? = 20

    fun printVariableType() {
        println("type is " + variableE!!::class)
        println("type is " + variableE!!::class.java)
    }

    lateinit var variableF: Any

    fun testSmartCast(arg: Any) {

        /**
         * is进行类型判断
         * 判断是Int之后，就可以用Int的方法了，这就是smart cast
         * 方法参数是val（常量）类型
         * 所以编译器可以确定一旦类型判断成功，就一定是Int类型，就可以smart cast
         * */
        if (arg is Int) {
            arg.minus(3)
        }

        /**
         * 离开类型判断的block后，就不能用smart cast了
         * */
//        arg.minus(3)

        /**
         *
         * 但这里的variableE是类的nullable且可变的属性，有可能在类型判断成功后又被其它线程置成null了
         * 所以不允许smart cast
         * 需要加上?以考虑null的情况
         * */
        if (variableE is Int) {
//            variableE.minus(3)
            variableE?.minus(3)
        }

        /**
         * variableF是Any类型的，在类型判断成功后，可能被被其它线程置成null，或者任意类型
         * 所以不允许smart cast，必须用as操作符手动转换
         * as?是安全的强制转换，如果强转失败，会返回null
         * */
        variableF = 1
        if (variableF is Int) {
//            variableF?.minus(3)
            (variableF as Int).minus(3)
        }

        println("smart cast string length = " + (variableF as? String)?.length)
    }

    /**
     * 三重引号"""中包裹的是原始字符串，其中的换行/格式等信息会全部保留
     * */
    fun printRawString() {
        var rawString: String = """ line1
            line2
            line3
        """
        println(rawString)
    }

    /**
     * when关键字，类似java里的switch关键字
     * */
    fun testWhen() {
        var a = 3
        when (a) {
            1 -> {
                println("condition1 met")
            }
            2 -> {
                println("condition2 met")
            }
            else -> {
                println("else met")
            }
        }
    }

    fun testFor() {
        // kt中，数组是Array，数组长度/下标等都是其属性
        // 用arrayOf生成一个数组，这是内联函数
        var array: Array<Int> = arrayOf(1, 2, 3)
        println("loop through array")
        for (i in array.indices) {
            println(i)
        }
    }

    /**
     * 函数参数都是val类型，是常量，不能再被赋值
     * */
    fun testFunc(str:String): Int {
//        str = "1"
        return 3
    }

    /**
     * 打印拼接的字符串
     * kotlin支持类似groovy语法的字符串拼接
     * */
    fun printConcatenatedString() {
        var element1: String = "element1"
        var element2: String = "element2"
        println("element name 1 = ${element1}, element name 2 = ${element2}")
    }

    //-----------------以下为val关键字标记的变量，表示常量，跟java的final是一样的---------------
    val constantA: String = "constant"

    fun change() {
        // val不可被再次赋值
//        constantA = "b";
    }

    /**
     * companion关键字表示伴生
     * companion object为伴生对象
     * 一个类中只能有一个伴生对象，这样一来伴生对象就跟类有了一一对应的关系
     * 所以kotlin中移除了static关键字，用companion object代替
     *
     * 在字节码中对应的是public static final KotlinElementaries.Companion companion对象
     * 而Companion这个类是个public static final class
     *
     * */
    companion object {

        // static标记，kotlin中没有java的static关键字，而是通过kotlin注解来实现
        @JvmStatic
        // main方法也可写在类内部的companion object里
        fun main(args: Array<String>) {
            // 直接
            println("a")
            // 创建对象：kotlin里不使用new关键字
            var c = KotlinElementaries()
            // 可为空的变量，如果真的为空，则被调用方法时不会崩溃，而是什么都不做
            // 打印时会打出字符串"null"
            c.printNullableVariable()
            // 打印拼接的字符串
            c.printConcatenatedString()
            // 打印变量类型
            c.printVariableType()
            //　调用包级方法
            packageLevelFunction()
            // 测试smartCast
            c.testSmartCast(2)
            // 打印原始字符串
            c.printRawString()
            // 测试when关键字
            c.testWhen()
            // 测试for关键字
            c.testFor()
            // 测试方法返回值
            println(c.testFunc("1"))
            // 测试操作符重载
            c.testOperatorOverride()
            // 测试方法扩展
            c.testMethodExpansion()
            // 测试属性扩展
            c.testPropertyExpansion()
        }
    }

    fun testOperatorOverride() {
        var a: Data = Data(3)
        var b: Data = Data(2)
        var c: Data = a + b;
        println("result = ${c.number}")
    }

    /** 数据类，该类的"+"操作符被重载*/
    class Data {

        constructor(number: Int) {
            this.number = number
        }

        /**
         * kotlin中的操作符重载，java中没有
         * 这里重载"+"号
         * */
        operator fun plus(other: Data): Data {
            var result = Data(0)
            result.number = this.number + other.number
            return result;
        }

        var number: Int = 0
    }

    /**
     * 方法扩展：在现有的类上增加一个方法，比如这里在Int类型上增加一个方法expandedMethod
     * 其原理是在用户类的字节码中加入扩展方法等效的字节码：
     * 在方法扩展所处的类中，比如这里是KotlinElementariesKt中，增加一个方法
     * 这个方法名字同扩展方法的名字，比如这里是expandedMethod
     * 让其参数是扩展的类的对象
     * 让其实现是等效的操作扩展的类的对象
     *
     * 调用扩展方法时实际上调用的是这个插入到字节码中的方法
     * 扩展方法不可以有参数
     * */
    fun Int.expandedMethod(): Int {
        return this + 2
    }

    /**
     * 属性扩展：在现有的类上增加一个属性，比如这里在Int类型上增加一个属性expandedProperty
     * 声明属性时必须声明getter和setter方法，表示这个属性被读写时会执行的动作
     * 其原理与扩展方法类似，也是在属性扩展所处的类中，比如这里是KotlinElementariesKt中，增加一个静态getter方法，一个静态setter方法
     *
     * 读写扩展属性时实际上调用的是这两个插入到字节码中的静态getter和setter方法
     * [扩展属性在字节码中没有属性来对应，仅仅由生成的静态getter和setter方法来支持读写功能]
     * */
    var Int.expandedProperty:Int
        get() {return 1}
        set(value) {}

    fun testMethodExpansion(){
        var number:Int = 2
        number = number.expandedMethod()
        println("methodExpansion = " + number + ", type = " + number::class + "/" + number::class.java)
    }

    fun testPropertyExpansion(){
        var number:Int = 2
        println("propertyExpansion = " + number.expandedProperty + ", type = " + number::class + "/" + number::class.java)
    }
}
