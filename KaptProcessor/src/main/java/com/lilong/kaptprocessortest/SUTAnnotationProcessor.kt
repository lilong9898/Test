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

        val fileSpecBuilder = FileSpec.builder(getPackageName(typeElement), "${typeElement.simpleName}GeneratedTestUtil")

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
            log(WARNING, "write file fails, exception = $e")
        }
    }

    private fun Element.isField() = this.kind == FIELD

    private fun Element.isPrivate() = this.modifiers.contains(PRIVATE)

    private fun Element.isFinal() = this.modifiers.contains(FINAL)

    /** 给被测类的这个私有变量生成 setter */
    private fun buildSetterFunction(typeElement: TypeElement, variableElement: VariableElement): FunSpec {
        return FunSpec.builder("set${capitalize(variableElement.simpleName.toString())}")
                .receiver(typeElement.asType().asTypeName())
                .addCode(buildSetterFunctionBody(typeElement, variableElement))
                .addParameter(buildSetterFunctionParameterSpec(variableElement))
                .build()
    }

    /** 给被测类的这个私有变量生成 setter 时，生成参数的过程 */
    private fun buildSetterFunctionParameterSpec(variableElement: VariableElement): ParameterSpec {
        return ParameterSpec.builder(variableElement.simpleName.toString(), variableElement.asType().asTypeName()).build()
    }

    /** 给被测类的这个私有变量生成 setter 时，生成方法体的过程 */
    private fun buildSetterFunctionBody(typeElement: TypeElement, variableElement: VariableElement): CodeBlock {
        val codeBlockBuilder = CodeBlock.builder()
        codeBlockBuilder.addStatement("val field = this::class.java.getDeclaredField(\"%L\")", variableElement.simpleName)
        codeBlockBuilder.addStatement("field.isAccessible = true")
        codeBlockBuilder.addStatement("field.set(this, %L)", variableElement.simpleName)
        return codeBlockBuilder.build()
    }

    /** 检测某个元素是否代表类/接口 */
    private fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            log(ERROR, "Invalid element type, class expected", this)
            return null
        }
        return this
    }

    /** 将一个字符串的首字母变成大写的 */
    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
        }
    }

    /** 获取一个类的包名 */
    private fun getPackageName(type: TypeElement): String {
        return elementUtils.getPackageOf(type).qualifiedName.toString()
    }

    private fun log(kind: Kind, message: String, element: Element? = null) {
        processingEnv.messager.printMessage(kind, message, element)
    }

    companion object {
        private const val OUTPUT_DIR_OPTION_KEY = "SUTAnnotationProcessorOutputDir"
    }
}
