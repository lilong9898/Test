package com.lilong.kaptprocessortest

import com.lilong.kaptannotation.SUT
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic.Kind
import javax.tools.Diagnostic.Kind.WARNING

/**
 * 这个注解处理器，可以在 test 目录下，给被测类的私有成员变量生成 setter
 * (1) setter 内部通过反射来实际设置私有变量
 * (2) setter 是被测类上的扩展方法
 *
 * 这样可以实现:
 * (1) main 目录下的被测类，私有变量的可见性不变，还是私有的，无法从外部操作
 * (2) test 目录下的被测类，可以通过生成的 setter 来操作私有变量
 */
class SUTAnnotationProcessor : AbstractProcessor() {

    private lateinit var elementUtils: Elements
    private lateinit var outputDir: File

    override fun init(processorEnv: ProcessingEnvironment) {
        super.init(processorEnv)
        elementUtils = processorEnv.elementUtils
        /*
          在 build.gradle 中用一个 option key 设置上 option value
          在注解处理器中用这个 option key 就可以获取到 option value
          这种机制可以将 build.gradle 中才能确定的信息传递给注解处理器，比如项目的目录路径
        */
        outputDir = File(processingEnv.options[OUTPUT_DIR_OPTION_KEY])
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(SUT::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val annotatedElements = roundEnv.getElementsAnnotatedWith(SUT::class.java)

        annotatedElements.filterIsInstance<TypeElement>().forEach {
            processAnnotatedTypeElement(it)
        }

        return true
    }

    private fun processAnnotatedTypeElement(typeElement: TypeElement) {

        val fileSpecBuilder = FileSpec.builder(typeElement.getPackageName(), "${typeElement.simpleName}GeneratedTestUtil")

        val methodNames = mutableSetOf<String>() // 这个类中所有方法的名字都存起来

        typeElement.enclosedElements.filter {
            it.isMethod() && it is ExecutableElement
        }.forEach {
            methodNames.add(it.simpleName.toString())
        }

        typeElement.enclosedElements.filter {
            it.isField() && it.isPrivate() && (!it.isFinal()) && it is VariableElement
        }.forEach {
            it as VariableElement
            val funSpec = buildSetterFunction(typeElement, it)

            /*
               如果类中已经有 setter 了，就不需再生成 setter
               因为 kotlin 中的公有变量，字节码层面上是私有变量 + 自动生成的 getter/setter
               所以检测元素的 Kind 是否为 PRIVATE 会不准，公有变量的 Kind 也是 PRIVATE，
               但是它不应被注解处理器生成 setter，因为 kotlin 已经生成了
             */
            if (funSpec.name !in methodNames) {
                fileSpecBuilder.addFunction(funSpec)
            }
        }

        try {
            fileSpecBuilder.build().writeTo(outputDir)
        } catch (e: IOException) {
            log("write file fails, exception = $e")
        }
    }

    private fun Element.isMethod() = this.kind == METHOD

    private fun Element.isField() = this.kind == FIELD

    private fun Element.isPrivate() = this.modifiers.contains(PRIVATE)

    private fun Element.isFinal() = this.modifiers.contains(FINAL)

    /** 给被测类的这个私有变量生成 setter */
    private fun buildSetterFunction(typeElement: TypeElement, variableElement: VariableElement): FunSpec {
        return FunSpec.builder("set${variableElement.simpleName.toString().capitalize()}")
                .receiver(typeElement.asType().asTypeName())
                .addCode(buildSetterFunctionBody(typeElement, variableElement))
                .addParameter(buildSetterFunctionParameterSpec(variableElement))
                .build()
    }

    /** 给被测类的这个私有变量生成 setter 时，生成参数的过程 */
    private fun buildSetterFunctionParameterSpec(variableElement: VariableElement): ParameterSpec {
        val variableTypeName = variableElement.asType().asTypeName().toKotlinIfNeeded()
        return ParameterSpec.builder(variableElement.simpleName.toString(), variableTypeName).build()
    }

    /** 给被测类的这个私有变量生成 setter 时，生成方法体的过程 */
    private fun buildSetterFunctionBody(typeElement: TypeElement, variableElement: VariableElement): CodeBlock {
        val codeBlockBuilder = CodeBlock.builder()
        codeBlockBuilder.addStatement("val field = this::class.java.getDeclaredField(\"%L\")", variableElement.simpleName)
        codeBlockBuilder.addStatement("field.isAccessible = true")
        codeBlockBuilder.addStatement("field.set(this, %L)", variableElement.simpleName)
        return codeBlockBuilder.build()
    }

    // region util

    /** 获取一个元素所代表的类/接口的包名 */
    private fun TypeElement.getPackageName(): String {
        return elementUtils.getPackageOf(this).qualifiedName.toString()
    }

    /**
     * 将一个类型转换成 kotlin 类型
     * 注解处理器返回给我们的元素类型可能是 java 类型，而希望单测代码中使用的都是 kotlin 类型
     */
    private fun TypeName.toKotlinIfNeeded(): TypeName {
        return when (this) {
            is ClassName -> toKotlinIfNeeded()
            is ParameterizedTypeName -> toKotlinIfNeeded()
            is LambdaTypeName -> toKotlinIfNeeded()
            else -> this
        }
    }

    /**
     * 对于不含类型参数的类型，如果是 java 类型，就将它转换成 kotlin 类型
     * 实测发现对于某些 kotlin 类型，和我们写的类，是无法被 java 的类加载器加载的，会引发异常
     * 这也正好说明了他们不需转换，所以异常处理中就把他们原样返回
     */
    private fun ClassName.toKotlinIfNeeded(): ClassName {
        try {
            val kClass = Class.forName(this.canonicalName).kotlin
            val kClassPackageName = kClass.qualifiedName?.substringBeforeLast(".") ?: return this
            val kClassSimpleName = kClass.qualifiedName?.substringAfterLast(".") ?: return this
            return ClassName(kClassPackageName, kClassSimpleName)
        } catch (e: Exception) {
            return this
        }
    }

    /**
     * 对于含类型参数的类型，如果类型本身或者类型参数是 java 类型，就将他们转换成 kotlin 类型
     * 比如数组，集合
     * 内部会分别转换类型本身和类型参数，然后再组装起来
     */
    private fun ParameterizedTypeName.toKotlinIfNeeded(): ParameterizedTypeName {
        val typeArguments = typeArguments.map { it.toKotlinIfNeeded() }.toTypedArray()
        return ParameterizedTypeName.get(rawType.toKotlinIfNeeded(), *typeArguments)
    }

    private fun LambdaTypeName.toKotlinIfNeeded(): LambdaTypeName {
        val receiver = receiver?.toKotlinIfNeeded()
        val parameters = parameters
        val returnType = returnType.toKotlinIfNeeded()
        return LambdaTypeName.Companion.get(receiver, parameters, returnType)
    }

    // endregion

    // region log

    private fun log(message: String, kind: Kind = WARNING, element: Element? = null) {
        processingEnv.messager.printMessage(kind, message, element)
    }

    // endregion

    companion object {
        private const val OUTPUT_DIR_OPTION_KEY = "SUTAnnotationProcessorOutputDir"
    }
}
