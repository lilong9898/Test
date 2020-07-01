package com.lilong.kaptprocessortest

import com.lilong.kaptannotation.SUT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
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
 * AbstractProcessor 的说明
 * https://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/AbstractProcessor.html
 */
class SUTAnnotationProcessor : AbstractProcessor() {

    private lateinit var elementUtils: Elements
    private lateinit var outputDir: File

    override fun init(processorEnv: ProcessingEnvironment) {
        super.init(processorEnv)
        elementUtils = processorEnv.elementUtils
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

        val fileSpecBuilder = FileSpec.builder(getPackageName(typeElement), "BaseTest")

        for (element in typeElement.enclosedElements) {

            log(WARNING, element.toString())
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

//    private fun createTypeSpecBuilder(typeElement: TypeElement): TypeSpec.Builder {
//        val originName = typeElement.qualifiedName.toString()
//        val packageName = getPackageName(typeElement)
//        return TypeSpec.classBuilder("BaseTest")
//    }

    private fun Element.isField() = this.kind == FIELD

    private fun Element.isPrivate() = this.modifiers.contains(PRIVATE)

    private fun Element.isFinal() = this.modifiers.contains(FINAL)

    private fun buildSetterFunction(typeElement: TypeElement, variableElement: VariableElement): FunSpec {
        return FunSpec.builder("set${capitalize(variableElement.simpleName.toString())}")
                .receiver(typeElement.asType().asTypeName())
                .addParameter(variableElement.buildSetterFunctionParameterSpec())
                .build()
    }

    private fun VariableElement.buildSetterFunctionParameterSpec(): ParameterSpec {
        return ParameterSpec.builder(simpleName.toString(), asType().asTypeName()).build()
    }

    private fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            log(ERROR, "Invalid element type, class expected", this)
            return null
        }
        return this
    }

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

    private fun getPackageName(type: TypeElement): String {
        return elementUtils.getPackageOf(type).qualifiedName.toString()
    }

    private fun log(kind: Kind, message: String, element: Element? = null) {
        processingEnv.messager.printMessage(kind, message, element)
    }

    companion object {
        private const val OUTPUT_DIR_OPTION_KEY = "outputDir"
    }
}
