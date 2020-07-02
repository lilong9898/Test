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
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic.Kind
import javax.tools.Diagnostic.Kind.ERROR
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

        if (annotatedElements.isEmpty()) {
            return false
        }

        for (element in annotatedElements) {

            val typeElement = element.toTypeElementOrNull() ?: continue
            processAnnotatedTypeElement(typeElement)
        }

        return true
    }

    private fun processAnnotatedTypeElement(typeElement: TypeElement) {

        val fileSpecBuilder = FileSpec.builder(typeElement.getPackageName(), "${typeElement.simpleName}GeneratedTestUtil")

        for (element in typeElement.enclosedElements) {

            if (!element.isField() || !element.isPrivate() || element.isFinal()) {
                continue
            }

            if (element is VariableElement) {
                fileSpecBuilder.addFunction(buildSetterFunction(typeElement, element))
            }
        }

        try {
            fileSpecBuilder.build().writeTo(outputDir)
        } catch (e: IOException) {
            log("write file fails, exception = $e")
        }
    }

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

    /** 检测某个元素是否代表类/接口 */
    private fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            log("Invalid element type, class expected", ERROR, this)
            return null
        }
        return this
    }

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
            else -> this
        }
    }

    /**
     * 将不含类型参数的 java 类型转换成 kotlin 类型
     * 某些 kotlin 类型，和是我们写的类，无法被 java 的类加载器加载，会引发异常
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
     * 将含有类型参数的 java 类型转换成 kotlin 类型
     * 比如数组，集合
     * 内部会分别转换类型本身和类型参数，然后再组装起来
     */
    private fun ParameterizedTypeName.toKotlinIfNeeded(): ParameterizedTypeName {
        val typeArguments = typeArguments.map { it.toKotlinIfNeeded() }.toTypedArray()
        return ParameterizedTypeName.get(rawType.toKotlinIfNeeded(), *typeArguments)
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
